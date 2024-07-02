package domain

type StatPrincipal = Function1[Heroe, Int]

val Fuerza: StatPrincipal       = (heroe:Heroe) => heroe.getFuerza() 

val Hp: StatPrincipal           = (heroe:Heroe) => heroe.getHp() 

val Velocidad: StatPrincipal    = (heroe:Heroe) => heroe.getVelocidad() 

val Inteligencia: StatPrincipal = (heroe:Heroe) => heroe.getInteligencia()


type Restriccion = Function1[Heroe, Boolean]

type Modificacion = Function1[Heroe, Stats]

type Cuantificador = Function1[Heroe, Int]

