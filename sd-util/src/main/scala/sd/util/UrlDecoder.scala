/** @author giabao
 *  created: 2013-10-24 18:25
 *  (c) 2011-2013 sandinh.com
 */
package sd.util

import java.net.{URLDecoder, URI}
import scala.collection.mutable

/** @see play.core.parsers.FormUrlEncodedParser
 *  @see org.apache.http.client.utils.URLEncodedUtils#parse(java.net.URI, java.lang.String)
 */
object UrlDecoder {
  def decode(uri: String, encoding: String = "utf-8"): Map[String, String] = {
    val query = URI.create(uri).getRawQuery
    var params = mutable.HashMap.empty[String, String]

    query.split('&').withFilter(_.contains('=')).foreach { param =>
      val parts = param.split('=')
      val key = URLDecoder.decode(parts.head, encoding)
      val value = URLDecoder.decode(parts.tail.headOption.getOrElse(""), encoding)
      params += key -> value
    }

    params.toMap
  }
}
