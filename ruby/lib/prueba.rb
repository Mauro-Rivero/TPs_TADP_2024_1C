class Interceptor
  attr_accessor :procBefore, :procAfter
  def agregarBefore(procBefore)
    @procBefore||=procBefore
  end

  def agregarAfter(procAfter)
    @procAfter||=procAfter
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


end

class Class
  def self.method_added(name)
    puts"ENTRE METHOD ADDED"
    unbound_method = self.instance_method(name)
    @seSobreescribio ||= false
    if !@seSobreescribio
      @seSobreescribio = true
      puts " #{self.instance_methods(false)}"
      self.define_method(name) do
        self.instance_eval(&@interceptor.procBefore)
        unbound_method.bind(self).call
        self.instance_eval(&@interceptor.procAfter)
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