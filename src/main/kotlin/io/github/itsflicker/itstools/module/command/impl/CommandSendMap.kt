package io.github.itsflicker.itstools.module.command.impl

import io.github.itsflicker.itstools.util.allSymbol
import io.github.itsflicker.itstools.util.playerFor
import org.bukkit.command.CommandSender
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
                suggestPlayers(allSymbol)
                dynamic("file") {
                    execute<CommandSender> { _, context, argument ->
                        context.playerFor {
                            it.sendMap(File(argument))
                        }
                    }
                }
            }
        }
        literal("url") {
            dynamic("player") {
                suggestPlayers(allSymbol)
                dynamic("url") {
                    execute<CommandSender> { _, context, argument ->
                        context.playerFor {
                            it.sendMap(File(argument))
                        }
                    }
                }
            }
        }
    }

}