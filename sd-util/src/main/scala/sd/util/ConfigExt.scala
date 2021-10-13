package sd.util

import com.typesafe.config.ConfigException.WrongType
import com.typesafe.config.{Config, ConfigFactory}
import java.util.{List => JList}
import scala.jdk.CollectionConverters._

/** dependencies: com.typesafe:config */
object ConfigExt {
  implicit final class Implicits(val c: Config) extends AnyVal {
    import com.typesafe.config.ConfigValueType._
    def getStringListEx(path: String): Seq[String] = {
      val v = c.getValue(path)
      v.valueType() match {
        case LIST => v.unwrapped.asInstanceOf[JList[String]].asScala.toSeq
        case STRING =>
          val tmpCfg = ConfigFactory.parseString(
            "root:" + v.unwrapped.asInstanceOf[String]
          )
          tmpCfg.resolve().getStringList("root").asScala.toSeq
        case x =>
          throw new WrongType(v.origin(), path, "String_LIST | STRING", x.name)
      }
    }

    def getIntListEx(path: String): Seq[Int] = {
      val v = c.getValue(path)
      v.valueType() match {
        case LIST =>
          v.unwrapped.asInstanceOf[JList[Integer]].asScala.map(_.intValue).toSeq
        case STRING =>
          val tmpCfg = ConfigFactory.parseString(
            "root:" + v.unwrapped.asInstanceOf[String]
          )
          tmpCfg.resolve().getIntList("root").asScala.map(_.intValue).toSeq
        case x =>
          throw new WrongType(v.origin(), path, "Int_LIST | STRING", x.name)
      }
    }
  }
}
