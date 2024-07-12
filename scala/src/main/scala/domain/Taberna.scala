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
    elegirMisionPara(equipo, criterio). match {
      case Some(mision) => this.sacarMision(mision).entrenar(equipo.realizar(mision), criterio)
      case None => equipo
    }
  }
  def entrenar2(equipo: Equipo, criterio: Criterio): Equipo = { //agrego criterio pues dice que elige la mejor mision.. Segun qué?
    elegirMisionPara(equipo, criterio).map(mision => this.sacarMision(mision).entrenar2(equipo.realizar(mision), criterio)).getOrElse(equipo)
    }
}
