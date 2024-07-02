package domain
import scala.collection.Set

case class Heroe (var statsBase: Stats, var inventario: Inventario, var trabajo: Trabajo){
  //Deberia haber un trabajo Desempleado? o seria una monada?
  require(getFuerza() >= 1)
  require(getHp() >= 1)
  require(getVelocidad() >= 1)
  require(getInteligencia() >= 1)
  //No va a romper en ningun momento que no sea la creacion del heroe, pq siempre lo mantenemos en estaods validos

  def getStats(): Stats = {
    (inventario.stats(this) + trabajo.stats ).toValid
  }

  def getFuerza(): Int = getStats().fuerza

  def getInteligencia(): Int = getStats().inteligencia

  def getVelocidad(): Int = getStats().velocidad

  def getHp(): Int = getStats().hp
  
  def statPrincipal(): Int = trabajo.statPrincipal(this)

  def cambiarTrabajo(untraba: Trabajo): Unit = {
    trabajo = untraba
  }

  def equiparItem(item: Item): Heroe = {
    if(item.restriccion(this)){
      this.copy(inventario = inventario.equipar(item))
    } else{
      this
    }
  }

  def esPositivoEquipar(item: Item): Boolean = {
    statPrincipal() < equiparItem(item).statPrincipal()
  }
}

//cambiarTrabajoA(unHeroe, Guerrero)
//unHeroe = unHeroe.cambiarTrabajo(Guerrero)
//
//Si nos conviene que el trabajo y todos los items sean monadas o tener un objeto "Desempleado"
//
