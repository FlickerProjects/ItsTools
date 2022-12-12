package io.github.itsflicker.itstools.module.listeners

import io.github.itsflicker.itstools.conf
import io.github.itsflicker.itstools.module.feature.DebugItem
import io.github.itsflicker.itstools.module.feature.IPInfo
import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import io.github.itsflicker.itstools.module.script.Reaction
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.*
import taboolib.common.platform.Ghost
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * Listeners
 * io.github.itsflicker.itstools.module.listeners
 *
 * @author wlys
 * @since 2021/8/4 20:42
 */
@Suppress("unused")
object Listeners {

    fun quit(player: Player) {
        ResourcePack.selected.remove(player.uniqueId)?.removed?.eval(player)
        DebugItem.cooldown.reset(player.name)
        IPInfo.caches.remove(player.uniqueId)
    }

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        IPInfo.cacheFromCloud(e.player)
    }

    @SubscribeEvent
    fun onQuit(e: PlayerQuitEvent) {
        quit(e.player)
    }

    @SubscribeEvent
    fun onKick(e: PlayerKickEvent) {
        quit(e.player)
    }

    @Ghost
    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onSwap(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        if (conf.shortcuts.cooldown.hasNext(player.name)) {
            val view = player.location.pitch
            if (player.isSneaking && conf.shortcuts.sneak_swap != Reaction.EMPTY) {
                e.isCancelled = true
                conf.shortcuts.sneak_swap.eval(player)
            }
            else if (view >= 80.0F && conf.shortcuts.down_swap != Reaction.EMPTY) {
                e.isCancelled = true
                conf.shortcuts.down_swap.eval(player)
            }
            else if (view <= -80.0F && conf.shortcuts.up_swap != Reaction.EMPTY) {
                e.isCancelled = true
                conf.shortcuts.up_swap.eval(player)
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDrop(e: PlayerDropItemEvent) {
        val player = e.player
        if (conf.shortcuts.cooldown.hasNext(player.name)) {
            val view = player.location.pitch
            if (player.isSneaking && conf.shortcuts.sneak_drop != Reaction.EMPTY) {
                e.isCancelled = true
                conf.shortcuts.sneak_drop.eval(player)
            }
            else if (view >= 80.0F && conf.shortcuts.down_drop != Reaction.EMPTY) {
                e.isCancelled = true
                conf.shortcuts.down_drop.eval(player) ?: return
            }
            else if (view <= -80.0F && conf.shortcuts.up_drop != Reaction.EMPTY) {
                e.isCancelled = true
                conf.shortcuts.up_drop.eval(player) ?: return
            }
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun onSpawn(e: CreatureSpawnEvent) {
        val entity = e.entity
        if (entity is Phantom && e.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            entity.getNearbyEntities(11.0, 40.0, 11.0).forEach {
                if (it is Player && it.hasPermission("itstools.stopphantomspawn")) {
                    e.isCancelled = true
                    return
                }
            }
        }
    }

    @SubscribeEvent
    fun onRPStatus(e: PlayerResourcePackStatusEvent) {
        val player = e.player
        val resourcePack = ResourcePack.selected[player.uniqueId] ?: return
        when (e.status) {
            PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED -> {
                resourcePack.loaded.eval(player)
            }
            PlayerResourcePackStatusEvent.Status.DECLINED -> {
                ResourcePack.selected.remove(player.uniqueId)
                resourcePack.declined.eval(player)
            }
            PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> {
                ResourcePack.selected.remove(player.uniqueId)
                resourcePack.failed.eval(player)
            }
            PlayerResourcePackStatusEvent.Status.ACCEPTED -> {
                resourcePack.accepted.eval(player)
            }
        }
    }

}