package clustering

import util.control.Breaks._
import ALI._

import org.mongodb.scala.Document
import services.LocationService._
import services.TemporalService.groupTemporalByLocation
import services.ClusteringService.{saveCluster, clusterIsExist}
import helpers.Normalization.zScore
import helpers.Cluster.{countDiff, getNewCluster}


object Main extends App {
  val ncluster = 3

  val clusteringLib = new ClusteringLib()
  val locations = findIndonesianLocations()
  val temporals = groupTemporalByLocation()

  temporals.foreach { temporal =>
    breakable {
      val locationId = temporal("location_id").asString().getValue
      val location = temporal("location").asDocument()
      val month = temporal("month").asInt32().getValue
      val year = temporal("year").asInt32().getValue
      val count = temporal("count").asInt32().getValue
      val restaurantIds = temporal("restaurant_id").asArray().getValues
      val restaurants = temporal("restaurant").asArray().getValues

      println(s"${locationId} - ${month} - ${year}")
      if (clusterIsExist(locationId, month, year)) {
        break()
      }

      val services = temporal("service").asArray().getValues
      val values = temporal("value").asArray().getValues
      val foods = temporal("food").asArray().getValues
      val vaders = temporal("vader").asArray().getValues
      val wordnets = temporal("wordnet").asArray().getValues

      var data = Array[Array[Double]]()

      if (count < 3) {
        println("Cannot cluster this location (n < 3)\n")
        break()
      }

      var newService = Array[Double]()
      var newValue = Array[Double]()
      var newFood = Array[Double]()
      var newVader = Array[Double]()
      var newWordnet = Array[Double]()

      (0 until count) foreach { i: Int =>
        newService = newService :+ services.get(i).asDouble().getValue
        newValue = newValue :+ values.get(i).asDouble().getValue
        newFood = newFood :+ foods.get(i).asDouble().getValue
        newVader = newVader :+ vaders.get(i).asDouble().getValue
        newWordnet = newWordnet :+ wordnets.get(i).asDouble().getValue
      }

      (0 until count) foreach { i: Int =>
        var temp = Array[Double]()

        val service = zScore(newService, newService(i))
        val value = zScore(newValue, newValue(i))
        val food = zScore(newFood, newFood(i))
        val vader = zScore(newVader, newVader(i))
        val wordnet = zScore(newWordnet, newWordnet(i))

        temp = temp :+ service
        temp = temp :+ value
        temp = temp :+ food
        temp = temp :+ vader
        temp = temp :+ wordnet

        data = data :+ temp
      }

      val hierarchicalCluster = clusteringLib.HierarchicalKmeans(data, ncluster)
      val centroids = clusteringLib.getCentroid(data, hierarchicalCluster)

      val variance = clusteringLib.getVariance(data, hierarchicalCluster)
      val sse = clusteringLib.getSSE(data, hierarchicalCluster)

      if(variance(0).isNaN) {
        variance(0) = 0
      }

      println(variance(0), variance(1), variance(0) / variance(1))

      val newClusters = getNewCluster(centroids)

      hierarchicalCluster.zipWithIndex foreach { case (cluster, i) =>
        val document = Document(
          "location_id" -> locationId,
          "location" -> location,
          "restaurant_id" -> restaurantIds.get(i).asString().getValue,
          "restaurant" -> restaurants.get(i).asDocument(),
          "month" -> month,
          "year" -> year,
          "new_cluster" -> newClusters(cluster),
          "cluster" -> cluster,
          "service" -> data(i)(0),
          "value" -> data(i)(1),
          "food" -> data(i)(2),
          "vader" -> data(i)(3),
          "wordnet" -> data(i)(4),
          "variance" -> (variance(0) / variance(1)),
          "sse" -> sse
        )

        saveCluster(document)
      }

      println("\n")
    }
  }
}