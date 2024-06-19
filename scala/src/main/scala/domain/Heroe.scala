package domain
import scala.collection.Set

abstract class Heroe (val statsBase: Stats){
  val inventario : List[Item] = List()
  val trabajo : Option[Trabajo] = None
  val cabeza : Option[Cabeza] = None
  val torso : Option[Torso] = None
  val manoIzquierda: Option[Arma] = None
  val manoDerecha: Option[Arma] = None

  def saludar() : Unit
   println("hola")

  def getStats(): Stats = {
    Set(trabajo, cabeza, torso, armaEquipada(), talismanes()).map(_.stats).fold(statsBase)(_+_).toValid()
  }
  
  def talismanes(): Unit = {
    
  }
  
  def armaEquipada(): Unit = {
    
  }

}

