package io.github.itsflicker.itstools.module.command.impl

import dev.lone.itemsadder.api.ItemsAdder
import io.github.itsflicker.itstools.conf
import io.github.itsflicker.itstools.module.resourcepack.COSUploader
import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import io.github.itsflicker.itstools.util.isItemsAdderHooked
import io.github.itsflicker.itstools.util.isOraxenHooked
import io.github.itsflicker.itstools.util.nms
import io.th0rgal.oraxen.OraxenPlugin
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.command.suggestPlayers
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.warning

/**
 * CommandResourcePack
 * io.github.itsflicker.itstools.module.command.impl
 *
 * @author wlys
 * @since 2021/8/6 22:55
 */
object CommandResourcePack {

    @CommandBody(permission = "itstools.command.resourcepack.get")
    val get = subCommand {
        dynamic("id") {
            suggestion<Player> { sender, _ ->
                conf.resource_packs.filterValues { it.condition.eval(sender) }.keys.toList()
            }
            execute<Player> { sender, _, argument ->
                send(sender, argument)
            }
        }
    }

    @CommandBody(permission = "itstools.command.resourcepack.send")
    val send = subCommand {
        dynamic("player") {
            suggestPlayers()
            dynamic("id") {
                suggest {
                    conf.resource_packs.keys.toList()
                }
                execute<ProxyCommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayer(context.argument(-1))!!
                    send(player, argument)
                }
            }
        }
    }

    @CommandBody(permission = "itstools.command.resourcepack.upload")
    val upload = subCommand {
        literal("cos") {
            dynamic("file") {
                suggest {
                    var array = newFile(getDataFolder(), "packs", folder = true).list()
                    if (array != null && isItemsAdderHooked) {
                        array += "itemsadder"
                    }
                    array?.toList()
                }
                execute<CommandSender> { _, _, argument ->
                    when (argument) {
                        "itemsadder" -> {
                            COSUploader.upload(getDataFolder()
                                .resolveSibling("ItemsAdder")
                                .resolve("output")
                                .resolve("generated.zip"))
                        }
                        else -> {
                            val file = getDataFolder().resolve("packs").resolve(argument)
                            COSUploader.upload(file)
                        }
                    }
                }
            }
        }
        literal("oss") {

        }
    }

    private fun send(player: Player, id: String) {
        val resourcePack = conf.resource_packs[id] ?: return
        ResourcePack.selected.remove(player.uniqueId)?.removed?.eval(player)
        when {
            resourcePack.url.equals("itemsadder", ignoreCase = true) -> {
                if (isItemsAdderHooked) {
                    ItemsAdder.applyResourcepack(player)
                } else {
                    warning("ItemsAdder not loaded.")
                }
            }
            resourcePack.url.equals("oraxen", ignoreCase = true) -> {
                if (isOraxenHooked) {
                    OraxenPlugin.get().uploadManager.sender.sendPack(player)
                } else {
                    warning("Oraxen not loaded.")
                }
            }
            else -> nms.sendResourcePack(player, resourcePack.url, resourcePack.hash)
        }
        ResourcePack.selected[player.uniqueId] = resourcePack
    }
}