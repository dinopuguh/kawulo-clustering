package clustering.services

import database.Mongo._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Accumulators._
import org.mongodb.scala.model.Filters._
import helpers.Helpers._
import org.mongodb.scala.{Document, MongoCollection}

object TemporalService {
  def groupTemporal(): Seq[Document] = {
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
    ).allowDiskUse(true).results()
  }

  def groupTemporalByLocation(locationId: String): Seq[Document] = {
    val collection: MongoCollection[Document] = database.getCollection("temporal")

    collection.aggregate(
      Seq(
        filter(
          equal("location_id", locationId)
        ),
        group(
          and(
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
    ).allowDiskUse(true).results()
  }
}
