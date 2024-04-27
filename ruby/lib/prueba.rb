class Interceptor
  attr_accessor :procBefore, :procAfter, :clase

  def initialize(procBefore, procAfter, clase)
    @procBefore = procBefore
    @procAfter = procAfter
    @clase = clase
  end

  def before_and_after_each_call(name, *args)
    @procBefore.call
    @clase.send(name, args)
    @procAfter.call

  end
end

class Module
  
  def invariant(&block)
    @invariantes ||= []
    @invariantes.push(block)
  end
  def before_and_after_each_call(procBefore, procAfter)
    @interceptor = Interceptor.new(procBefore, procAfter, self)
  end

#proc "intermedio" que chequee la invariante recibida, before and after, y que en caso de no cumplirse, tirar el error.

  def method_missing(name, *args)
    @interceptor.before_and_after_each_call(name, args)
    super
  end
#hablado en checkpoint
  def method_added(name)
    unbound_method = self.instance_method(name)
    seSobreescribio = false
    define_method(name) do
      @interceptor.before
      unbound_method.bind(self).call
      @interceptor.after
    end
  end

end

#usar attr accesor y metodos de atributos
#esto no haria falta si el initialize esta contem´lado con el method added
class Class
  def new(args, &block)
    # Tu implementación personalizada del método new aquí
    instancia = allocate  # Crea una nueva instancia sin llamar a initialize
    instancia.initialize(args, &block)  # Llama a initialize con los argumentos proporcionados
    @invariantes.map { |invariant| instancia.instance_eval(invariant)}
    return instancia
  end
end

class Guerrero
  before_and_after_each_call(proc{puts "entre"},proc { puts "sali"})
  before_and_after_each_call(proc{puts "otra"},proc { puts "sali 2"})


  pre {puts ''} #tiene que dejar una "nota" para que el method_added sepa qué ejecutar/cuando hacerlo.
  def hablar
    puts "hola"
  end
end
metodo(&block)