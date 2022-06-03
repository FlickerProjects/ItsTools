package io.github.itsflicker.fltools.module.command.impl

import org.bukkit.Bukkit
import org.bukkit.Material
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.module.chat.colored
import taboolib.module.nms.sendToast
import taboolib.module.nms.type.ToastFrame

/**
 * CommandSendToast
 * io.github.itsflicker.fltools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/4 23:21
 */
object CommandSendToast {

    val command = subCommand {
        // player
        dynamic("player") {
            suggestion<ProxyCommandSender> { _, _ ->
                onlinePlayers().map { it.name }
            }
            // toast frame
            dynamic("frame") {
                suggestion<ProxyCommandSender> { _, _ ->
                    ToastFrame.values().map { it.toString() }
                }
                // material
                dynamic("material") {
                    suggestion<ProxyCommandSender> { _, _ ->
                        Material.values().map { it.toString() }
                    }
                    // message
                    dynamic("message") {
                        execute<ProxyCommandSender> { _, context, argument ->
                            val player = Bukkit.getPlayer(context.argument(-3))
                            player ?: kotlin.run {
                                return@execute
                            }
                            player.sendToast(
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