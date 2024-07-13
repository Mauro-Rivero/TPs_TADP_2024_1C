/////////////
/// ALIAS///
////////////
package domain

type StatPrincipal = Heroe => Int

val Fuerza: StatPrincipal       = _.getFuerza()

val Hp: StatPrincipal           = _.getHp()

val Velocidad: StatPrincipal    = _.getVelocidad()

val Inteligencia: StatPrincipal = _.getInteligencia()

type Restriccion = Heroe => Boolean

type CondicionDeRealizacion = Equipo => Boolean

type Modificacion = Heroe => Heroe

type Cuantificador = Heroe => Int

type CuantificadorOptional = Heroe => Option[Int]

type Criterio = (Equipo, Equipo) => Boolean

type Facilidad = Heroe => Option[Int]  // OPTON[Int]

type Recompensa = Equipo => Equipo

////////////
/// HEROE //
////////////

case class Heroe (var statsBase: Stats, var inventario: Inventario, var trabajo: Trabajo){
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
    if (tarea.condicion.exists(_(this))) {
      tarea.modificacion(this)
    } else {
      this
    }
  }
}
/////////////
/// EQUIPO //
/////////////

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
////////////////
/// INVENTARI //
////////////////

case class Inventario(cabeza: Option[Item], torso : Option[Item] = None, manos: List[Item] = List(), talismanes: List[Item] = List()) {

  def equipar(item: Item): Inventario = {
    item.tipo match {
      case _: Cabeza => this.copy(cabeza = Some(item))
      case _: Torso => this.copy(torso = Some(item))
      case Talisman => this.copy(talismanes = talismanes.::(item))
      case DosManos => this.copy(manos = List(item))
      case UnaMano =>
        this.copy(manos = if (manos.count((x: Item) => x.tipo == UnaMano) < 2) {
          manos.::(item)
        } else {
          manos.take(1).::(item)
        })
    }
  }

  def stats(heroe:Heroe): Stats = {
    List(cabeza, torso, manos, talismanes).flatten.map(_(heroe)).foldLeft(heroe.statsBase)((x,y) => x + y.statsBase)
  }
}
///////////
/// ITEM //
///////////

case class Item( modificacion: Modificacion, restriccion: Restriccion, tipo: Tipo, precio: Int = 0) {

  def apply(heroe:Heroe): Heroe = {
    modificacion(heroe)
  }
}

sealed trait Tipo

sealed trait Cabeza extends Tipo

case object Casco extends Cabeza

case object Sombrero extends Cabeza

sealed trait Torso extends Tipo

case object Armadura extends Torso

case object Vestido extends Torso

case object Talisman extends Tipo

sealed trait Arma extends Tipo

sealed trait Escudo extends Tipo

case object UnaMano extends Arma with Escudo

case object DosManos extends Arma


def fuerzaBaseMayorA(valor: Int): Restriccion = { (heroe: Heroe) => heroe.statsBase.fuerza > valor }

val palitoMagico: Item = Item(_.alterarInteligencia(20), _.trabajo == Mago, UnaMano:Arma)

val cascoVikingo: Item = Item(_.alterarHp(10), _.getFuerzaBase()>30, Casco:Cabeza)

/////////////
/// TAREA //
////////////

case class Tarea(condicion: Option[Restriccion], modificacion: Modificacion, facilidad: Facilidad, condicionDeRealizacion: Option[CondicionDeRealizacion]){
}
//////////////
/// TRABAJO //
//////////////

class Trabajo(val stats: Stats, val statPrincipal: StatPrincipal) {
}

object Guerrero extends Trabajo( Stats(hp = 10, fuerza = 15, inteligencia = -10), Fuerza)
val Mago = new Trabajo(new Stats(fuerza = -20, inteligencia = 20), Inteligencia)
val Ladron = new Trabajo(new Stats(hp = -5, velocidad = 10), Velocidad)

/////////////
/// MISION //
/////////////

case class Mision(tareas: List[Tarea] = List(), recompensa: Recompensa) {

}
/////////////
/// STATS //
////////////

case class Stats(val hp: Int = 0, val velocidad: Int = 0, val fuerza: Int = 0, val inteligencia: Int = 0){

  def +(otrasStats: Stats): Stats = Stats(
    hp = hp + otrasStats.hp,
    velocidad= velocidad + otrasStats.velocidad,
    fuerza= fuerza + otrasStats.fuerza,
    inteligencia= inteligencia + otrasStats.inteligencia
  )

  def alterarFuerza(valor: Int): Stats = this.copy(fuerza = fuerza + valor).toValid

  def alterarInteligencia(valor: Int): Stats = this.copy(inteligencia = inteligencia + valor).toValid

  def alterarHp(valor: Int): Stats = this.copy(hp = hp + valor).toValid

  def alterarVelocidad(valor: Int): Stats = this.copy(velocidad = velocidad + valor).toValid


  def toValid: Stats = Stats(
    hp = Math.max(hp,1),
    velocidad= Math.max(velocidad,1),
    fuerza= Math.max(fuerza,1),
    inteligencia= Math.max(inteligencia,1)
  )
}
//////////////
/// TABERNA //
//////////////


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
    elegirMisionPara(equipo, criterio).map(mision => this.sacarMision(mision).entrenar(equipo.realizar(mision), criterio)).getOrElse(equipo)
  }
}

