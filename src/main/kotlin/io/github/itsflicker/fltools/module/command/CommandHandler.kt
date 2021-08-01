package io.github.itsflicker.fltools.module.command

import io.github.itsflicker.fltools.module.command.impl.CommandLight
import io.github.itsflicker.fltools.module.command.impl.CommandLore
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.*
import taboolib.common.util.asList
import taboolib.module.nms.sendToast
import taboolib.module.nms.type.ToastFrame

/**
 * CommandHandler
 * io.github.itsflicker.fltools.module.command
 *
 * @author wlys
 * @since 2021/7/31 21:55
 */
@CommandHeader("fltools", ["ft"], "FlTools命令", permission = "fltools.access")
object CommandHandler {

    @CommandBody(permission = "fltools.command.light")
    val light = CommandLight

    @CommandBody(aliases = ["st"], permission = "fltools.command.sendtoast")
    val sendToast = subCommand {
        // player
        dynamic {
            suggestion<ProxyCommandSender> { _, _ ->
                onlinePlayers().map { it.name }
            }
            // toast frame
            dynamic {
                suggestion<ProxyCommandSender> { _, _ ->
                    ToastFrame.values().asList()
                }
                // material
                dynamic {
                    suggestion<ProxyCommandSender> { _, _ ->
                        Material.values().asList()
                    }
                    // message
                    dynamic {
                        execute<ProxyCommandSender> { _, context, argument ->
                            Bukkit.getPlayer(context.argument(-3))!!.sendToast(
                                Material.valueOf(context.argument(-1)),
                                argument,
                                ToastFrame.valueOf(context.argument(-2))
                            )
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "fltools.command.lore")
    val lore = CommandLore

}