package io.github.itsflicker.itstools.module.command.impl

import io.github.itsflicker.itstools.util.allSymbol
import io.github.itsflicker.itstools.util.playerFor
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
            suggestPlayers(allSymbol)
            dynamic("frame") {
                suggest {
                    ToastFrame.entries.map { it.toString() }
                }
                dynamic("material") {
                    suggest {
                        Material.entries.map { it.toString() }
                    }
                    dynamic("message") {
                        execute<ProxyCommandSender> { _, ctx, arg ->
                            ctx.playerFor {
                                it.sendToast(
                                    Material.valueOf(ctx["material"]),
                                    arg.colored(),
                                    ToastFrame.valueOf(ctx["frame"])
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}