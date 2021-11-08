package sd.util

import org.apache.commons.codec.binary.Hex.encodeHex
import org.apache.commons.codec.digest.HmacUtils

object HmacSHA1 {
  @deprecated("Use HmacUtils from commons-codec directly", "1.2.1")
  def signHmacSHA1hex(message: String, secret: String): String = {
    val hmac = HmacUtils.hmacSha1(secret, message)
    new String(encodeHex(hmac, false))
  }
}
