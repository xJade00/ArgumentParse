package it.xaan.ap

import scala.util.{Failure, Success, Try}

sealed trait Parser {
  type Out

  def parse[R](content: String, arguments: Set[Argument[Any]]): Out
}
object Lenient extends Parser {
  type Out = ParsedArgument

  def parse[R](content: String, arguments: Set[Argument[Any]]): Out = Parser.internal(content, arguments, ignore = true).get
}
object Strict extends Parser {
  type Out = Try[ParsedArgument]

  def parse[R](content: String, arguments: Set[Argument[Any]]): Out = Parser.internal(content, arguments, ignore = true)
}
object Parser {

  def parse[Mode <: Parser](content: String, arguments: Set[Argument[_]])(implicit parser: Mode): parser.Out = parser.parse(content, arguments)

  private[ap] def internal(content: String, options: Set[Argument[Any]], ignore: Boolean): Try[ParsedArgument] = Try {
    val filtered = options.filter(x => content.contains(s"--${x.name}="))
    val required = options.filter(x => x.required)
    if (required.diff(filtered).nonEmpty) throw MissingArgumentsException(options.diff(filtered) ++ required.diff(filtered))
    val values = filtered.map(argument => {
      val name = s"--${argument.name}="
      val index = content.indexOf(name)
      val sub = content.substring(index + name.length)
      println(s"Sub for argument $argument: $sub")
      (argument, argument.argType.grab(sub))
    })
      .filter { case (_, opt) => opt.isDefined }
      .map { case (arg, opt) => (arg, arg.name, opt.get) }
      .filter { case (arg, _, value) => if (!ignore) true else arg.allowedValues.contains(value) }
    val failedValues = options.filter(x => values.exists(y => x.name == y._2))
    if (values.size != filtered.size && !ignore) throw FailedValidationException(options.diff(failedValues))
    val improper = values.find { case (arg, _, value) => arg.allowedValues.nonEmpty && !arg.allowedValues.contains(value) }
    improper match {
      case Some((arg, _, value)) => throw ImproperValue(arg, value)
      case None => new ParsedArgument(values.map { case (_, name, value) => (name, value) }.toMap)
    }
  } match {
    case Failure(_) if ignore => Success(new ParsedArgument(Map()))
    case x => x
  }

}
