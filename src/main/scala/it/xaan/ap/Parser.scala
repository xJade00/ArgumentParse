package it.xaan.ap

import it.xaan.ap.ArgumentType.UnitArg

import scala.util.Try

object Parser {

  def strict(content: String, arguments: Set[Argument[_]]): Try[ParsedArgument] = parse(content, arguments, lenient = false)

  def lenient(content: String, arguments: Set[Argument[_]]): ParsedArgument = parse(content, arguments, lenient = true).getOrElse(new ParsedArgument(Map()))



  private[ap] def parse(content: String, options: Set[Argument[Any]], lenient: Boolean): Try[ParsedArgument] = Try {
    val filtered = options.filter(x => content.contains(s"--${x.name}${if (x.argType != UnitArg && x.argType != JavaApi.UnitArg) "=" else ""}"))
    val required = options.filter(x => x.required)
    if (required.diff(filtered).nonEmpty) throw MissingArgumentsException(options.diff(filtered) ++ required.diff(filtered))
    val values = filtered.map(argument => {
      val name = s"--${argument.name}="
      val index = content.indexOf(name)
      val sub = content.substring(index + name.length)
      (argument, argument.argType.grab(sub))
    })
      .filter { case (_, opt) => opt.isDefined }
      .map { case (arg, opt) => (arg, arg.name, opt.get) }
      .filter { case (arg, _, value) => if (!lenient || arg.argType == UnitArg || arg.allowedValues.isEmpty) true else arg.allowedValues.contains(value) }
    val failedValues = options.filter(x => values.exists(y => x.name == y._2))
    if (values.size != filtered.size && !lenient) throw FailedValidationException(options.diff(failedValues))
    val improper = values.find { case (arg, _, value) => arg.allowedValues.nonEmpty && !arg.allowedValues.contains(value) }
    improper match {
      case Some((arg, _, value)) => throw ImproperValue(arg, value)
      case None => new ParsedArgument(values.map { case (_, name, value) => (name, value) }.toMap)
    }
  }

}
