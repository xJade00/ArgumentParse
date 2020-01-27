package it.xaan.ap

class ParsingException(message: String) extends RuntimeException(s"There was an error parsing: $message")
case class MissingArgumentsException(arguments: Set[Argument[Any]]) extends ParsingException(s"Couldn't find arguments: $arguments")
case class FailedValidationException(failed: Set[Argument[Any]]) extends ParsingException(s"Couldn't parse arguments: $failed")
case class ImproperValue(argument: Argument[Any], value: Any) extends ParsingException(s"$value is not a proper value for $argument")
