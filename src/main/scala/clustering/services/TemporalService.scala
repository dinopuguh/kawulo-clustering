package clustering.services

import database.Mongo._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Accumulators._
import org.mongodb.scala.model.Filters._
import helpers.Helpers._
import org.mongodb.scala.{Document, MongoCollection}

object TemporalService {
  def normalizeTemporal(): Unit = {
    val collection: MongoCollection[Document] = database.getCollection("temporal")

    val temporal = collection.aggregate(
      Seq(
        group(
          and(
            equal("restaurant_id", "$restaurant_id"),
            equal("month", "$month"),
            equal("year", "$year")
          ),
          first("location_id","$location_id"),
          first("restaurant_id","$restaurant_id"),
          first("month", "$month"),
          first("year", "$year"),
          push("service", "$service"),
          push("value", "$value"),
          push("food", "$food"),
          push("vader", "$vader"),
          push("wordnet", "$wordnet"),
          sum("count",1)
        ),
      )
    ).results()

    temporal.foreach {temporal =>
      val restaurantId = temporal("restaurant_id").asString().getValue
      val month = temporal("month").asInt32().getValue
      val year = temporal("year").asInt32().getValue

      println(s"${restaurantId} - ${month} - ${year}")
    }
  }

  def groupTemporalByLocation(): Seq[Document] = {
    val collection: MongoCollection[Document] = database.getCollection("temporal")

    collection.aggregate(
      Seq(
        group(
          and(
            equal("location_id", "$location_id"),
            equal("month", "$month"),
            equal("year", "$year")
          ),
          first("location_id", "$location_id"),
          first("location", "$location"),
          first("month", "$month"),
          first("year", "$year"),
          push("restaurant_id", "$restaurant_id"),
          push("restaurant", "$restaurant"),
          push("service", "$service"),
          push("value", "$value"),
          push("food", "$food"),
          push("vader", "$vader"),
          push("wordnet", "$wordnet"),
          sum("count", 1)
        ),
      )
    ).results()
  }
}
