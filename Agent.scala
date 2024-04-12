package com.github.l7u7ch.dious

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import sttp.client4.quick.*

case class Agent(private val service: String) {
  private var accessJwt: String = ""
  private var handle: String = ""
  private var did: String = ""

  // https://www.docs.bsky.app/docs/api/com-atproto-server-create-session
  def createSession(identifier: String, password: String): String = {
    case class Payload(
        identifier: String,
        password: String
    )

    val payload: String =
      Payload(identifier = identifier, password = password).asJson.toString

    val response: String = quickRequest
      .post(uri"${service}/xrpc/com.atproto.server.createSession")
      .header("Content-Type", "application/json")
      .header("Accept", "application/json")
      .body(payload)
      .send()
      .body

    val cursor: HCursor = parse(response).getOrElse(Json.Null).hcursor

    accessJwt = cursor.get[String]("accessJwt").getOrElse("")
    handle = cursor.get[String]("handle").getOrElse("")
    did = cursor.get[String]("did").getOrElse("")

    response
  }

  // https://www.docs.bsky.app/docs/api/com-atproto-repo-create-record
  def createRecord(msg: String): String = {
    case class Payload(
        repo: String,
        collection: String,
        record: Map[String, String]
    )

    val payload: String = Payload(
      repo = handle,
      collection = "app.bsky.feed.post",
      record = Map(
        "text" -> msg,
        "createdAt" -> java.time.Instant.now().toString
      )
    ).asJson.toString

    val response: String = quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.createRecord")
      .header("Content-Type", "application/json")
      .header("Accept", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(payload)
      .send()
      .body

    response
  }

  // https://www.docs.bsky.app/docs/api/com-atproto-repo-delete-record
  def deleteRecord(rkey: String): String = {
    case class Payload(
        repo: String,
        collection: String,
        rkey: String
    )

    val payload: String = Payload(
      repo = handle,
      collection = "app.bsky.feed.post",
      rkey = rkey
    ).asJson.toString

    val response: String = quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.deleteRecord")
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(payload)
      .send()
      .body

    response
  }

  // https://www.docs.bsky.app/docs/api/com-atproto-repo-delete-record
  def deleteRepost(rkey: String): String = {
    case class Payload(
        repo: String,
        collection: String,
        rkey: String
    )

    val payload: String = Payload(
      repo = handle,
      collection = "app.bsky.feed.repost",
      rkey = rkey
    ).asJson.toString

    val response: String = quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.deleteRecord")
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(payload)
      .send()
      .body

    response
  }

  // https://www.docs.bsky.app/docs/api/com-atproto-repo-delete-record
  def deleteLike(rkey: String): String = {
    case class Payload(
        repo: String,
        collection: String,
        rkey: String
    )

    val payload: String = Payload(
      repo = handle,
      collection = "app.bsky.feed.like",
      rkey = rkey
    ).asJson.toString

    val response: String = quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.deleteRecord")
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(payload)
      .send()
      .body

    response
  }

  // https://www.docs.bsky.app/docs/api/app-bsky-feed-get-author-feed
  def getAuthorFeed(limit: Int = 50, cursor: String = ""): String = {
    val response: String = quickRequest
      .get(
        uri"${service}/xrpc/app.bsky.feed.getAuthorFeed?actor=${handle}&limit=${limit}&cursor=${cursor}"
      )
      .header("Accept", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .send()
      .body

    response
  }

  // https://docs.bsky.app/docs/api/app-bsky-feed-get-actor-likes
  def getActorLikes(limit: Int = 50, cursor: String = ""): String = {
    quickRequest
      .get(
        uri"${service}/xrpc/app.bsky.feed.getActorLikes?actor=${handle}&limit=${limit}&cursor=${cursor}"
      )
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .send()
      .body
  }
}
