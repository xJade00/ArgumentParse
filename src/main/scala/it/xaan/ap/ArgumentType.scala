package it.xaan.ap

import scala.util.Try
import scala.util.matching.Regex

/**
 * An object representing a type of argument.
 *
 * @param validator Returns true if the string can be turned into the specified type.
 * @param converter Turns the string into the specified type.
 * @tparam T The type
 */
abstract class ArgumentType[+T](val validator: String => Boolean, val converter: String => T) {
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
   * Converts the found string to the specified type.
   *
   * @param str The full string to grab from.
   * @return None if it can't be converted. otherwise Some with the value.
   */
  def grab(str: String): Option[T] = {
    val found = find(str)
    if (validator(found)) Some(converter(found))
    else None
  }
}
private object ArgumentType {
  val StringRegex = """"(\\"|[^"])*[^\\]""""
}
/**
 * An argument type representing characters.
 */
case object CharArg extends ArgumentType[Char](x => x.length == 3, _ (1)) {
  override def find(str: String): String =
    if (str.length < 3) ""
    else str.substring(0, 3)
}
/**
 * An argument type representing Strings.
 */
case object StringArg extends ArgumentType[String](x => x == "\"\"" || x.matches(ArgumentType.StringRegex), x => x.substring(0, x.length - 1)) {
  private val regex = new Regex(ArgumentType.StringRegex)

  override def find(str: String): String = regex.findFirstIn(str).getOrElse("")

  override def grab(str: String): Option[String] = {
    val x = find(str)
    Some(x.substring(1, x.length -1).replace("\\\"", "\""))
  }
}
/**
 * An argument type representing booleans.
 */
case object BooleanArg extends ArgumentType[Boolean](x => x == "true" || x == "false", _.toBoolean)
/**
 * An argument type representing doubles.
 */
case object DoubleArg extends ArgumentType[Double](x => Try(x.replace("\r\n", "").toDouble).isSuccess, _.toDouble)
/**
 * An argument type representing longs.
 */
case object LongArg extends ArgumentType[Long](x => Try(x.replace("\r\n", "").toLong).isSuccess, _.toLong)
/**
 * An argument type representing floats.
 */
case object FloatArg extends ArgumentType[Float](x => Try(x.replace("\r\n", "").toFloat).isSuccess, _.toFloat)
/**
 * An argument type representing bytes.
 */
case object ByteArg extends ArgumentType[Byte](x => Try(x.replace("\r\n", "").toByte).isSuccess, _.toByte)
/**
 * An argument type representing shorts.
 */
case object ShortArg extends ArgumentType[Short](x => Try(x.replace("\r\n", "").toShort).isSuccess, _.toShort)
/**
 * An argument type representing integers.
 */
case object IntArg extends ArgumentType[Int](x => Try(x.replace("\r\n", "").toInt).isSuccess, _.toInt)