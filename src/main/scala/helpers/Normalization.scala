package helpers

import scala.math.{sqrt, pow}
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics

object Normalization {
  def minMax(data:Double, min:Double, max:Double, new_min: Double, new_max: Double):Double = {
    var newData = (data - min) * (new_max - new_min) / (max - min) + new_min

    if(newData.isNaN)
      newData = max

    newData
  }



  def zScore(data: Array[Double], value: Double): Double = {
    val statistics = new DescriptiveStatistics(data)

    val mean = statistics.getMean
    val stdDev = statistics.getStandardDeviation


    if(stdDev == 0) {
      return 0.0
    }

    (value-mean) / stdDev
  }

}
