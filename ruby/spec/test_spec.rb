require 'rspec'
require_relative '../lib/prueba.rb'
describe 'Prueba' do
    it 'debería pasar este test' do
      class Persona < ClassPRUEBA
        attr_accessor :contador
        before_and_after_each_call(proc {@contador += 1}, proc{@contador -= 4})

        def initialize(contador)
          @contador = contador
        end
        def hablar
          @contador += 1
        end

      end

      pepe = Persona.new(2)
      pepe.hablar
      expect(pepe.contador).to eq(0)
    end
end