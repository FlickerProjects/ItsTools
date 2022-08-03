package io.github.itsflicker.fltools.module.command

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import io.github.itsflicker.fltools.api.NMS
import org.bukkit.NamespacedKey
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.command.*
import taboolib.common5.Demand
import taboolib.expansion.createHelper
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.ai.getGoalAi
import taboolib.module.ai.getTargetAi
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * CommandMisc
 * io.github.itsflicker.fltools.module.command
 *
 * @author wlys
 * @since 2021/8/3 15:20
 */
@CommandHeader("floperation", ["fo"], "FlTools-FlOperation", permission = "flentity.access")
object CommandOperation {

    val cache: Cache<UUID, Consumer<LivingEntity>> = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build()

    @Suppress("Deprecation")
    @CommandBody(permission = "fltools.command.addpotion", optional = true)
    val addpotion = subCommand {
        dynamic("potion") {
            suggest {
                PotionEffectType.values().map { it.key.toString() }
            }
            execute<Player> { sender, _, argument ->
                val key = argument.split(':').let { NamespacedKey(it[0], it[1]) }
                cache.put(sender.uniqueId) {
                    it.addPotionEffect(PotionEffect(PotionEffectType.getByKey(key)!!, 30 * 20, 0))
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
            dynamic("args", optional = true) {
                suggest {
                    listOf("-d", "-a", "--ambient", "--p", "--i")
                }
                execute<Player> { sender, context, argument ->
                    val key = context.argument(-1).split(':').let { NamespacedKey(it[0], it[1]) }
                    val de = Demand("potion $argument")
                    val duration = de.get(listOf("duration", "d"), "30")!!.toInt() * 20
                    val amplifier = de.get(listOf("amplifier", "a"), "1")!!.toInt().minus(1)
                    val ambient = de.tags.contains("ambient")
                    val particles = de.tags.contains("p")
                    val icon = de.tags.contains("i")
                    cache.put(sender.uniqueId) {
                        it.addPotionEffect(PotionEffect(PotionEffectType.getByKey(key)!!, duration, amplifier, ambient, particles, icon))
                    }
                    sender.sendMessage("§cClick an entity in the next 10 seconds.")
                }
            }
        }
    }

    @CommandBody(permission = "fltools.command.makemeleehostile", optional = true)
    val makemeleehostile = subCommand {
        execute<Player> { sender, _, _ ->
            cache.put(sender.uniqueId) {
                NMS.INSTANCE.makeMeleeHostile(it)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
        dynamic("args", optional = true) {
            suggest {
                listOf("-d", "-s")
            }
            execute<Player> { sender, _, argument ->
                val de = Demand("0 $argument")
                val damage = de.get(listOf("damage", "d"), "2.0")!!.toDouble()
                val speed = de.get(listOf("speed", "s"), "1.0")!!.toDouble()
                cache.put(sender.uniqueId) {
                    NMS.INSTANCE.makeMeleeHostile(it, damage, speed)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(permission = "fltools.command.getai", optional = true)
    val getai = subCommand {
        execute<Player> { sender, _, _ ->
            cache.put(sender.uniqueId) {
                sender.sendMessage(it.getGoalAi()
                    .sortedBy { ai -> ai!!.invokeMethod<Int>("getPriority") }
                    .joinToString("\n") { ai -> ai!!.invokeMethod<Any>("getGoal")!!.javaClass.simpleName }
                )
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(permission = "fltools.command.gettarget", optional = true)
    val gettarget = subCommand {
        execute<Player> { sender, _, _ ->
            cache.put(sender.uniqueId) {
                sender.sendMessage(it.getTargetAi()
                    .sortedBy { target -> target!!.invokeMethod<Int>("getPriority") }
                    .joinToString("\n") { target -> target!!.invokeMethod<Any>("getGoal")!!.javaClass.simpleName }
                )
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(permission = "fltools.command.getentityuuid", optional = true)
    val getentityuuid = subCommand {
        execute<Player> { sender, _, _ ->
            val location = sender.eyeLocation
            val direction = location.direction
            location.add(direction)
            sender.sendMessage(sender.world.rayTraceEntities(location, direction, 25.0)?.hitEntity?.uniqueId.toString())
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }
}