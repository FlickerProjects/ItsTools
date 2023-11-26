package io.github.itsflicker.itstools.module.command.impl

import org.bukkit.entity.Player
import taboolib.common.platform.command.int
import taboolib.common.platform.command.subCommand
import taboolib.common.util.addSafely
import taboolib.module.chat.colored
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyLore

/**
 * CommandLore
 * io.github.itsflicker.itstools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/1 11:44
 */
@Suppress("Deprecation")
object CommandLore {

    // it lore append/insert/pop <...>
    val command = subCommand {
        literal("append") {
            dynamic("text") {
                execute<Player> { sender, _, arg ->
                    if (sender.itemInHand.isAir()) {
                        return@execute
                    }
                    sender.itemInHand.modifyLore {
                        add(arg.colored())
                    }
                }
            }
        }
        literal("insert") {
            int("line") {
                suggestion<Player>(uncheck = true) { sender, _ ->
                    (1..(sender.itemInHand.itemMeta?.lore?.size ?: 1)).map { it.toString() }
                }
                dynamic("text") {
                    execute<Player> { sender, ctx, arg ->
                        if (sender.itemInHand.isAir()) {
                            return@execute
                        }
                        sender.itemInHand.modifyLore {
                            addSafely(ctx.int("line") - 1, arg.colored(), "")
                        }
                    }
                }
            }
        }
        literal("pop") {
            int("line", optional = true) {
                execute<Player> { sender, _, arg ->
                    if (sender.itemInHand.isAir()) {
                        return@execute
                    }
                    sender.itemInHand.modifyLore {
                        try {
                            sender.sendMessage("Removed lore: ${this.removeAt(arg.toInt()-1)}")
                        }
                        catch (e: IndexOutOfBoundsException) {
                            sender.sendMessage("该物品第${arg}行没有Lore!")
                        }
                    }
                }
            }
            execute<Player> { sender, _, _ ->
                if (sender.itemInHand.isAir()) {
                    return@execute
                }
                sender.itemInHand.modifyLore {
                    if (isEmpty()) return@modifyLore
                    this.removeLast()
                }
            }
        }
    }
}