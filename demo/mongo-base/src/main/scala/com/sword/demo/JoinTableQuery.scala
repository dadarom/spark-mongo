package com.sword.demo

import com.sword.domain.{Score, Student, SparkPlus}
import com.sword.util.MongoHelper

/**
 * Created by triones on 18-6-13.
 */
class JoinTableQuery extends SparkPlus{

  def query(){
    val db = "test"
    val collection = "spark"
    val scoreDB = "scoreDB"
    val scoreCollection = "score"

    val session = createSparkSession()
    val mongoReader : MongoHelper = new MongoHelper(mongoUri,session)

    mongoReader.read[Student](db,collection).createOrReplaceTempView("student")
    mongoReader.read[Score](scoreDB,scoreCollection).createOrReplaceTempView("score")
    session.sql("select student.name from student,score where student.id = score.studentId").show(100)
  }
}

object JoinTableQuery{
  def main(args: Array[String]) {
    val process = new JoinTableQuery()
    if (args.length > 0 && ("local[*]" == args(0))) {
      process.masterUrl = args(0)
      process.query()
    }
  }
}
