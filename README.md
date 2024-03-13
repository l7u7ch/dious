```sbt
ThisBuild / scalaVersion := "3.4.0"
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.l7u7ch" % "dious" % "main-SNAPSHOT"
```

```scala
import com.github.l7u7ch.dious.*

@main def hello(): Unit = {
  val agent = Agent(service = "https://bsky.social/")

  agent.createSession(
    identifier = "<USER_ID>",
    password = "<APP_PASSWORD>"
  )
}
```

```scala
def createSession(identifier: String, password: String): String
def createRecord(msg: String): String
def getAuthorFeed(limit: Int = 50, cursor: String = ""): String
def deleteRecord(rkey: String): String
def deleteRepost(rkey: String): String
def deleteLike(rkey: String): String
def getActorLikes(cursor: String = ""): String
```
