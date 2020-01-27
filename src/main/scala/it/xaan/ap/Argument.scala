package it.xaan.ap

case class Argument[+T](
                        name: String,
                        allowedValues: Seq[T] = Nil,
                        required: Boolean = false,
                        argType: ArgumentType[T]
                      ) {
  override def equals(obj: Any): Boolean = obj match {
    case other: Argument[T] => name == other.name
    case _ => false
  }
}