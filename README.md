# ArgumentParse

A halfway decent CLI type argument parser for Scala.


### Usage

```scala
val content = """!ban @user --reason="Being a jerk to everyone" --days=7"""

val options = Set(
                Argument(name = reason, required = true, argType = StringArg),
                Argument(name = days, argType = IntArg)
              )

val parsed = Parser.parse(content, options)(Lenient)

val days = parsed[Int]("days")
val reason = parsed[String]("reason")
val user = ...

ban(user, reason, days)
```


### Installation 
[![](https://jitpack.io/v/xaanit/ArgumentParse.svg)](https://jitpack.io/#xaanit/ArgumentParse)


```scala
resolvers ++= Seq("jitpack" at "https://jitpack.io")

libraryDependencies ++= Seq("com.github.xaanit" % "ArgumentParse" % "$VERSION$")

```
