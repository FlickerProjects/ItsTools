package io.github.itsflicker.fltools.module.command

import io.github.itsflicker.fltools.module.DebugItem
import io.github.itsflicker.fltools.module.command.impl.*
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common5.Demand
import taboolib.expansion.createHelper
import taboolib.module.kether.KetherShell
import taboolib.module.kether.runKether
import taboolib.platform.util.giveItem

/**
 * CommandHandler
 * io.github.itsflicker.fltools.module.command
 *
 * @author wlys
 * @since 2021/7/31 21:55
 */
@CommandHeader("fltools", ["ft"], "FlTools主命令", permission = "fltools.access")
object CommandTools {

    @CommandBody(permission = "fltools.command.light", optional = true)
    val light = CommandLight.command

    @CommandBody(permission = "fltools.command.sendtoast", optional = true)
    val sendtoast = CommandSendToast.command

    @CommandBody(permission = "fltools.command.sendmap", optional = true)
    val sendmap = CommandSendMap.command

    @CommandBody(permission = "fltools.command.lore", optional = true)
    val lore = CommandLore.command

    @CommandBody(permission = "fltools.command.rp", optional = true)
    val rp = CommandRP.command

    @CommandBody(permission = "fltools.command.getdebugitem", optional = true)
    val getdebugitem = subCommand {
        execute<Player> { sender, _, _ ->
            sender.giveItem(DebugItem.item)
        }
    }

    @CommandBody(permission = "fltools.command.simplekether", optional = true)
    val simplekether = subCommand {
        dynamic("args") {
            suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
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
    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}