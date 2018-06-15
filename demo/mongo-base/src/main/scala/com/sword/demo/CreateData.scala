package com.sword.demo


import com.sword.domain.{Score, Student, SparkPlus}
import com.sword.util.{GsonUtils, MongoHelper, FileUtil}
import org.bson.Document

/**
 * Created by triones on 18-6-12.
 */

class CreateData extends SparkPlus{

  def create(){
    val db = "test"
    val collection = "spark"
    val scoreDB = "scoreDB"
    val scoreCollection = "score"

    val session = createSparkSession()
    val mongoReader : MongoHelper = new MongoHelper(mongoUri,session)

    mongoReader.drop(db,collection)
    mongoReader.drop(scoreDB,scoreCollection)

    val students = FileUtil.read("student.properties").map(s => GsonUtils.parseJson(s,classOf[Student])).toSeq
    val SDF = session.createDataFrame(students)
    mongoReader.save(SDF,db,collection)


    val scores = FileUtil.read("score.properties").map(s => GsonUtils.parseJson(s,classOf[Score])).toSeq
    val scoreDF = session.createDataFrame(scores)
    mongoReader.save(scoreDF,scoreDB,scoreCollection)

    mongoReader.read[Student](db,collection).show(100)
    mongoReader.read[Score](scoreDB,scoreCollection).show(100)
  }
}
object CreateData{

  def main(args: Array[String]) {
    val process = new CreateData()
    if (args.length > 0 && ("local[*]" == args(0))) {
      process.masterUrl = args(0)
      process.create()
    }
  }


}
