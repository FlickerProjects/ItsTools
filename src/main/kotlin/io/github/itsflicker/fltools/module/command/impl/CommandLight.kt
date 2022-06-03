package io.github.itsflicker.fltools.module.command.impl

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.module.nms.createLight
import taboolib.module.nms.deleteLight
import taboolib.module.nms.type.LightType

/**
 * CommandLight
 * io.github.itsflicker.fltools.module.command.impl
 *
 * @author wlys
 * @since 2021/7/31 21:59
 */
object CommandLight {

    val command = subCommand {
        literal("create") {
            // type
            dynamic("type") {
                suggestion<Player> { _, _ ->
                    listOf("SKY", "BLOCK", "BOTH")
                }
                // level
                dynamic("level") {
                    suggestion<Player> { _, _ ->
                        (1..15).map { it.toString() }
                    }
                    execute<Player> { sender, context, argument ->
                        sender.getTargetBlock(null, 50).createLight(
                            argument.toInt(),
                            when(context.argument(-1)) {
                                "SKY" -> LightType.SKY
                                "BLOCK" -> LightType.BLOCK
                                "BOTH" -> LightType.ALL
                                else -> LightType.ALL
                            }
                        )
                    }
                }
            }
        }
        literal("delete") {
            // type
            dynamic("type") {
                suggestion<Player> { _, _ ->
                    listOf("SKY", "BLOCK", "BOTH")
                }
                execute<Player> { sender, _, argument ->
                    sender.getTargetBlock(null, 50).deleteLight(
                        when(argument) {
                            "SKY" -> LightType.SKY
                            "BLOCK" -> LightType.BLOCK
                            "BOTH" -> LightType.ALL
                            else -> LightType.ALL
                        }
                    )
                }
            }
        }
    }
}