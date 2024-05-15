module Contrato

  def before_and_after_each_call(proc_before, proc_after)
    @procsBefore ||= []
    @procsBefore.push(proc_before)
    @procsAfter ||= []
    @procsAfter.push(proc_after)
  end

  def invariant(&procRecibido)
    #proc que reciba el proc de la invariante, que lo evalue en self,
    # podemos cambiar el unless del method added por
    procInvariante = proc{
        raise "Invariant no cumplida" if !self.instance_eval(&procRecibido)
    }
    before_and_after_each_call(proc{}, procInvariante)
    @procsInvariantes ||= []
    @procsInvariantes.push(procInvariante)
  end


  # [CORRECCION]
  # Falta que los parametros del metodo esten disponibles dentro del contexto del pre
  def pre(&procPreRecibido)
    procPre = proc{
        raise "Precondicion no cumplida" if !self.instance_eval(&procPreRecibido)
    }

    @procsPre ||= []
    @procsPre.push(procPre)
  end

  # [CORRECCION]
  # Falta que los parametros del metodo esten disponibles dentro del contexto del pre
  # y que reciba el resultado del metodo como parametro
  def pos(&procPostRecibido)
    procPost = proc{
        raise "Postcondición no cumplida" if !self.instance_eval(&procPostRecibido)
    }
    @procsPost ||= []
    @procsPost.push(procPost)
  end

  def method_added(method_name)
    # [CORRECCION]
    # Explicar concepto de booly
    #@seSobreescribio ||= false
    original_method = instance_method(method_name)
    # [CORRECCION]
    # Acá están creando un diccionario cada vez que se ejecuta method_added
    # con $ lo hacemos global, puede funcar (probar)
    preList ||= {}
    preList[method_name] = procPre
    postList ||= {}
    postList[method_name] = procPost
      if !@seSobreescribio
        @seSobreescribio = true
        if method_name == :initialize
          # [CORRECCION]
          # En lugar de copiar el proc al contexto, ¿por qué no pedirse a la clase de self?
          invariantProc = procInvariant
          define_method(method_name) do |*args, &block|
            preList[method_name].call(self)
            original_method.bind(self).call(*args, &block)
            postList[method_name].call(self)
            invariantProc.call(self)
          end
        else
          afterProc = procAfter
          beforeProc = procBefore
          define_method(method_name) do |*args, &block|
            #LOGRAR QUE LOS PROCS PRE Y POST SE EJECUTEN DENTRO DEL CONEXTO DEL
            # MÉTODO, ES LA UNICA FORMA DE QUE RECONOZCA VARIABLES DE EL PARÁMETRO
            beforeProc.call(self)
            preList[method_name].call(self)
            ret = original_method.bind(self).call(*args, &block)
            postList[method_name].call(self)
            afterProc.call(self)
            return ret
          end
        end
      else
        @seSobreescribio = false
      end
    @procsPost = []
    @procsPre = []
  end

  # [CORRECCION] -- ¿"Hecha"?
  # Si estan creando un proc que ejecute todo el acumulado para ver si alguno tira la excepción
  # ¿Por qué no armar uno que tire la excepción si alguno de los proc da false?
  def procBefore()
    if @procsBefore.nil?
    proc{}
    else
      #proc{|obj| @procsBefore.each do |before| obj.instance_eval(&before) end}
      proc{@procsBefore.any?{|proc| proc.call(self)}} #si lo ven correcto, transportar al resto
    end
  end

  def procAfter()
    if @procsAfter.nil?
      proc{}
    else
      proc{|obj| @procsAfter.each do |after| obj.instance_eval(&after) end}
    end
  end
  def procInvariant()
    if @procsInvariantes.nil?
      proc{}
    else
      proc{|obj| @procsInvariantes.each do |invariant| obj.instance_eval(&invariant) end}
    end
  end

  def procPre()
    if @procsPre.nil?
      proc{}
    else
      preProcs = @procsPre.clone
      proc{|obj| preProcs.each do |preProc| obj.instance_eval(&preProc) end}
    end
  end

  def procPost()
    if @procsPost.nil?
      proc{}
    else
      postProcs = @procsPost.clone
      proc{|obj| postProcs.each do |postProc| obj.instance_eval(&postProc) end}
    end
  end
end






#
# class Module
#   def invariant(&block)
#     @invariantes ||= []
#     @invariantes.push(block)
#   end
#   def before_and_after_each_call(procBefore, procAfter)
#     @interceptor ||= Interceptor.new
#     @interceptor.agregarBefore(procBefore)
#     @interceptor.agregarAfter(procAfter)
#   end
#
# #proc "intermedio" que chequee la invariante recibida, before and after, y que en caso de no cumplirse, tirar el error.
#
# #hablado en checkpoint
#
#
# end
#
# class ClassPrueba
#   def self.method_added(name)
#     puts"ENTRE METHOD ADDED"
#     unbound_method = self.instance_method(name)
#     @seSobreescribio ||= false
#     if !@seSobreescribio
#       @seSobreescribio = true
#       puts " #{self.instance_methods(false)}"
#       self.define_method(name) do
#         self.instance_eval(&@interceptor.procBefore)
#         unbound_method.bind(self).call
#         self.instance_eval(&@interceptor.procAfter)
#       end
#     else
#       @seSobreescribio = false
#     end
#   end
# end
#
#
# #usar attr accesor y metodos de atributos
# #esto no haria falta si el initialize esta contem´lado con el method added
# # class Class
# #   def new(args, &block)
# #     # Tu implementación personalizada del método new aquí
# #     instancia = allocate  # Crea una nueva instancia sin llamar a initialize
# #     instancia.initialize(args, &block)  # Llama a initialize con los argumentos proporcionados
# #     @invariantes.map { |invariant| instancia.instance_eval(invariant)}
# #     return instancia
# #   end
# # end