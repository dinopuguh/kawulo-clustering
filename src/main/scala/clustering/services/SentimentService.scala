package clustering.services

import scala.collection.immutable.IndexedSeq

import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._

import database.Mongo._
import helpers.Helpers._

object SentimentService {
  def findAll(): Seq[Document] ={
    val collection:MongoCollection[Document] = database.getCollection("sentiment")

    collection.find().results()
  }}
