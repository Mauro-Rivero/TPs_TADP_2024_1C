package domain


type StatPrincipal = Heroe => Int

val Fuerza: StatPrincipal       = _.getFuerza() 

val Hp: StatPrincipal           = _.getHp() 

val Velocidad: StatPrincipal    = _.getVelocidad() 

val Inteligencia: StatPrincipal = _.getInteligencia()


type Restriccion = Heroe => Boolean

type CondicionDeRealizacion = Equipo => Boolean

type Modificacion = Heroe => Heroe

type Cuantificador = Heroe => Int

type CuantificadorOptional = Heroe => Option[Int]

type Criterio = (Equipo, Equipo) => Boolean

type Facilidad = Heroe => Option[Int]  // OPTON[Int]

type Recompensa = Equipo => Equipo

