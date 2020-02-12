# ArgumentParse

A halfway decent CLI type argument parser for Scala.


### Usage

```scala
val content = """!ban @user --reason="Being a jerk to everyone" --days=7"""

val days = Argument[Int](name = "days")
val reason = Argument[String](name = "reason", required = true)

val parsed = Parser.lenient(content, Set(days, reason))
val user = ...
// Do some checking to see if the reason exists, or use strict and check the Failure state
ban(user, parsed(reason), parsed.get(days).getOrElse(0))
```


From Java:
```java
        String content = "!ban @user --reason=\"Being a jerk to everyone\" --days=7";

        Argument<Integer> days = JavaApi.asInteger(new ArgumentBuider<>("days"));
        Argument<String> reason = JavaApi.asString(new ArgumentBuider<String>("reason").required(true));
        Set<Argument<?>> set = new HashSet<>();
        set.add(days);
        set.add(reason);
        ParsedArgument parsed = Parser.lenient(content, JavaApi.toScalaSet(set));
        System.out.println(parsed.apply(days));
        System.out.println(parsed.apply(reason));
        ban(user, parsed.apply(reason), parsed.getJava(days).orElse(0));
```


### Installation 
[![](https://jitpack.io/v/xaanit/ArgumentParse.svg)](https://jitpack.io/#xaanit/ArgumentParse)


```scala
resolvers ++= Seq("jitpack" at "https://jitpack.io")

libraryDependencies ++= Seq("com.github.xaanit" % "ArgumentParse" % "$VERSION$")

```
