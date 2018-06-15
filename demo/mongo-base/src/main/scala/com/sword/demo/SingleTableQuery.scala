package com.sword.demo

import com.sword.domain.{SparkPlus, Student}
import com.sword.util.MongoHelper

/**
 * Created by triones on 18-6-13.
 */
class SingleTableQuery extends SparkPlus{

  def query(){
    val db = "test"
    val collection = "spark"

    val session = createSparkSession()
    val mongoReader : MongoHelper = new MongoHelper(mongoUri,session)

    val characters = mongoReader.read[Student](db,collection)
    characters.createOrReplaceTempView(collection)

    val centenarians = session.sql(s"SELECT *  FROM $collection")
    centenarians.show()
  }

}

object SingleTableQuery{
  def main(args: Array[String]) {
    val process = new SingleTableQuery()
    if (args.length > 0 && ("local[*]" == args(0))) {
      process.masterUrl = args(0)
      process.query()
    }
  }
}
