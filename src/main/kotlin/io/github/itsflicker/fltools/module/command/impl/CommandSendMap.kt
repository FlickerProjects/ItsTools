package io.github.itsflicker.fltools.module.command.impl

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.common.platform.function.onlinePlayers
import taboolib.module.nms.sendMap
import java.io.File
import java.net.URL

/**
 * @author wlys
 * @since 2022/6/3 20:20
 */
object CommandSendMap {

    val command = subCommand {
        literal("file") {
            dynamic("player") {
                suggestPlayers()
                dynamic("file") {
                    execute<CommandSender> { _, context, argument ->
                        val player = Bukkit.getPlayer(context.argument(-1)) ?: return@execute
                        player.sendMap(File(argument))
                    }
                }
            }
        }
        literal("url") {
            dynamic("player") {
                suggestion<ProxyCommandSender> { _, _ ->
                    onlinePlayers().map { it.name }
                }
                dynamic("url") {
                    execute<CommandSender> { _, context, argument ->
                        val player = Bukkit.getPlayer(context.argument(-1)) ?: return@execute
                        player.sendMap(URL(argument))
                    }
                }
            }
        }
    }
}