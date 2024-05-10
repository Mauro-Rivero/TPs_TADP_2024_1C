require 'rspec'
require_relative '../lib/prueba.rb'
describe 'Prueba' do
  class Pila
    extend Contrato
    attr_accessor :current_node, :capacity

    #AGREGAMOS ESTO PARA CHEQUEAR QUE FUNCIONE TAMBIÉN
    before_and_after_each_call(proc{puts"Entre a un metodo"}, proc{puts"Sali de un metodo"})

    invariant { capacity >= 0 }

    pos { empty? }
    def initialize(capacity)
      @capacity = capacity
      @current_node = nil
    end

    pre { !full? }
    pos { height > 0 }
    def push(element)
      @current_node = Node.new(element, current_node)
    end

    pre { !empty? }
    def pop
      element = top
      @current_node = @current_node.next_node
      element
    end

    pre { !empty? }
    def top
      current_node.element
    end

    def height
      empty? ? 0 : current_node.size
    end

    def empty?
      current_node.nil?
    end

    def full?
      height == capacity
    end

    Node = Struct.new(:element, :next_node) do
      def size
        next_node.nil? ? 1 : 1 + next_node.size
      end
    end
  end
  it 'deberia fallar invariante' do
    expect{Pila.new(-2)}.to raise_error RuntimeError
  end

  it 'deberia crearse una pila' do
    expect(Pila.new(2).class).to be(Pila)
  end

  it 'deberia fallar precondición' do
    pila = Pila.new(0)
    expect{pila.push(1)}.to raise_error RuntimeError
  end

  it 'deberia fallar precondición' do
    pila = Pila.new(0)
    expect{pila.top}.to raise_error RuntimeError
  end
end