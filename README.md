Bluesky が提供している API を Scala で扱うライブラリ

JitPack で公開しています

## 1. インストール

### 1.1. Gradle

```groovy
// build.gradle
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.l7u7ch:dious:main-SNAPSHOT'
}
```

### 1.2. Maven

```xml
<!-- pom.xml -->
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.l7u7ch</groupId>
  <artifactId>dious</artifactId>
  <version>main-SNAPSHOT</version>
</dependency>
```

### 1.3. sbt

```sbt
// build.sbt
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.l7u7ch" % "dious" % "main-SNAPSHOT"
```

## 2. サンプルコード

```scala
import com.github.l7u7ch.dious.*

@main def Main(): Unit = {
  val agent = Agent(service = "https://bsky.social/")

  agent.createSession(
    identifier = "<USER_ID>",
    password = "<APP_PASSWORD>"
  )

  agent.createRecord("Hello, World!!")
}
```

## 3. 実装メソッド

```scala
// com.atproto.server.*
def createSession(identifier: String, password: String): String

// com.atproto.repo.*
def createRecord(msg: String): String
def deleteRecord(rkey: String): String
def getRecord(collection: String = "app.bsky.feed.post", rkey: String): String
def listRecords(collection: String = "app.bsky.feed.post", limit: Int = 50): String

// 非推奨
def deleteLike(rkey: String): String
def deleteRepost(rkey: String): String
def getActorLikes(limit: Int = 50, cursor: String = ""): String
def getAuthorFeed(limit: Int = 50, cursor: String = ""): String
```
