package domain

case class Item( modificacion: Modificacion, restriccion: Restriccion, tipo: Tipo, precio: Int = 0) {
  
  def apply(heroe:Heroe): Stats = {
    modificacion(heroe)
  }
}

sealed trait Tipo

sealed trait Cabeza extends Tipo

case object Casco extends Cabeza

case object Sombrero extends Cabeza

sealed trait Torso extends Tipo

case object Armadura extends Torso

case object Vestido extends Torso

case object Talisman extends Tipo

sealed trait Arma extends Tipo

sealed trait Escudo extends Tipo

case object UnaMano extends Arma with Escudo

case object DosManos extends Arma

//
//type Cabeza <: Tipo
//type Sombrero <: Cabeza
//type Casco <: Cabeza
//type Torso <: Tipo
//type Armadura <: Torso
//type Vestido <: Torso
//type UnaMano <: Tipo
//type DosManos <: Tipo
//type Talisman <: Tipo
//type Arma <: Item
//type Escudo <: Item

def fuerzaBaseMayorA(valor: Int): Restriccion = { (heroe: Heroe) => heroe.statsBase.fuerza > valor }
//val cascoVikingo: Item = Item(new Stats(hp = 10), fuerzaBaseMayorA(30))

//val soloMagos: Restriccion = (heroe: Heroe) => heroe.trabajo.contains(Mago) // TODO ARREGLAR

val palitoMagico: Item = Item(_ => Stats(inteligencia = 20), _.trabajo == Mago, UnaMano:Arma)
//
//Casco Vikingo: +10 hp, sólo lo pueden usar héroes con fuerza base > 30. Va en la cabeza.
//  Palito mágico: +20 inteligencia, sólo lo pueden usar magos (>o ladrones con más de 30 de inteligencia base). Una mano.
//Armadura Elegante-Sport: +30 velocidad, -30 hp. Armadura.
//  Arco Viejo: +2 fuerza. Ocupa las dos manos.
//Escudo Anti-Robo: +20 hp. No pueden equiparlo los ladrones ni nadie con menos de 20 de fuerza base. Una mano.
//Talismán de Dedicación: Todos los stats se incrementan 10% del valor del stat principal del trabajo.
//  Talismán del Minimalismo: +50 hp. -10 hp por cada otro ítem equipado.
//  Vincha del búfalo de agua: Si el héroe tiene más fuerza que inteligencia, +30 a la inteligencia; de lo contrario +10 a todos los stats menos la inteligencia. Sólo lo pueden equipar los héroes sin trabajo. Sombrero.
//Talismán maldito: Todos los stats son 1.
//Espada de la Vida: Hace que la fuerza del héroe sea igual a su hp.

