module Contrato

  def before_and_after_each_call(proc_before, proc_after)
    @procsBefore ||= []
    @procsBefore.push(proc_before)
    @procsAfter ||= []
    @procsAfter.push(proc_after)
  end

  def invariant(&procRecibido)
    @procsInvariantes ||= []
    @procsInvariantes.push(procRecibido)
  end


  # [CORRECCION]
  # Falta que los parametros del metodo esten disponibles dentro del contexto del pre
  def pre(&procRecibido)
    @procsPre ||= []
    @procsPre.push(procRecibido)
  end

  # [CORRECCION]
  # Falta que los parametros del metodo esten disponibles dentro del contexto del pre
  # TODO y que reciba el resultado del metodo como parametro DONE
  def pos(&procRecibido)
    @procsPost ||= []
    @procsPost.push(procRecibido)
  end

  def method_added(method_name)
    # [CORRECCION]
    # TODO Explicar concepto de booly DONE
    #@seSobreescribio ||= false
    original_method = instance_method(method_name)
    parametros = original_method.parameters.map{|arg| arg[1]}
    # [CORRECCION]
    # TODO Acá están creando un diccionario cada vez que se ejecuta method_added DONE
    preProc = listToProc(@procsPre, "Precondición no cumplida")
    postProc = listToProc(@procsPost, "Postcondición no cumplida")
    invariantProc = listToProc(@procsInvariantes, "Invariante no cumplida")
      if !@seSobreescribio
        @seSobreescribio = true
        # [CORRECCION]
        # TODO No hace falta convertir a string, pueden chequear contra el simbolo :initialize DONE
        if method_name == :initialize
          # [CORRECCION]
          # En lugar de copiar el proc al contexto, ¿por qué no pedirse a la clase de self?
          define_method(method_name) do |*args, &block|
            preProc.call(self)
            original_method.bind(self).call(*args, &block)
            postProc.call(self)
            invariantProc.call(self)
          end
        else

          afterProc = listToProc(@procsAfter, "")
          beforeProc = listToProc(@procsBefore, "")
          define_method(method_name) do |*args, &block|
            (0...args.length).each { |i|
              self.define_singleton_method(parametros[i])do
                args[i]
              end
            }
            beforeProc.call(self)
            preProc.call(self)
            ret = original_method.bind(self).call(*args, &block)
            postProc.call(self,ret)
            afterProc.call(self)
            invariantProc.call(self)
            return ret
          end
        end
      else
        @seSobreescribio = false
      end
    @procsPost = []
    @procsPre = []
  end

  # [CORRECCION]
    # TODO Si estan creando un proc que ejecute todo el acumulado para ver si alguno tira la excepción DONE
    # TODO ¿Por qué no armar uno que tire la excepción si alguno de los proc da false? DONE
  def listToProc(lista, mensajeDeError)
    if lista.nil?
      proc{}
    else
      listaClonada = lista.clone
      proc{|obj,result| raise mensajeDeError if listaClonada.any?{|unProc| !obj.instance_exec(result,&unProc)}}
    end
  end
end
