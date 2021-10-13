package sd.util

import java.util.Collections
import java.util.{Map => JMap}

/** http://stackoverflow.com/a/7201825/457612 */
object EnvHack {
  def setEnv(newEnv: Map[String, String]): Unit = {
    try {
      val processEnvironmentClass =
        Class.forName("java.lang.ProcessEnvironment")
      val theEnvironmentField =
        processEnvironmentClass.getDeclaredField("theEnvironment")
      theEnvironmentField.setAccessible(true)
      val env = theEnvironmentField.get(null).asInstanceOf[JMap[String, String]]
      newEnv.foreach { case (k, v) => env.put(k, v) }
      val theCaseInsensitiveEnvironmentField =
        processEnvironmentClass.getDeclaredField(
          "theCaseInsensitiveEnvironment"
        )
      theCaseInsensitiveEnvironmentField.setAccessible(true)
      val cienv = theCaseInsensitiveEnvironmentField
        .get(null)
        .asInstanceOf[JMap[String, String]]
      newEnv.foreach { case (k, v) => cienv.put(k, v) }
    } catch {
      case e: NoSuchFieldException =>
        try {
          val env = System.getenv()
          for (cl <- classOf[Collections].getDeclaredClasses) {
            if ("java.util.Collections$UnmodifiableMap" == cl.getName) {
              val field = cl.getDeclaredField("m")
              field.setAccessible(true)
              val map = field.get(env).asInstanceOf[JMap[String, String]]
              map.clear()
              newEnv.foreach { case (k, v) => map.put(k, v) }
            }
          }
        } catch {
          case e2: Exception => e2.printStackTrace()
        }
      case e: Exception => e.printStackTrace()
    }
  }
}
