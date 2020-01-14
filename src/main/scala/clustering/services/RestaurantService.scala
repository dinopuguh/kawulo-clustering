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
import org.bson.types.ObjectId

object RestaurantService {
  def findByLocationId(location_id: ObjectId): Seq[Document] ={
    val collection:MongoCollection[Document] = database.getCollection("restaurant")

    collection.find(equal("location.$id",location_id)).results()
  }
}
