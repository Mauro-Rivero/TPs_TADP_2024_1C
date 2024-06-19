package domain

class Stats(val hp: Int = 0, val velocidad: Int = 0, val fuerza: Int = 0, val inteligencia: Int = 0){
  
  def +(otrasStats: Stats): Stats = {
    new Stats(
      hp = hp + otrasStats.hp,
      velocidad= velocidad + otrasStats.velocidad,
      fuerza= fuerza + otrasStats.fuerza,
      inteligencia= inteligencia + otrasStats.inteligencia
    )
  }
  
  def toValid(): Stats = {
    new Stats(
      hp = Math.max(hp,1),
      velocidad= Math.max(velocidad,1),
      fuerza= Math.max(fuerza,1),
      inteligencia= Math.max(inteligencia,1)
    )
  }
}
