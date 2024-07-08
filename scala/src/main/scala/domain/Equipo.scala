package domain


case class Equipo(nombre: String, miembros: Set[Heroe] = Set(), pozoComun: Int = 0){

  def mejorHeroeSegun(criterio: Cuantificador): Option[Heroe] =  miembros.maxByOption(criterio) //mejorHeroeSegunOptional....
  def mejorHeroeSegunOptional(criterio: Cuantificador): Option[Heroe] =  miembros.maxByOption(criterio)

  def obtenerItem(item:Item): Equipo = {
    val heroeACambiar = mejorHeroeSegun(heroe => heroe.equiparItem(item).statPrincipal() -  heroe.statPrincipal()) // al no tener efecto sabemos que heroe es pero sin modificarlo
    heroeACambiar match {
      case Some(heroe) if heroe.esPositivoEquipar(item) =>  reemplazarMiembro(heroe, heroe.equiparItem(item))
      case _ => vender(item)
    }
  }
  /* OPCION DE CORRECCION FUNCION OBTENERITEM() DE ARRIBA
  def obtenerItem(item : Item): Equipo = {
    val heroeACambiar = mejorHeroeSegun(heroe => heroe.equiparItem(item).statPrincipal() -  heroe.statPrincipal())
    if(heroeACambiar.exists(heroe => heroe.esPositivoEquipar(item))){
      heroeACambiar.map(heroe => reemplazarMiembro(heroe, heroe.equiparItem(item))).getOrElse(this) //No deberia llegar nunca al getOrElse(this)
    }else{
      vender(item)
    }
  }
*/
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
/* OPCION DE CORRECCION FUNCION LIDER() DE ARRIBA
  def lider(): Option[Heroe] = {
    val mejor = mejorHeroeSegun(_.statPrincipal())
    mejor.flatMap { lider =>
      val segundo = eliminarMiembro(lider).mejorHeroeSegun(_.statPrincipal())
      if(segundo.exists(segundoLider => lider.statPrincipal() == segundoLider.statPrincipal())){
        None
      }else {
        Some(lider)
      }
    }
  }
*/
  def realizar(mision: Mision): Equipo = {
    tratarDeRealizarTareas(mision.tareas) match {
      case None => this
      case Some(equipo) => mision.recompensa(equipo)
    }
  }
 /* OPCION DE CORRECCION FUNCION realizar(mision: Mision) DE ARRIBA
  def realizar(mision: Mision): Equipo = {
    tratarDeRealizarTareas(mision.tareas).map(equipo => mision.recompensa(equipo)).getOrElse(this)
  }
  */
  def puedeRealizar(mision: Mision): Boolean = tratarDeRealizarTareas(mision.tareas).isDefined
  
  private def tratarDeRealizarTareas(tareas: List[Tarea]): Option[Equipo] = {
    tareas.foldLeft(Option(this)) {
      case (None, _) => None
      case (Some(equipo), tarea) => equipo.tratarDeRealizarTarea(tarea)
    }
  }

  private def tratarDeRealizarTarea(tarea:Tarea): Option[Equipo] = {
    if(tarea.condicionDeRealizacion(this)){ //ES OPTIONAL
      mejorHeroeSegun(heroe => tarea.facilidad(heroe)).map { heroe =>
          reemplazarMiembro(heroe, heroe.realizar(tarea))
        }
    }else{
      None
    }
  }
}

