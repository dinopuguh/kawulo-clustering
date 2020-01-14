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

object LocationService {
  def findAllLocations(): Seq[Document] ={
    val collection: MongoCollection[Document] = database.getCollection("location")

    collection.find().results()
  }

  def findIndonesianLocations():Seq[Document]={
    val cities = Array(
      "Banda Aceh",
      "Medan",
      "Padang",
      "Pekanbaru",
      "Palembang",
      "Bengkulu",
      "Bandar Lampung",
      "Pangkal Pinang",
      "Tanjung Pinang",
      "Jakarta",
      "Bandung",
      "Semarang",
      "Yogyakarta Region",
      "Surabaya",
      "Serang",
      "Denpasar",
      "Mataram",
      "Kupang",
      "Pontianak",
      "Banjarmasin",
      "Samarinda",
      "Manado",
      "Palu",
      "Makassar",
      "Kendari",
      "Gorontalo",
      "Mamuju",
      "Ambon",
      "Jayapura",
      "Manokwari"
    )

    val collection:MongoCollection[Document] = database.getCollection("location")

    var locations = Seq[Document]()

    cities.foreach(city => {
      val location:Document = collection.find(equal("name",city)).headResult()
      locations = locations :+ location
    })

    locations
  }
}
