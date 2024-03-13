```sbt
ThisBuild / scalaVersion := "3.4.0"
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.l7u7ch" % "studious-disco" % "main-SNAPSHOT"
```

```scala
import com.github.l7u7ch.xxx.*

@main def hello(): Unit = {
  val agent = Agent(service = "https://bsky.social/")

  agent.createSession(
    identifier = "<USER_ID>",
    password = "<APP_PASSWORD>"
  )
}
```
