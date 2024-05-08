require 'rspec'
require_relative '../lib/prueba.rb'
describe 'Prueba' do
    it 'debería pasar este test' do
      class Persona
        extend Contrato
        attr_accessor :algo

        invariant{ algo > 4 }
        invariant{ algo >= 0}
        #invariant{ algo < 0}

        before_and_after_each_call(
          proc{@algo = @algo +5},
          proc{@algo = @algo - 3}
        )
        before_and_after_each_call(
          proc{puts"hola"},
          proc{puts"chau"}
        )
        before_and_after_each_call(
          proc{puts"hola"},
          proc{puts"chau"}
        )

        def initialize(algo)
          @algo = algo
        end

        pos {puts"#{algo}"}
        def hablar()
          @algo = @algo-1
        end

        pre { algo >= 0 }
        pos { algo >= 0 }
        def morir()
          puts "MORI"
        end
      end

      pepe = Persona.new(5)
      pepe.hablar
      puts "nuevo metodo"
      pepe.morir

      # expect(pepe.algo).to eq(3)
      # pepe.hablar
      # expect(pepe.algo).to eq(6)
    end

    it 'ejemplo' do
      class Pila
        extend Contrato
        attr_accessor :current_node, :capacity

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

    end
end