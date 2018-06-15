package com.sword.util


import com.google.gson.{JsonParser, Gson}
import org.apache.commons.lang3.StringUtils
object GsonUtils extends Logging {

  val gSon = new Gson()
  val parser = new JsonParser

  def toJson[T](t: T): String = {
    gSon.toJson(t)
  }

  /**
   * 将json型的字符串解析成指定的对象，若失败，则返回null
   */
  def parseJson[T](msg: String, clazz: Class[T]): T = {
    if (StringUtils.isEmpty(msg)) {
      return null.asInstanceOf[T]
    }
    try {
      val obj = gSon.fromJson(msg, clazz)
      return obj
    } catch {
      case e: Exception => warn("parse msg '" + msg + "' to class '" + clazz.getName + "' error")
    }
    null.asInstanceOf[T]
  }

}
