package sd.util

import javax.inject.{Inject, Singleton}
import com.typesafe.config.Config
import sd.util.ConfigExt.Implicits

/** dependencies: com.typesafe:config, javax.inject */
@Singleton
final class SDAuth @Inject() (config: Config) {
  private[this] val mods = config.getIntListEx("sd.mods")
  private[this] val admins = config.getIntListEx("sd.admins")

  @inline def isMod(uid: Int): Boolean = mods.contains(uid)
  @inline def isAdmin(uid: Int): Boolean = admins.contains(uid)
}