package io.github.itsflicker.itstools.module.command.impl

import org.bukkit.Bukkit
import org.bukkit.Material
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.command.suggestPlayers
import taboolib.module.chat.colored
import taboolib.module.nms.sendToast
import taboolib.module.nms.type.ToastFrame

/**
 * CommandSendToast
 * io.github.itsflicker.itstools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/4 23:21
 */
object CommandSendToast {

    val command = subCommand {
        dynamic("player") {
            suggestPlayers()
            dynamic("frame") {
                suggest {
                    ToastFrame.values().map { it.toString() }
                }
                dynamic("material") {
                    suggest {
                        Material.values().map { it.toString() }
                    }
                    dynamic("message") {
                        execute<ProxyCommandSender> { _, context, argument ->
                            val player = Bukkit.getPlayer(context.argument(-3))
                            player?.sendToast(
                                Material.valueOf(context.argument(-1)),
                                argument.colored(),
                                ToastFrame.valueOf(context.argument(-2))
                            )
                        }
                    }
                }
            }
        }
    }
}