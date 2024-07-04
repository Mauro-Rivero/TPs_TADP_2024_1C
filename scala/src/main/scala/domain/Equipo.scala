package domain

case class Equipo(nombre: String, miembros: Set[Heroe] = Set(), pozoComun: Int = 0){

  def mejorHeroeSegun(criterio: Cuantificador): Option[Heroe] =  miembros.maxByOption(criterio)

  def obtenerItem(item:Item): Equipo = {
    val heroeACambiar = mejorHeroeSegun(heroe => heroe.equiparItem(item).statPrincipal() -  heroe.statPrincipal()) // al no tener efecto sabemos que heroe es pero sin modificarlo
    heroeACambiar match {
      case Some(heroe) if heroe.esPositivoEquipar(item) =>  reemplazarMiembro(heroe, heroe.equiparItem(item))
      case _ => vender(item)
    }
  }

  def vender( item: Item): Equipo = this.copy(pozoComun = pozoComun + item.precio)

  def obtenerMiembro(heroe: Heroe): Equipo = this.copy(miembros = miembros + heroe)
  
  def eliminarMiembro(heroe: Heroe): Equipo = this.copy(miembros = miembros - heroe)

  def reemplazarMiembro(unMiembro: Heroe, otroMiembro: Heroe): Equipo = eliminarMiembro(unMiembro).obtenerMiembro(otroMiembro)

  def lider(): Option[Heroe] = {
    val mejor = mejorHeroeSegun(_.statPrincipal())
    mejor.flatMap { lider =>
      val segundo = eliminarMiembro(lider).mejorHeroeSegun(_.statPrincipal())
      segundo match {
        case Some(segundoLider) if lider.statPrincipal() == segundoLider.statPrincipal() => None
        case _ => Some(lider)
      }
    }
  }
}

