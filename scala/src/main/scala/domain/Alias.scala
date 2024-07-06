package domain

type StatPrincipal = Heroe => Int

val Fuerza: StatPrincipal       = _.getFuerza() 

val Hp: StatPrincipal           = _.getHp() 

val Velocidad: StatPrincipal    = _.getVelocidad() 

val Inteligencia: StatPrincipal = _.getInteligencia()


type Restriccion = Heroe => Boolean

type Modificacion = Heroe => Stats

type Cuantificador = Heroe => Int

type Criterio = (Equipo, Equipo) => Boolean

type Dificultad = Heroe => Int

type Recompensa = Equipo => Equipo