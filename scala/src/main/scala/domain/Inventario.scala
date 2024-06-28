package domain

case class Inventario(cabeza: Option[Item], torso : Option[Item] = None, manos: List[Item] = List(), talismanes: List[Item] = List()) {
  
  def equipar(item: Item): Inventario = {
    item match {
      case Cabeza(_, _) => this.copy(cabeza = Some(item))
      case Torso(_, _) => this.copy(torso = Some(item))
      case Talisman(_, _) => this.copy(talismanes = talismanes.::(item))
      case Mano(_, _, DosManos) => this.copy(manos = List(item))
      case Mano(_, _, UnaMano) =>
        this.copy(manos = if (manos.contains((x: Mano) => x.tipo == DosManos)) {
          List(item)
        } else if (manos.size == 2) {
          manos.take(1).::(item)
        } else {
          manos.::(item)
        })
    }
  }
    
    def stats(heroe:Heroe): Stats = {
      List(cabeza, torso, manos, talismanes).flatten.map(_.modificacion(heroe)).fold(heroe.statsBase)(_+_)
    }
}
