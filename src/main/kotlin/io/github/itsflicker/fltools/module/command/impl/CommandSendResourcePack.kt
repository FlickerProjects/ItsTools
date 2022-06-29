package io.github.itsflicker.fltools.module.command.impl

import dev.lone.itemsadder.api.ItemsAdder
import io.github.itsflicker.fltools.Settings
import io.github.itsflicker.fltools.api.NMS
import io.github.itsflicker.fltools.module.resourcepack.ResourcePack
import io.th0rgal.oraxen.OraxenPlugin
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.warning
import taboolib.module.kether.KetherShell

/**
 * CommandSendResourcePack
 * io.github.itsflicker.fltools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/6 22:55
 */
object CommandSendResourcePack {

    private val isOraxenHooked by lazy { Bukkit.getPluginManager().isPluginEnabled("Oraxen") }
    private val isItemsAdderHooked  by lazy { Bukkit.getPluginManager().isPluginEnabled("ItemsAdder") }

    val command = subCommand {
        dynamic("player") {
            suggestion<ProxyCommandSender> { _, _ ->
                onlinePlayers().map { it.name }
            }
            dynamic("id") {
                suggestion<ProxyCommandSender> { _, _ ->
                    Settings.packs.get().keys.toList()
                }
                execute<ProxyCommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayer(context.argument(-1))!!
                    ResourcePack.selected.remove(player.uniqueId)?.onRemoved?.let { KetherShell.eval(it, sender = adaptPlayer(player)) }
                    val resourcePack = Settings.packs.get()[argument]!!
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
        }
    }
}