package sd.util

import com.typesafe.config.ConfigException.WrongType
import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

object ConfigExt {
  implicit final class Implicits(val c: Config) extends AnyVal {
    import com.typesafe.config.ConfigValueType._
    def getStringListEx(path: String): Seq[String] = {
      val v = c.getValue(path)
      v.valueType() match {
        case LIST => v.unwrapped.asInstanceOf[java.util.List[String]].asScala
        case STRING =>
          val tmpCfg = ConfigFactory.parseString("root:" + v.unwrapped.asInstanceOf[String])
          tmpCfg.resolve().getStringList("root").asScala
        case x => throw new WrongType(v.origin(), path, "String_LIST | STRING", x.name)
      }
    }

    def getIntListEx(path: String): Seq[Int] = {
      val v = c.getValue(path)
      v.valueType() match {
        case LIST => v.unwrapped.asInstanceOf[java.util.List[Integer]].asScala.map(_.intValue)
        case STRING =>
          val tmpCfg = ConfigFactory.parseString("root:" + v.unwrapped.asInstanceOf[String])
          tmpCfg.resolve().getIntList("root").asScala.map(_.intValue)
        case x => throw new WrongType(v.origin(), path, "Int_LIST | STRING", x.name)
      }
    }
  }
}
