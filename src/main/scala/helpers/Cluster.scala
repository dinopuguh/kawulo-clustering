package helpers

import scala.math.{pow, sqrt}

object Cluster {
  def countDiff(points: Array[Double], points2: Array[Double]): Double ={
    var total = 0.0

    points.zipWithIndex.foreach{case(point,i) =>
      total += pow((point-points2(i)),2)
    }

    sqrt(total)
  }

  def getNewCluster(centroids: Array[Array[Double]]): Array[Int] ={
    var temp = Array[Double]()

    centroids.zipWithIndex.foreach{case(centroid,i) =>
      temp = temp :+ countDiff(centroid, new Array[Double](5))
    }

    val sortedTemp = temp.zipWithIndex.sorted

    var newClusters = Array[Int]()

    sortedTemp.indices.foreach{i =>
      newClusters = newClusters :+ sortedTemp(i)._2
    }

    newClusters
  }
}
