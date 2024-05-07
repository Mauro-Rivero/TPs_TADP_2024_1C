require 'rspec'
require_relative '../lib/prueba.rb'
describe 'Prueba' do
    it 'debería pasar este test' do
      class Persona
        extend Contrato
        attr_accessor :algo
        def initialize
          @algo ||= 0
        end
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


        def hablar()
          @algo = @algo+1
        end
      end

      pepe = Persona.new
      pepe.hablar()
      expect(pepe.algo).to eq(3)
      pepe.hablar
      expect(pepe.algo).to eq(6)
    end
end