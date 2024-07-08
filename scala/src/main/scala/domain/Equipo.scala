package domain

import scala.util.{Failure, Success, Try}

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

  def realizar(mision: Mision): Equipo = {
    tratarDeRealizarTareas(mision.tareas) match {
      case None => this
      case Some(equipo) => mision.recompensa(equipo)
    }
  }
  
  def puedeRealizar(mision: Mision): Boolean = {
    tratarDeRealizarTareas(mision.tareas) match {
      case None => false
      case Some(_) => true
    }
  }
  
  private def tratarDeRealizarTareas(tareas: List[Tarea]): Option[Equipo] = {
    tareas.foldLeft(Option(this)) {
      case (None, _) => None
      case (Some(equipo), tarea) => equipo.tratarDeRealizarTarea(tarea)
    }
  }

  private def tratarDeRealizarTarea(tarea:Tarea): Option[Equipo] = {
    if(tarea.condicionDeRealizacion(this)){ //Interpretamos que la condicion de las tareas es sobre el equipo
      mejorHeroeSegun(heroe => tarea.facilidad(heroe)).map { heroe =>
          reemplazarMiembro(heroe, heroe.realizar(tarea))
        }
    }else{
      None
    }
  }
}

