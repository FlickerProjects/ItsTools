package io.github.itsflicker.itstools.module.command.impl

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.module.nms.createLight
import taboolib.module.nms.deleteLight
import taboolib.module.nms.type.LightType

/**
 * CommandLight
 * io.github.itsflicker.itstools.module.command.impl
 *
 * @author wlys
 * @since 2021/7/31 21:59
 */
object CommandLight {

    val command = subCommand {
        literal("create") {
            dynamic("type") {
                suggestion<Player> { _, _ ->
                    listOf("SKY", "BLOCK", "BOTH")
                }
                dynamic("level") {
                    suggestion<Player> { _, _ ->
                        (1..15).map { it.toString() }
                    }
                    execute<Player> { sender, ctx, arg ->
                        sender.getTargetBlock(null, 50).createLight(
                            arg.toInt(),
                            when(ctx["type"]) {
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
            dynamic("type") {
                suggestion<Player> { _, _ ->
                    listOf("SKY", "BLOCK", "BOTH")
                }
                execute<Player> { sender, _, arg ->
                    sender.getTargetBlock(null, 50).deleteLight(
                        when(arg) {
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