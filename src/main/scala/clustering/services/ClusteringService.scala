package clustering.services

import org.mongodb.scala.Document

import helpers.Helpers._
import org.mongodb.scala.model.Filters._
import database.Mongo.database

object ClusteringService {
  def clusterIsExist(location_id: String, month: Int, year: Int): Boolean = {
    val collection = database.getCollection("cluster")

    val count = collection.find(
      and(
        equal("location_id",location_id),
        equal("month",month),
        equal("year",year),
      )
    ).results().size

    if(count > 0) {
      println("Cluster already exist\n")

      true
    } else {
      false
    }
  }

  def saveCluster(document: Document): Unit ={
    val collection = database.getCollection("cluster")

    collection.insertOne(document).results()

    println(s"successfully save cluster of ${document("restaurant_id").asString().getValue}.")
  }
}
