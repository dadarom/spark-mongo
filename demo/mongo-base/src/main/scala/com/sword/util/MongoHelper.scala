package com.sword.util
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession, Dataset}
import com.mongodb.spark.{MongoConnector, MongoSpark}
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import org.bson.Document
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
 * Created by triones on 18-6-12.
 */
class MongoHelper(uri : String, sparkSession : SparkSession) {

  def read[T <: Product: TypeTag](database: String, collection : String) : DataFrame = {
    MongoSpark.load[T](sparkSession, ReadConfig(buildConfig(database,collection)))
  }

  def save(data : Dataset[_],database: String, collection : String) = {
    MongoSpark.save(data.write.option("collection", collection).option("database",database).option("uri",uri))
  }

  def save[D: ClassTag](rdd: RDD[D],database: String, collection : String) = {
     val writeConfig  = WriteConfig(buildConfig(database,collection))
     MongoSpark.save(rdd,writeConfig)
  }

  def drop(database :String,collection : String){
    val readConfig =  ReadConfig(buildConfig(database,collection))
    MongoConnector(readConfig).withDatabaseDo(readConfig, db => db.drop())
  }

//  def buildRDD[T <: Product: TypeTag](dataFile : String) : RDD[T] =  {
//    val students = FileUtil.read("student.properties").toSeq
//    sparkSession.createDataFrame(students)
//  }

  def buildConfig(database :String,collection : String) : Map[String,String] = {
    Map("collection" -> collection,"database" -> database,"uri"->uri)
  }

}