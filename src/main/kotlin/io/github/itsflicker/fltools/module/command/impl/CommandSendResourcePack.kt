package io.github.itsflicker.fltools.module.command.impl

import io.github.itsflicker.fltools.api.NMS
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.onlinePlayers
import taboolib.common.platform.subCommand

/**
 * CommandSendResourcePack
 * io.github.itsflicker.fltools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/6 22:55
 */
object CommandSendResourcePack {

    val command = subCommand {
        dynamic {
            suggestion<ProxyCommandSender> { _, _ ->
                onlinePlayers().map { it.name }
            }
            execute<ProxyCommandSender> { _, _, argument ->
                NMS.INSTANCE.sendResourcePack(Bukkit.getPlayer(argument)!!)
            }
        }
    }
}