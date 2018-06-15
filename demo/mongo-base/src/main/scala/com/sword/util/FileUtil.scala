package com.sword.util

import java.io._
import java.nio.charset.Charset
import java.util.Properties

import scala.collection.mutable.ListBuffer

object FileUtil extends Logging {

  def fileContain(file: String, content : String) : Int = {
    val lines = FileUtil.read(file)
    var result = 0
    for (line <- lines) {
      if (line.contains(content)) {
        result+=1
      }
    }
    result
  }

  def loadConf(confFile: String): Map[String, String] = {
    val properties = load(confFile)
    val conf  = scala.collection.mutable.Map[String, String]()

    val iter = properties.keySet().iterator()
    while (iter.hasNext){
      try {
        val key = iter.next().asInstanceOf[String];
        val value = properties.get(key).asInstanceOf[String]
        conf.put(key,value)
      }
      catch {
        case e: Exception => {
          //          LOG.error("error while parse spark conf file", e)
        }
      }
    }
    conf.toMap
  }

  def load(file: String): Properties = {
    val properties = new Properties()
    var resourceAsStream: InputStream = null

    try {
      resourceAsStream = FileUtil.getClass.getClassLoader.getResourceAsStream(file)
      properties.load(resourceAsStream)
      properties
    } catch {
      case e: Exception =>
        error("Failed to load file " + file, e)
        properties
    } finally {
      try {
        if (resourceAsStream != null) {
          resourceAsStream.close()
        } else {
        }
      } catch {
        case e: Exception => logger.error("Failed to close resource stream");
      }
    }
  }

  def read(file: String): List[String] = {

    val contents = new ListBuffer[String]()
    var line: String = null
    var fis: InputStream = null
    var isr: InputStreamReader = null
    var br: BufferedReader = null
    try {
      try {
        info("try to read file from class path first " + file)
        fis = FileUtil.getClass.getClassLoader.getResourceAsStream(file)
        isr = new InputStreamReader(fis, Charset.forName("UTF-8"))
      } catch {
        case e: Exception => {
          warn("failed to read file from class path so try to read form local path")
          fis = new FileInputStream(file)
          isr = new InputStreamReader(fis, Charset.forName("UTF-8"))
        }
      }
      br = new BufferedReader(isr)
      while ( {
        line = br.readLine
        line
      } != null) {
        contents += line
      }
    }
    catch {
      case e: IOException => {
        error("error while read file "+ file +" , with exception",e)
      }
    }
    finally {
      if (br != null) {
        try {
          br.close
        }
        catch {
          case e: IOException => {
            error("error while read file "+ file +" , with exception",e)
          }
        }
      }
      if (isr != null) {
        try {
          isr.close
        }
        catch {
          case e: IOException => {
            error("error while read file "+ file +" , with exception",e)
          }
        }
      }
      if (fis != null) {
        try {
          fis.close
        }
        catch {
          case e: IOException => {
            error("error while read file "+ file +" , with exception",e)
          }
        }
      }
    }
    return contents.toList
  }


  def append(file: String, content: String) = {
    var outputFile: File = null
    var fileWriter: FileWriter = null
    var bufferedWriter: BufferedWriter = null

    outputFile = new File(file)

    if (!outputFile.exists()) {
      outputFile.createNewFile()
    }

    try {
      fileWriter = new FileWriter(outputFile, true)
      bufferedWriter = new BufferedWriter(fileWriter)
      bufferedWriter.write(content + System.getProperty("line.separator"))
    }
    catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
    finally {
      if (bufferedWriter != null) {
        try {
          bufferedWriter.close
        }
        catch {
          case e: IOException => {
            e.printStackTrace
          }
        }
      }
      if (fileWriter != null) {
        try {
          fileWriter.close
        }
        catch {
          case e: IOException => {
            e.printStackTrace
          }
        }
      }
    }
  }

  def read(inputStream : InputStream) : java.util.List[String] = {
    val contents = new java.util.ArrayList[String]()
    var line: String = null
    var isr: InputStreamReader = null
    var br: BufferedReader = null
    try {
      try {
        isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"))
      } catch {
        case e: Exception => {
          warn("failed to read file from class path so try to read form local path")
        }
      }
      br = new BufferedReader(isr)
      while ( {
        line = br.readLine
        line
      } != null) {
        contents.add(line)
      }
    }
    catch {
      case e: IOException => {
        error("error while read input stream with exception",e)
      }
    }
    finally {
      if (br != null) {
        try {
          br.close
        }
        catch {
          case e: IOException => {
            error("error while read input stream with exception",e)
          }
        }
      }
      if (isr != null) {
        try {
          isr.close
        }
        catch {
          case e: IOException => {
            error("error while read input stream with exception",e)
          }
        }
      }
    }
    return contents
  }


}
