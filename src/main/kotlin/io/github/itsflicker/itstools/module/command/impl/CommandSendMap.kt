package io.github.itsflicker.itstools.module.command.impl

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.playerFor
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.module.nms.sendMap
import java.io.File

/**
 * @author wlys
 * @since 2022/6/3 20:20
 */
object CommandSendMap {

    val command = subCommand {
        literal("file") {
            dynamic("player") {
                suggestPlayers(allSymbol = true)
                dynamic("file") {
                    execute<CommandSender> { _, context, argument ->
                        context.playerFor(-1) {
                            val player = it.cast<Player>()
                            player.sendMap(File(argument))
                        }
                    }
                }
            }
        }
        literal("url") {
            dynamic("player") {
                suggestPlayers(allSymbol = true)
                dynamic("url") {
                    execute<CommandSender> { _, context, argument ->
                        context.playerFor(-1) {
                            val player = it.cast<Player>()
                            player.sendMap(File(argument))
                        }
                    }
                }
            }
        }
    }

}