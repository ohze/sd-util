package sd.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter.printHexBinary

object HmacSHA1 {
  def signHmacSHA1hex(message: String, secret: String): String = {
    val key = secret.getBytes("UTF-8")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(new SecretKeySpec(key, "HmacSHA1"))
    printHexBinary(mac.doFinal(message.getBytes("utf-8")))
  }
}
