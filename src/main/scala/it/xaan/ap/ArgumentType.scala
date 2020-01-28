package it.xaan.ap

/**
 * Defines a type that can be parsed from a string.
 *
 * @tparam T The type to parse to.
 */
trait Parseable[+T] {
  def read(s: String): T
}

/**
 * An object representing a type of argument.
 *
 * @param validator Returns true if the string can be turned into the specified type.
 * @param converter Turns the string into the specified type.
 * @tparam T The type
 */
abstract class ArgumentType[+T](val validator: String => Boolean, val converter: Parseable[T]) {
  /**
   * Finds the string that will be validated and possibly converted.
   *
   * @param str The full string to extract from
   * @return The found string. If no string is found this should be `""`
   */
  def find(str: String): String = {
    val idx = str.indexWhere(x => x == ' ' || x == '\n' || x == '\r')
    str.substring(0, if (idx == -1) str.length else idx)
  }

  /**
   * Converts the found string to the specified type. This generally shouldn't be overwritten
   *
   * @param str The full string to grab from.
   * @return None if it can't be converted. otherwise Some with the value.
   */
  final def grab(str: String): Option[T] = {
    val found = find(str)
    if (validator(found)) Some(converter.read(found))
    else None
  }
}
private[ap] object ArgumentType {
  val StringRegex = """"(\\"|[^"])*[^\\]""""
}
