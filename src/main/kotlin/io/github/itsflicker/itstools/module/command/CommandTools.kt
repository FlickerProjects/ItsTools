package io.github.itsflicker.itstools.module.command

import io.github.itsflicker.itstools.ItsTools
import io.github.itsflicker.itstools.module.command.impl.*
import io.github.itsflicker.itstools.module.feature.DebugItem
import io.github.itsflicker.itstools.util.allSymbol
import io.github.itsflicker.itstools.util.playerFor
import io.github.itsflicker.itstools.util.spawnRandomFirework
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common5.Demand
import taboolib.expansion.createHelper
import taboolib.module.kether.KetherShell
import taboolib.module.kether.runKether
import taboolib.platform.util.giveItem
import taboolib.platform.util.toBukkitLocation

/**
 * CommandHandler
 * io.github.itsflicker.itstools.module.command
 *
 * @author wlys
 * @since 2021/7/31 21:55
 */
@CommandHeader("itstools", ["it"], "Main command of ItsTools", permission = "itstools.access")
object CommandTools {

    @CommandBody(permission = "itstools.command.light", optional = true)
    val light = CommandLight.command

    @CommandBody(permission = "itstools.command.sendtoast", optional = true)
    val sendtoast = CommandSendToast.command

    @CommandBody(permission = "itstools.command.sendmap", optional = true)
    val sendmap = CommandSendMap.command

    @CommandBody(permission = "itstools.command.lore", optional = true)
    val lore = CommandLore.command

    @CommandBody(["rp"], optional = true)
    val resourcepack = CommandResourcePack

//    @CommandBody(optional = true)
//    val timebar = subCommand {
//
//    }

    @CommandBody(permission = "itstools.command.firework", optional = true)
    val firework = subCommand {
        location(euler = false) {
            int("fuse") {
                int("power") {
                    int("maxEffects") {
                        execute<CommandSender> { _, ctx, _ ->
                            val location = ctx.location().toBukkitLocation()
                            val fuse = ctx.int("fuse")
                            val power = ctx.int("power")
                            val maxEffects = ctx.int("maxEffects")
                            spawnRandomFirework(location, fuse, power, maxEffects)
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "itstools.command.forcechat", optional = true)
    val forcechat = subCommand {
        dynamic("player") {
            suggestPlayers(allSymbol)
            dynamic("message") {
                execute<CommandSender> { _, context, argument ->
                    context.playerFor {
                        it.chat(argument)
                    }
                }
            }
        }
    }

    @CommandBody(permission = "itstools.command.getdebugitem", optional = true)
    val getdebugitem = subCommand {
        execute<Player> { sender, _, _ ->
            sender.giveItem(DebugItem.item)
        }
    }

    @CommandBody(permission = "itstools.command.simplekether", optional = true)
    val simplekether = subCommand {
        dynamic("args") {
            suggestUncheck {
                listOf("-source", "-namespace", "-sender")
            }
            execute<ProxyCommandSender> { sender, _, argument ->
                val de = Demand("simpleKether $argument")
                val source = de.get("source") ?: return@execute
                val namespace = de.get("namespace")?.split(";") ?: emptyList()
                val proxySender = de.get("sender")?.let { getProxyPlayer(it) } ?: sender
                runKether { KetherShell.eval(source, namespace = namespace, sender = proxySender) }
            }
        }
    }

    @CommandBody(permission = "itstools.command.reload", optional = true)
    val reload = subCommand {
        execute<CommandSender> { _, _, _ ->
            ItsTools.reload()
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}