package domain

case class Taberna(tablon: List[Mision]) extends App{

  def elegirMisionPara(equipo: Equipo, criterio: Criterio): Option[Mision] = {
    val mejorMision = tablon.reduceLeft((unaMision, otraMision) => 
      if (criterio(equipo.realizar(unaMision), equipo.realizar(otraMision))) unaMision else otraMision)
    Some(mejorMision).filter(equipo.puedeRealizar) //Si no puede realizar queda vacia la monada y devuelve None
  }

  def sacarMision(unaMision: Mision): Taberna = {
    this.copy(tablon = tablon.filterNot(_ == unaMision))
  }

  def entrenar(equipo: Equipo, criterio: Criterio): Equipo = { //agrego criterio pues dice que elige la mejor mision.. Segun qué?
    elegirMisionPara(equipo, criterio) match {
      case Some(mision) => this.sacarMision(mision).entrenar(equipo.realizar(mision), criterio)
      case None => equipo
    }
  }

//  def entrenar(equipo:Equipo):Equipo = {
//  ???
//  }
//
//  
//  equipo = entrenar(equipo)
}
//Entrenar: Cuando un equipo entrena, intenta realizar todas las misiones, una por una,
//eligiendo la mejor misión para hacer a continuación. Cada misión se realiza luego de haber cobrado
//  la recompensa de la anterior y el equipo no se detiene hasta haber finalizado todas las misiones o
//  fallar una.
