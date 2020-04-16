package database

import org.mongodb.scala.{MongoClient, MongoDatabase}

object Mongo {
  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27018")

  val database: MongoDatabase = mongoClient.getDatabase("kawulo")
}
