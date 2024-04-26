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
  @interceptor
  def before_and_after_each_call(procBefore, procAfter)
    @interceptor = Interceptor.new(procBefore, procAfter, self)
  end

  def method_missing(name, *args)
    @interceptor.before_and_after_each_call(name, args)
    super
  end

  def method_added(name)
    unbound_method = instance_method(name)
    define_method(name) do
      @interceptor.before
      unbound_method.bind(self)
      @interceptor.after
    end
  end

end

class Guerrero
  before_and_after_each_call(proc{puts "entre"},proc { puts "sali"})


  def hablar
    puts "hola"
  end
end
