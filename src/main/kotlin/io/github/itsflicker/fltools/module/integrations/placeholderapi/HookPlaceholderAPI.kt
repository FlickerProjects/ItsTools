package io.github.itsflicker.fltools.module.integrations.placeholderapi

import io.github.itsflicker.fltools.module.resourcepack.ResourcePack
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

/**
 * @author wlys
 * @since 2022/6/29 12:24
 */
object HookPlaceholderAPI : PlaceholderExpansion {

    override val identifier: String
        get() = "fltools"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player != null && player.isOnline) {
            val params = args.split('_')

            return when (params[0].lowercase()) {
                "rp" -> {
                    if (params.size == 1) {
                        ResourcePack.selected[player.uniqueId]?.id.toString()
                    } else {
                        (ResourcePack.selected[player.uniqueId]?.id == params[1]).toString()
                    }
                }
                else -> "out of case"
            }
        }

        return "ERROR"
    }

}