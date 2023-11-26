package io.github.itsflicker.itstools.module.command

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import io.github.itsflicker.itstools.module.feature.DebugItem
import io.github.itsflicker.itstools.util.nms
import org.bukkit.Bukkit
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
import taboolib.module.ai.removeGoalAi
import taboolib.module.ai.removeTargetAi
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * CommandMisc
 * io.github.itsflicker.itstools.module.command
 *
 * @author wlys
 * @since 2021/8/3 15:20
 */
@CommandHeader("itsoperation", ["io"], "ItsTools-Operations", permission = "itstools.access")
object CommandOperation {

    val cacheOperations: Cache<UUID, Consumer<LivingEntity>> = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.SECONDS)
        .build()

    @CommandBody(permission = "itstools.command.addpotion", optional = true)
    val addpotion = subCommand {
        dynamic("potion") {
            suggest {
                PotionEffectType.values().map { it.key.key }
            }
            execute<Player> { sender, _, arg ->
                val key = NamespacedKey.minecraft(arg)
                cacheOperations.put(sender.uniqueId) {
                    it.addPotionEffect(PotionEffect(PotionEffectType.getByKey(key)!!, 30 * 20, 0))
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
            dynamic("args", optional = true) {
                suggest {
                    listOf("-d", "-a", "--ambient", "--p", "--i")
                }
                execute<Player> { sender, ctx, arg ->
                    val key = NamespacedKey.minecraft(ctx["potion"])
                    val de = Demand("potion $arg")
                    val duration = de.get(listOf("duration", "d"), "30")!!.toInt() * 20
                    val amplifier = de.get(listOf("amplifier", "a"), "1")!!.toInt().minus(1)
                    val ambient = de.tags.contains("ambient")
                    val particles = de.tags.contains("p")
                    val icon = de.tags.contains("i")
                    cacheOperations.put(sender.uniqueId) {
                        it.addPotionEffect(PotionEffect(PotionEffectType.getByKey(key)!!, duration, amplifier, ambient, particles, icon))
                    }
                    sender.sendMessage("§cClick an entity in the next 10 seconds.")
                }
            }
        }
    }

    @CommandBody(permission = "itstools.command.makemeleehostile", optional = true)
    val makemeleehostile = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) {
                nms.makeMeleeHostile(it)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
        dynamic("args", optional = true) {
            suggestUncheck {
                listOf("-d", "-s", "-t", "-p", "--f")
            }
            execute<Player> { sender, _, arg ->
                val de = Demand("0 $arg")
                val damage = de.get(listOf("damage", "d"))?.toDouble()
                val speed = de.get(listOf("speed", "s"), "1.0")!!.toDouble()
                val priority = de.get(listOf("priority", "p"), "2")!!.toInt()
                val type = de.get(listOf("type", "t"), "EntityHuman")!!
                val followingTargetEvenIfNotSeen = de.tags.contains("followingTargetEvenIfNotSeen") || de.tags.contains("f")
                cacheOperations.put(sender.uniqueId) {
                    nms.makeMeleeHostile(it, damage, speed, priority, type, followingTargetEvenIfNotSeen)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(permission = "itstools.command.removegoal", optional = true)
    val removegoal = subCommand {
        dynamic("goal") {
            execute<Player> { sender, _, arg ->
                cacheOperations.put(sender.uniqueId) {
                    it.removeGoalAi(arg)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(permission = "itstools.command.removetarget", optional = true)
    val removetarget = subCommand {
        dynamic("target") {
            execute<Player> { sender, _, arg ->
                cacheOperations.put(sender.uniqueId) {
                    it.removeTargetAi(arg)
                }
                sender.sendMessage("§cClick an entity in the next 10 seconds.")
            }
        }
    }

    @CommandBody(permission = "itstools.command.getgoal", optional = true)
    val getgoal = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) {
                sender.sendMessage(it.getGoalAi()
                    .sortedBy { ai -> ai!!.invokeMethod<Int>("getPriority") }
                    .joinToString("\n") { ai -> ai!!.invokeMethod<Any>("getGoal")!!.javaClass.simpleName }
                )
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(permission = "itstools.command.gettarget", optional = true)
    val gettarget = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) {
                sender.sendMessage(it.getTargetAi()
                    .sortedBy { target -> target!!.invokeMethod<Int>("getPriority") }
                    .joinToString("\n") { target -> target!!.invokeMethod<Any>("getGoal")!!.javaClass.simpleName }
                )
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(permission = "itstools.command.settarget", optional = true)
    val settarget = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) { target ->
                val entity = DebugItem.cache[sender.name]?.let { Bukkit.getEntity(it) as? LivingEntity } ?: return@put
                nms.setTargetEntity(entity, target)
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

    @CommandBody(permission = "itstools.command.togglegravity", optional = true)
    val togglegravity = subCommand {
        execute<Player> { sender, _, _ ->
            cacheOperations.put(sender.uniqueId) {
                it.setGravity(!it.hasGravity())
            }
            sender.sendMessage("§cClick an entity in the next 10 seconds.")
        }
    }

//    @CommandBody(permission = "itstools.command.getentityuuid", optional = true)
//    val getentityuuid = subCommand {
//        execute<Player> { sender, _, _ ->
//            val location = sender.eyeLocation
//            val direction = location.direction
//            location.add(direction)
//            sender.sendMessage(sender.world.rayTraceEntities(location, direction, 25.0)?.hitEntity?.uniqueId.toString())
//        }
//    }

//    @CommandBody(permission = "admin", optional = true)
//    val test = subCommand {
//        execute<Player> { sender, _, _ ->
//            cacheOperations.put(sender.uniqueId) {
//                it.addGoalAi(BoatingAi(it), 1)
//            }
//            sender.sendMessage("§cClick an entity in the next 10 seconds.")
//        }
//    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

}