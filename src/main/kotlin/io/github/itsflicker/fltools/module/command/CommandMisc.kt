package io.github.itsflicker.fltools.module.command

import io.github.itsflicker.fltools.api.NMS
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.CommandBody
import taboolib.common.platform.CommandHeader
import taboolib.common.platform.subCommand
import taboolib.common5.Coerce
import java.util.*

/**
 * CommandMisc
 * io.github.itsflicker.fltools.module.command
 *
 * @author wlys
 * @since 2021/8/3 15:20
 */
@CommandHeader("flmisc", ["fm"], permission = "fltools.access")
object CommandMisc {

    @CommandBody(permission = "fltools.command.makemeleehostile")
    val makeMeleeHostile = subCommand {
        dynamic {
            dynamic(optional = true) {
                suggestion<Player>(uncheck = true) { _, _ ->
                    listOf("1.0", "2.0", "3.0", "4.0", "5.0")
                }
                restrict<Player> { _, _, argument ->
                    Coerce.asDouble(argument).isPresent
                }
                execute<Player> { _, context, argument ->
                    (Bukkit.getEntity(UUID.fromString(context.argument(-1))) as? LivingEntity)?.let {
                        NMS.INSTANCE.makeMeleeHostile(it, Coerce.toDouble(argument))
                    } ?: run {  }
                }
            }
            suggestion<Player>(uncheck = true) { sender, _ ->
                sender.world.livingEntities.filter { it.type != EntityType.PLAYER && it.location.distance(sender.location) <= 50 }
                    .sortedBy { it.location.distance(sender.location) }
                    .map { it.uniqueId.toString() }
            }
            execute<Player> { _, context, _ ->
                (Bukkit.getEntity(UUID.fromString(context.argument(0))) as? LivingEntity)?.let {
                    NMS.INSTANCE.makeMeleeHostile(it)
                } ?: run {  }
            }
        }
    }

    @CommandBody(permission = "fltools.command.getentityuuid")
    val getEntityUUID = subCommand {
        execute<Player> { sender, _, _ ->
            val location = sender.eyeLocation
            val direction = location.direction
            location.add(direction)
            sender.sendMessage(sender.world.rayTraceEntities(location, direction, 15.0)?.hitEntity?.uniqueId.toString())
        }
    }
}