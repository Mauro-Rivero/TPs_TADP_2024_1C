package domain

case class Tarea(condicion: Option[Restriccion], modificacion: Modificacion, facilidad: Facilidad, condicionDeRealizacion: Option[CondicionDeRealizacion]){
}
