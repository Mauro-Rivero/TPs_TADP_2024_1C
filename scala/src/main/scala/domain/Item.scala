package domain

case class Item( modificacion: Modificacion, restriccion: Restriccion, tipo: Tipo, precio: Int = 0) {
  
  def apply(heroe:Heroe): Heroe = {
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


def fuerzaBaseMayorA(valor: Int): Restriccion = { (heroe: Heroe) => heroe.statsBase.fuerza > valor }

val palitoMagico: Item = Item(_.alterarInteligencia(20), _.trabajo == Mago, UnaMano:Arma)

val cascoVikingo: Item = Item(_.alterarHp(10), _.getFuerzaBase()>30, Casco:Cabeza)

