package it.xaan.ap

import scala.util.Try
import scala.util.matching.Regex

abstract class ArgumentType[+T](val validator: String => Boolean, val converter: String => T) {
  def find(str: String): String = {
    val idx = str.indexWhere(x => x == ' ' || x == '\n' || x == '\r')
    str.substring(0, if (idx == -1) str.length else idx)
  }

  def grab(str: String): Option[T] = {
    val found = find(str)
    if (validator(found)) Some(converter(found))
    else None
  }
}
private object ArgumentType {
  val StringRegex = """"(\\"|[^"])*[^\\]""""
}
case object CharArg extends ArgumentType[Char](x => x.length == 3, _ (1)) {
  override def find(str: String): String =
    if (str.length < 3) ""
    else str.substring(0, 3)
}
case object StringArg extends ArgumentType[String](x => x == "\"\"" || x.matches(ArgumentType.StringRegex), x => x.substring(0, x.length - 1)) {
  private val regex = new Regex(ArgumentType.StringRegex)

  override def find(str: String): String = regex.findFirstIn(str).getOrElse("")

  override def grab(str: String): Option[String] = {
    val x = find(str)
    Some(x.substring(1, x.length -1).replace("\\\"", "\""))
  }
}
case object BooleanArg extends ArgumentType[Boolean](x => x == "true" || x == "false", _.toBoolean)
case object DoubleArg extends ArgumentType[Double](x => Try(x.replace("\r\n", "").toDouble).isSuccess, _.toDouble)
case object LongArg extends ArgumentType[Long](x => Try(x.replace("\r\n", "").toLong).isSuccess, _.toLong)
case object FloatArg extends ArgumentType[Float](x => Try(x.replace("\r\n", "").toFloat).isSuccess, _.toFloat)
case object ByteArg extends ArgumentType[Byte](x => Try(x.replace("\r\n", "").toByte).isSuccess, _.toByte)
case object ShortArg extends ArgumentType[Short](x => Try(x.replace("\r\n", "").toShort).isSuccess, _.toShort)
case object IntArg extends ArgumentType[Int](x => Try(x.replace("\r\n", "").toInt).isSuccess, _.toInt)