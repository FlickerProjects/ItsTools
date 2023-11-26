package io.github.itsflicker.itstools.module.command.impl

import io.github.itsflicker.itstools.conf
import io.github.itsflicker.itstools.module.resourcepack.COSUploader
import io.github.itsflicker.itstools.module.resourcepack.OSSUploader
import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import io.github.itsflicker.itstools.util.allSymbol
import io.github.itsflicker.itstools.util.isItemsAdderHooked
import io.github.itsflicker.itstools.util.playerFor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submitAsync

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
                ResourcePack.send(sender, argument)
            }
        }
    }

    @CommandBody(permission = "itstools.command.resourcepack.send")
    val send = subCommand {
        player(suggest = allSymbol) {
            dynamic("id") {
                suggest {
                    conf.resource_packs.keys.toList()
                }
                execute<ProxyCommandSender> { _, ctx, argument ->
                    ctx.playerFor {
                        ResourcePack.send(it, argument)
                    }
                }
            }
        }
    }

    @CommandBody(permission = "itstools.command.resourcepack.upload")
    val upload = subCommand {
        dynamic("type") {
            suggest {
                listOf("cos", "oss")
            }
            dynamic("file") {
                suggest {
                    var array = newFile(getDataFolder(), "packs", folder = true).list()!!
                    if (isItemsAdderHooked) {
                        array += "itemsadder"
                    }
                    array.toList()
                }
                execute<CommandSender> { sender, ctx, argument ->
                    val file = when (argument) {
                        "itemsadder" -> {
                            getDataFolder()
                                .resolveSibling("ItemsAdder")
                                .resolve("output")
                                .resolve("generated.zip")
                        }
                        else -> {
                            getDataFolder().resolve("packs").resolve(argument)
                        }
                    }
                    submitAsync {
                        val succeed = when (ctx["type"]) {
                            "cos" -> COSUploader.upload(file)
                            "oss" -> OSSUploader.upload(file)
                            else -> error("out of case")
                        }
                        if (succeed) {
                            sender.sendMessage("§a上传成功!")
                        } else {
                            sender.sendMessage("§c上传失败!")
                        }
                    }
                }
            }
        }

    }

}