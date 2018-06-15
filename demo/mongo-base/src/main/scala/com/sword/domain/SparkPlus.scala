package com.sword.domain

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * Created by triones on 18-6-12.
 */
abstract class SparkPlus {

  // spark
  var sparkConfFile = "spark.properties"
  var masterUrl: String = null

  // app
  var appConfFile = "config.properties"
  var config: Map[String, String] = null
  var mongoUri: String = null

  def init() = {
    config = com.sword.util.FileUtil.loadConf(this.appConfFile)
    mongoUri = config.get("mongo").get
  }

  init()

  def createSparkSession() : SparkSession = {
    val conf = createSparkConf()
    createSparkSession(conf)
  }

  def createSparkSession(sparkConf : SparkConf) : SparkSession = {
    SparkSession.builder().config(sparkConf).getOrCreate()
  }

  def setSparkConf(sparkConf: SparkConf) {
    val confs = com.sword.util.FileUtil.loadConf(this.sparkConfFile)
    confs.foreach((e:(String,String)) => sparkConf.set(e._1,e._2))

    if (masterUrl != null) {
      sparkConf.setMaster(masterUrl)
    }
  }

  def createSparkConf(): SparkConf = {
    val sparkConf: SparkConf = new SparkConf()
    this.setSparkConf(sparkConf)
    sparkConf
  }

  def getLocalConfig(key: String): String = {
    config.get(key).get
  }
}
