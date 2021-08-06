package io.github.itsflicker.fltools.module.command

import io.github.itsflicker.fltools.module.command.impl.CommandLight
import io.github.itsflicker.fltools.module.command.impl.CommandLore
import io.github.itsflicker.fltools.module.command.impl.CommandSendToast
import taboolib.common.platform.CommandBody
import taboolib.common.platform.CommandHeader
import taboolib.common.platform.subCommand

/**
 * CommandHandler
 * io.github.itsflicker.fltools.module.command
 *
 * @author wlys
 * @since 2021/7/31 21:55
 */
@CommandHeader("fltools", ["ft"], "FlTools主命令", permission = "fltools.access")
object CommandTools {

    @CommandBody(permission = "fltools.command.light")
    val light = CommandLight.command

    @CommandBody(permission = "fltools.command.sendtoast")
    val sendToast = CommandSendToast.command

    @CommandBody(permission = "fltools.command.lore")
    val lore = CommandLore.command

}