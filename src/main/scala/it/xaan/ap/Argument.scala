package it.xaan.ap
/**
 * Represents a possible argument that can be in the message.
 *
 * @param name The name of the argument.
 * @param allowedValues Any allowed values.
 * @param required If the value is required.
 * @param argType The type of it.
 * @tparam T The type. For strict typing.
 */
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