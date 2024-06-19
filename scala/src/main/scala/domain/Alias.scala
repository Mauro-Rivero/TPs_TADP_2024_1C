package domain

type StatPrincipal = Function1[Heroe, Int]

val Fuerza: StatPrincipal       = (heroe:Heroe) => heroe.getStats().fuerza 

val Hp: StatPrincipal           = (heroe:Heroe) => heroe.getStats().hp 

val Velocidad: StatPrincipal    = (heroe:Heroe) => heroe.getStats().velocidad 

val Inteligencia: StatPrincipal = (heroe:Heroe) => heroe.getStats().inteligencia 


type Restriccion = Function1[Heroe, Boolean]