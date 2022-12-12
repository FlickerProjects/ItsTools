package io.github.itsflicker.itstools.module.integration.placeholderapi

import io.github.itsflicker.itstools.module.feature.IPInfo
import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

/**
 * @author wlys
 * @since 2022/6/29 12:24
 */
@Suppress("unused")
object HookPlaceholderAPI : PlaceholderExpansion {

    override val identifier: String
        get() = "itstools"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player != null && player.isOnline) {
            val params = args.split('_')

            return when (params[0].lowercase()) {
                "resourcepack", "rp" -> {
                    if (params.size == 1) {
                        ResourcePack.selected[player.uniqueId]?.id.toString()
                    } else {
                        (ResourcePack.selected[player.uniqueId]?.id == params[1]).toString()
                    }
                }
                "ip" -> {
                    val default = params.getOrElse(2) { "" }
                    val info = IPInfo.caches[player.uniqueId] ?: return default
                    when (params.getOrElse(1) { "province" }.lowercase()) {
                        "country" -> info.country
                        "shortname" -> info.short_name
                        "province" -> info.province
                        "city" -> info.city
                        "area" -> info.area
                        "isp" -> info.isp
                        else -> "out of case"
                    }
                }
                else -> "out of case"
            }
        }

        return "ERROR"
    }

}