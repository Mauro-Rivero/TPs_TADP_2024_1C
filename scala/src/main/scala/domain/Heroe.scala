package domain

abstract class Heroe (hp: Int, fuerza: Int, velocidad: Int, inteligencia: Int ){
  val inventario : List[Item] = List()
  var trabajo : Trabajo
  var casco : Casco
  var manoIzquierda: Arma
  var manoDerecha: Arma
  var Manos : Arma
//var Armas : List[Arma] = List() alternativa

  def saludar() : Unit
   println("hola")

  //dameFuerza :: Armas -> [Armadura] -> Heroe -> Heroe
  //dameFuerza armas armaduras heroe =  foldr (modificarStats) heroe (armaduras : armas)

  //aplicarArmamento :: Heroe -> Heroe

  calcularStat :: Heroe -> Heroe
  calcualrStat heroe = normalizarStat . aplicarArmamento $ heroe

  normalizarStat
  normalizarStat  heroe = max (1, stat heroe)


}
