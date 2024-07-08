package domain

import scala.util.{Success, Try}


type StatPrincipal = Heroe => Int

val Fuerza: StatPrincipal       = _.getFuerza() 

val Hp: StatPrincipal           = _.getHp() 

val Velocidad: StatPrincipal    = _.getVelocidad() 

val Inteligencia: StatPrincipal = _.getInteligencia()


type Restriccion = Heroe => Boolean

type CondicionDeRealizacion = Equipo => Boolean

type Modificacion = Heroe => Heroe

type Cuantificador = Heroe => Int

type Criterio = (Equipo, Equipo) => Boolean

type Facilidad = Heroe => Int  // OPTON[Int]

type Recompensa = Equipo => Equipo
//
//def incrementarSegun(stats:Stats, condicion: Restriccion) : Recompensa
//
//val mision = Mision(List(), incrementarSegun(Stats(hp = 10), _.trabajo == Mago))

object Result {
  def apply(equipo: => Equipo): Try[Equipo] = 
    Success(equipo)
  
}

