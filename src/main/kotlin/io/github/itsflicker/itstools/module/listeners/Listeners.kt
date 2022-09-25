package io.github.itsflicker.itstools.module.listeners

import io.github.itsflicker.itstools.conf
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
object Listeners {

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        ResourcePack.selected.remove(e.player.uniqueId)?.removed?.eval(e.player)
    }

    @SubscribeEvent
    fun e(e: PlayerKickEvent) {
        ResourcePack.selected.remove(e.player.uniqueId)?.removed?.eval(e.player)
    }

    @Ghost
    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: PlayerSwapHandItemsEvent) {
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
    fun e(e: PlayerDropItemEvent) {
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
    fun e(e: CreatureSpawnEvent) {
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
    fun e(e: PlayerResourcePackStatusEvent) {
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