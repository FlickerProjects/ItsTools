package io.github.itsflicker.fltools.module.command.impl

import org.bukkit.entity.Player
import taboolib.common.platform.CommandBody
import taboolib.common.platform.subCommand
import taboolib.common.util.addSafely
import taboolib.common5.Coerce
import taboolib.module.chat.colored
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import taboolib.platform.util.modifyLore

/**
 * CommandLore
 * io.github.itsflicker.fltools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/1 11:44
 */
object CommandLore {

    // fltools lore append/insert/pop <...>
    val command = subCommand {
        literal("append") {
            // text
            dynamic {
                execute<Player> { sender, _, argument ->
                    if (sender.itemInHand.isAir()) {
                        return@execute
                    }
                    sender.itemInHand.modifyLore {
                        add(argument.colored())
                    }
                }
            }
        }
        literal("insert") {
            // line
            dynamic {
                suggestion<Player>(uncheck = true) { sender, _ ->
                    (1..(sender.itemInHand.itemMeta?.lore?.size ?: 1)).map { it.toString() }
                }
                restrict<Player> { _, _, argument ->
                    Coerce.asInteger(argument).isPresent
                }
                // text
                dynamic {
                    execute<Player> { sender, context, argument ->
                        if (sender.itemInHand.isAir()) {
                            return@execute
                        }
                        sender.itemInHand.modifyLore {
                            addSafely(context.argument(-1)!!.toInt()-1, argument.colored(), "")
                        }
                    }
                }
            }
        }
        literal("pop") {
            // line
            dynamic(optional = true) {
                restrict<Player> { _, _, argument ->
                    Coerce.asInteger(argument).isPresent
                }
                execute<Player> { sender, _, argument ->
                    if (sender.itemInHand.isAir()) {
                        return@execute
                    }
                    sender.itemInHand.modifyLore {
                        try {
                            sender.sendMessage("已移除Lore: ${this.removeAt(argument.toInt()-1)}")
                        }
                        catch (e: IndexOutOfBoundsException) {
                            sender.sendMessage("该物品第${argument}行没有Lore!")
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