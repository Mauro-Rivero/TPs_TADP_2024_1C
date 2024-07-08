package domain

case class Taberna (var tablon: List[Mision] = List()){
  def elegirMisionPara(equipo: Equipo, criterio: Criterio): Option[Mision] = {
    val mejorMision = tablon.reduceLeft((unaMision, otraMision) => if (criterio(equipo.realizar(unaMision), equipo.realizar(otraMision))) unaMision else otraMision)
    Some(mejorMision).filter(equipo.puedeRealizar) //Si no puede realizar queda vacia la monada y devuelve None
  }

//  Entrenar: Cuando un equipo entrena, intenta realizar todas las misiones, una por una,
//  eligiendo la mejor misión para hacer a continuación. Cada misión se realiza luego de haber cobrado
//  la recompensa de la anterior y el equipo no se detiene hasta haber finalizado todas las misiones o
//  fallar una.
  def sacarMisionDeTablon(tablon: List[Mision], unaMision: Mision): List[Mision]{
    tablon.filterNot(_ == unaMision)
  }

  def entrenar(equipo:Equipo, criterio: Criterio, tablon: List[Mision] = this.tablon):(Equipo, List[Mision]) = { //agrego criterio pues dice que elige la mejor mision.. Segun qué?
    elegirMisionPara(equipo, criterio) match {
      case Some(mision) => entrenar(equipo.realizar(mision), criterio, this.sacarMisionDeTablon(mision))
      case None => (equipo, tablon)
    }
  }
}