package domain

abstract class Heroe (val statsBase: Stats){
  val inventario : List[Item] = List()
  val trabajo : Trabajo
  val casco : Option[Casco]
  val manoIzquierda: Option[Arma]
  val manoDerecha: Option[Arma]
  val stats:Stats

  def saludar() : Unit
   println("hola")

  def getStats(): Stats = {
  List(trabajo, casco, manoDerecha, manoIzquierda).fold(statsBase)()
  }

  def cambiarTrabajoA(nuevoTrabajo: Trabajo): Unit = {
    this.copy(trabajo = nuevoTrabajo)
  }
}
