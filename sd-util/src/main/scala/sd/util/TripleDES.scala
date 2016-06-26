/** @author giabao
  * created: 2013-11-01 21:27
  * (c) 2011-2013 sandinh.com
  */
package sd.util

import javax.crypto.Cipher, Cipher.{ENCRYPT_MODE, DECRYPT_MODE}
import org.apache.commons.codec.digest.DigestUtils.md5Hex
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64.{encodeBase64String, decodeBase64}

/** dependencies: commons-codec */
object TripleDES {
  private def getCipher(key: String, mode: Int) = {
    val cipher = Cipher.getInstance("TripleDES")
    val keyMd5 = md5Hex(key).substring(0, 24)
    cipher.init(mode, new SecretKeySpec(keyMd5.getBytes, "TripleDES"))
    cipher
  }

  def encrypt(key: String, data: String): String = encodeBase64String(getCipher(key, ENCRYPT_MODE).doFinal(data.getBytes))

  def decrypt(key: String, data: String): String = new String(getCipher(key, DECRYPT_MODE).doFinal(decodeBase64(data)))
}
