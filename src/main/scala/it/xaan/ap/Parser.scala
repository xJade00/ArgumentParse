package it.xaan.ap

import scala.util.{Failure, Success, Try}

object Parser {
  def parseIgnored(content: String, options: Set[Argument[_]]): ParsedArgument = parse(content, options, ignore = true).get

  def parseFailure(content: String, options: Set[Argument[_]]): Try[ParsedArgument] = parse(content, options, ignore = false)


  private def parse(content: String, options: Set[Argument[_]], ignore: Boolean): Try[ParsedArgument] = Try {
    val filtered = options.filter(x => content.contains(s"--${x.name}="))
    val required = options.filter(x => x.required)
    if (required.diff(filtered).nonEmpty && !ignore) throw MissingArgumentsException(options.diff(filtered))
    val values = filtered.map(argument => {
      val name = s"--${argument.name}="
      val index = content.indexOf(name)
      val sub = content.substring(index + name.length)
      (argument, argument.argType.grab(sub))
    })
      .filter { case (_, opt) => opt.isDefined }
      .map { case (arg, opt) => (arg.name, opt.get) }
    val failedValues = options.filter(x => values.exists(y => x.name == y._1))
    if (values.size != filtered.size && !ignore) throw FailedValidationException(options.diff(failedValues))
    new ParsedArgument(values.toMap)
  } match {
    case Failure(_) if ignore => Success(new ParsedArgument(Map()))
    case x => x
  }

}
