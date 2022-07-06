package io.github.itsflicker.fltools.module.command.impl

import dev.lone.itemsadder.api.ItemsAdder
import io.github.itsflicker.fltools.Settings
import io.github.itsflicker.fltools.api.NMS
import io.github.itsflicker.fltools.module.resourcepack.ResourcePack
import io.th0rgal.oraxen.OraxenPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.warning
import taboolib.module.kether.KetherShell
import taboolib.module.kether.runKether

/**
 * CommandRP
 * io.github.itsflicker.fltools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/6 22:55
 */
object CommandRP {

    private val isOraxenHooked by lazy { Bukkit.getPluginManager().isPluginEnabled("Oraxen") }
    private val isItemsAdderHooked  by lazy { Bukkit.getPluginManager().isPluginEnabled("ItemsAdder") }

    val command = subCommand {
        dynamic("id") {
            suggestion<ProxyCommandSender> { sender, _ ->
                Settings.packs.get().filterValues { it.permission == null || sender.hasPermission(it.permission) }.keys.toList()
            }
            execute<Player> { sender, _, argument ->
                val resourcePack = Settings.packs.get()[argument]!!
                send(sender, resourcePack)
            }
            dynamic("player", optional = true) {
                suggestion<ProxyCommandSender> { sender, _ ->
                    if (sender.hasPermission("fltools.command.rp.other")) {
                        onlinePlayers().map { it.name }
                    } else {
                        emptyList()
                    }
                }
                execute<ProxyCommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayer(argument)!!
                    val resourcePack = Settings.packs.get()[context.argument(-1)]!!
                    send(player, resourcePack)
                }
            }
        }
    }

    private fun send(player: Player, resourcePack: ResourcePack) {
        ResourcePack.selected.remove(player.uniqueId)?.onRemoved?.let { runKether { KetherShell.eval(it, sender = adaptPlayer(player)) } }
        when {
            resourcePack.id.equals("itemsadder", ignoreCase = true) -> {
                if (isItemsAdderHooked) {
                    ItemsAdder.applyResourcepack(player)
                } else {
                    warning("ItemsAdder not loaded.")
                }
            }
            resourcePack.id.equals("oraxen", ignoreCase = true) -> {
                if (isOraxenHooked) {
                    OraxenPlugin.get().uploadManager.sender.sendPack(player)
                } else {
                    warning("Oraxen not loaded.")
                }
            }
            else -> NMS.INSTANCE.sendResourcePack(player, resourcePack.url, resourcePack.hash)
        }
        ResourcePack.selected[player.uniqueId] = resourcePack
    }
}