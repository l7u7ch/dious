package com.github.l7u7ch.xxx

import sttp.client4.quick.*
import upickle.default.*

case class Agent(val service: String) {
  var accessJwt: String = ""
  var handle: String = ""
  var did: String = ""

  // https://www.docs.bsky.app/docs/api/com-atproto-server-create-session
  def createSession(identifier: String, password: String): String = {
    val map: Map[String, String] =
      Map("identifier" -> identifier, "password" -> password)

    val json = upickle.default.write(map)

    val response: String = quickRequest
      .post(uri"${service}/xrpc/com.atproto.server.createSession")
      .header("Content-Type", "application/json")
      .header("Accept", "application/json")
      .body(json)
      .send()
      .body

    accessJwt = ujson.read(response)("accessJwt").str
    handle = ujson.read(response)("handle").str
    did = ujson.read(response)("did").str

    response
  }

  // https://www.docs.bsky.app/docs/api/com-atproto-repo-create-record
  def createRecord(msg: String): String = {
    case class PetOwner(
        repo: String,
        collection: String,
        record: Map[String, String]
    ) derives ReadWriter

    val petOwner = PetOwner(
      handle,
      "app.bsky.feed.post",
      Map(
        "text" -> msg,
        "createdAt" -> java.time.Instant.now().toString
      )
    )

    val json: String = write(petOwner)

    quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.createRecord")
      .header("Content-Type", "application/json")
      .header("Accept", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(json)
      .send()
      .body
  }

  // https://www.docs.bsky.app/docs/api/app-bsky-feed-get-author-feed
  def getAuthorFeed(limit: Int = 50, cursor: String = ""): String = {
    quickRequest
      .get(
        uri"${service}/xrpc/app.bsky.feed.getAuthorFeed?actor=${handle}&limit=${limit}&cursor=${cursor}"
      )
      .header("Accept", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .send()
      .body
  }

  // https://www.docs.bsky.app/docs/api/com-atproto-repo-delete-record
  def deleteRecord(rkey: String): String = {
    val map: Map[String, String] =
      Map(
        "repo" -> handle,
        "collection" -> "app.bsky.feed.post",
        "rkey" -> rkey
      )

    val json = upickle.default.write(map)

    quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.deleteRecord")
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(json)
      .send()
      .body
  }

  //
  def deleteRepost(rkey: String): String = {
    val map: Map[String, String] =
      Map(
        "repo" -> handle,
        "collection" -> "app.bsky.feed.repost",
        "rkey" -> rkey
      )

    val json = upickle.default.write(map)

    quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.deleteRecord")
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(json)
      .send()
      .body
  }

  //
  def deleteLike(rkey: String): String = {
    val map: Map[String, String] =
      Map(
        "repo" -> handle,
        "collection" -> "app.bsky.feed.like",
        "rkey" -> rkey
      )

    val json = upickle.default.write(map)

    quickRequest
      .post(uri"${service}/xrpc/com.atproto.repo.deleteRecord")
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .body(json)
      .send()
      .body
  }

  // https://docs.bsky.app/docs/api/app-bsky-feed-get-actor-likes
  def getActorLikes(cursor: String = ""): String = {
    quickRequest
      .get(
        uri"${service}/xrpc/app.bsky.feed.getActorLikes?actor=${handle}&cursor=${cursor}"
      )
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + accessJwt)
      .send()
      .body
  }
}
