package domain

// debe ser una trait?
class Trabajo(val stats: Stats, val statPrincipal: StatPrincipal) {
}

val Guerrero = new Trabajo(new Stats(hp = 10, fuerza = 15, inteligencia = -10), Fuerza)
val Mago = new Trabajo(new Stats(fuerza = -20, inteligencia = 20), Inteligencia)
val Ladron = new Trabajo(new Stats(hp = -5, velocidad = 10), Velocidad)