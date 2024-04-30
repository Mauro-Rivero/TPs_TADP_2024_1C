class Interceptor

  def agregarBefore(procBefore)
    @procsBefore ||= []
    @procsBefore.push(procBefore)
    end

  def agregarAfter(procAfter)
    @procsAfter ||= []
    @procsAfter.push(procAfter)
  end

  def procBefore()
    proc {@procsBefore.map { |procBefore| procBefore.call}}
    end
  def procAfter()
    proc {@procsAfter.map { |procAfter| procAfter.call}}
  end
end

class Module
  
  def invariant(&block)
    @invariantes ||= []
    @invariantes.push(block)
  end
  def before_and_after_each_call(procBefore, procAfter)
    @interceptor ||= Interceptor.new
    @interceptor.agregarBefore(procBefore)
    @interceptor.agregarAfter(procAfter)
  end

#proc "intermedio" que chequee la invariante recibida, before and after, y que en caso de no cumplirse, tirar el error.

#hablado en checkpoint
  def method_added(name)
    unbound_method = self.instance_method(name)
    if !@seSobreescribio
      @seSobreescribio = true
      define_method(name) do
        @interceptor.procBefore.call
        unbound_method.bind(self).call
        @interceptor.procAfter.call
      end
    else
      @seSobreescribio = false
    end
  end

end

#usar attr accesor y metodos de atributos
#esto no haria falta si el initialize esta contem´lado con el method added
# class Class
#   def new(args, &block)
#     # Tu implementación personalizada del método new aquí
#     instancia = allocate  # Crea una nueva instancia sin llamar a initialize
#     instancia.initialize(args, &block)  # Llama a initialize con los argumentos proporcionados
#     @invariantes.map { |invariant| instancia.instance_eval(invariant)}
#     return instancia
#   end
# end

class Guerrero
  before_and_after_each_call(proc{puts "entre"},proc { puts "sali"})
  before_and_after_each_call(proc{puts "otra"},proc { puts "sali 2"})


  pre {puts ''} #tiene que dejar una "nota" para que el method_added sepa qué ejecutar/cuando hacerlo.
  def hablar
    puts "hola"
  end

end

pepe = Guerrero.new

pepe.hablar

