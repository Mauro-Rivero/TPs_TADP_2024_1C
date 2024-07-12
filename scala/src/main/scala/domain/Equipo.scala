package domain

case class Equipo(nombre: String, miembros: Set[Heroe] = Set(), pozoComun: Int = 0){

  def mejorHeroeSegun(criterio: Cuantificador): Option[Heroe] =  miembros.maxByOption(criterio) //mejorHeroeSegunOptional....
  def mejorHeroeSegunOptional(criterio: CuantificadorOptional): Option[Heroe] =  miembros.maxByOption(miembro => criterio(miembro).getOrElse(Int.MinValue))
  
  def obtenerItem(item : Item): Equipo = {
    val heroeACambiar = mejorHeroeSegun(heroe => heroe.equiparItem(item).statPrincipal() -  heroe.statPrincipal())
    if(heroeACambiar.exists(_.esPositivoEquipar(item))){
      reemplazarMiembro(heroeACambiar.get, heroeACambiar.get.equiparItem(item))
    }else{
      vender(item)
    }
  }
  def vender( item: Item): Equipo = this.copy(pozoComun = pozoComun + item.precio)

  def obtenerMiembro(heroe: Heroe): Equipo = this.copy(miembros = miembros + heroe)
  
  def eliminarMiembro(heroe: Heroe): Equipo = this.copy(miembros = miembros - heroe)

  def reemplazarMiembro(unMiembro: Heroe, otroMiembro: Heroe): Equipo = eliminarMiembro(unMiembro).obtenerMiembro(otroMiembro)
  
  def lider(): Option[Heroe] = {
    val mejor = mejorHeroeSegun(_.statPrincipal())
    mejor.filter { lider =>
      eliminarMiembro(lider).mejorHeroeSegun(_.statPrincipal()).exists(_.statPrincipal() != lider.statPrincipal())
    }
  }

  def realizar(mision: Mision): Equipo = {
    tratarDeRealizarTareas(mision.tareas) match {
      case None => this
      case Some(equipo) => mision.recompensa(equipo)
    }
  }

  def realizar2(mision: Mision): Equipo = {
    val result = tratarDeRealizarTareas(mision.tareas)
    if (result.nonEmpty) {
      mision.recompensa(result.get)
    } else {
      this
    }
  }
  def realizar3(mision: Mision): Equipo = {
    tratarDeRealizarTareas(mision.tareas).map(equipo => mision.recompensa(equipo)).getOrElse(this)
  }
  
  def puedeRealizar(mision: Mision): Boolean = tratarDeRealizarTareas(mision.tareas).isDefined
  
  private def tratarDeRealizarTareas(tareas: List[Tarea]): Option[Equipo] = {
    tareas.foldLeft(Option(this)) {
      case (None, _) => None
      case (Some(equipo), tarea) => equipo.tratarDeRealizarTarea(tarea)
    }
  }

  private def tratarDeRealizarTarea(tarea:Tarea): Option[Equipo] = {
    if(tarea.condicionDeRealizacion.exists(_(this))){ //ES OPTIONAL
      mejorHeroeSegunOptional(heroe => tarea.facilidad(heroe)).map { heroe =>
          reemplazarMiembro(heroe, heroe.realizar(tarea))
        }
    }else{
      None
    }
  }
}

