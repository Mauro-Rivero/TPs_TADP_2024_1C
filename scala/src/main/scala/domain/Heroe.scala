package domain

import scala.util.Try

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

  def getFuerzaBase(): Int = statsBase.fuerza

  def getInteligenciaBase(): Int = statsBase.inteligencia

  def getVelocidadBase(): Int = statsBase.velocidad

  def getHpBase(): Int = statsBase.hp

  def getFuerza(): Int = getStats().fuerza

  def getInteligencia(): Int = getStats().inteligencia

  def getVelocidad(): Int = getStats().velocidad

  def getHp(): Int = getStats().hp

  def alterarFuerza(valor: Int): Heroe = this.copy(statsBase= statsBase.alterarFuerza(valor))
  
  def alterarInteligencia(valor: Int): Heroe = this.copy(statsBase= statsBase.alterarInteligencia(valor))
  
  def alterarHp(valor: Int): Heroe = this.copy(statsBase= statsBase.alterarHp(valor))
  
  def alterarVelocidad(valor: Int): Heroe = this.copy(statsBase= statsBase.alterarVelocidad(valor))

  def statPrincipal(): Int = trabajo.statPrincipal(this)

  def cambiarTrabajo(untraba: Trabajo): Heroe = this.copy(trabajo = untraba)

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
  
  def realizar(tarea: Tarea): Heroe = {     
    tarea.condicion.match {
        case Some(cond) if cond(this) => tarea.modificacion(this)
        case _ => this
      }
  }
}

//cambiarTrabajoA(unHeroe, Guerrero)
//unHeroe = unHeroe.cambiarTrabajo(Guerrero)
//
//Si nos conviene que el trabajo y todos los items sean monadas o tener un objeto "Desempleado"
//
