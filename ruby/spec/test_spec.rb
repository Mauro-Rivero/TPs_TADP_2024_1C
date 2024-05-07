require 'rspec'
require_relative '../lib/prueba.rb'
describe 'Prueba' do
    it 'debería pasar este test' do
      class Persona
        extend Contrato

        before_and_after_each_call(
          proc{puts"hola"},
          proc{puts"chau"}
        )

        def hablar(algo)
          puts "#{algo}"
        end
      end

      pepe = Persona.new
      pepe.hablar(2)
      Persona.define_method(:reir)do
        puts "JAJAJAJA"
      end
      pepe.reir

      class Auto
        extend Contrato
        before_and_after_each_call(proc{puts"brum"},proc{puts"choque"})
      end

      Auto.define_method(:algo)do
        puts"CULOOOOOOOOOOOOOOO"
      end

      Auto.new.algo
      pepe.reir
    end
end