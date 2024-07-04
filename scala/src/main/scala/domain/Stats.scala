package domain

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
