package domain

class Item( modificacion: Modificacion, restriccion: Restriccion) {
  
  def apply(heroe:Heroe): Stats = {
    modificacion(heroe)
  }
}

case class Cabeza(modificacion: Modificacion, restriccion: Restriccion) extends Item(modificacion, restriccion)
class Casco(modificacion: Modificacion, restriccion: Restriccion) extends Cabeza(modificacion, restriccion)
case class Torso(modificacion: Modificacion, restriccion: Restriccion) extends Item(modificacion, restriccion)
case class Talisman(modificacion: Modificacion, restriccion: Restriccion) extends Item(modificacion, restriccion)
case class Mano(modificacion: Modificacion, restriccion: Restriccion, tipo:Tipo) extends Item(modificacion, restriccion)

sealed trait Tipo
case object UnaMano extends Tipo
case object DosManos extends Tipo

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

//val palitoMagico: Item = Item(Stats(inteligencia = 20), soloMagos)
//
//Casco Vikingo: +10 hp, sólo lo pueden usar héroes con fuerza base > 30. Va en la cabeza.
//  Palito mágico: +20 inteligencia, sólo lo pueden usar magos (o ladrones con más de 30 de inteligencia base). Una mano.
//Armadura Elegante-Sport: +30 velocidad, -30 hp. Armadura.
//  Arco Viejo: +2 fuerza. Ocupa las dos manos.
//Escudo Anti-Robo: +20 hp. No pueden equiparlo los ladrones ni nadie con menos de 20 de fuerza base. Una mano.
//Talismán de Dedicación: Todos los stats se incrementan 10% del valor del stat principal del trabajo.
//  Talismán del Minimalismo: +50 hp. -10 hp por cada otro ítem equipado.
//  Vincha del búfalo de agua: Si el héroe tiene más fuerza que inteligencia, +30 a la inteligencia; de lo contrario +10 a todos los stats menos la inteligencia. Sólo lo pueden equipar los héroes sin trabajo. Sombrero.
//Talismán maldito: Todos los stats son 1.
//Espada de la Vida: Hace que la fuerza del héroe sea igual a su hp.

