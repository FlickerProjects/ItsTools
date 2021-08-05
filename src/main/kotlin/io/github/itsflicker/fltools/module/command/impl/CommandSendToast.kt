package io.github.itsflicker.fltools.module.command.impl

import org.bukkit.Bukkit
import org.bukkit.Material
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.onlinePlayers
import taboolib.common.platform.subCommand
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
        dynamic {
            suggestion<ProxyCommandSender> { _, _ ->
                onlinePlayers().map { it.name }
            }
            // toast frame
            dynamic {
                suggestion<ProxyCommandSender> { _, _ ->
                    ToastFrame.values().map { it.toString() }
                }
                // material
                dynamic {
                    suggestion<ProxyCommandSender> { _, _ ->
                        Material.values().map { it.toString() }
                    }
                    // message
                    dynamic {
                        execute<ProxyCommandSender> { _, context, argument ->
                            val player = Bukkit.getPlayer(context.argument(-3)!!)
                            player ?: kotlin.run {
                                return@execute
                            }
                            player.sendToast(
                                Material.valueOf(context.argument(-1)!!),
                                argument.colored(),
                                ToastFrame.valueOf(context.argument(-2)!!)
                            )
                        }
                    }
                }
            }
        }
    }
}