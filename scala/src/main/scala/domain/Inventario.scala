package domain

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
