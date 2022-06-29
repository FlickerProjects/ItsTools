package io.github.itsflicker.fltools.module.listeners

import io.github.itsflicker.fltools.Settings
import io.github.itsflicker.fltools.Settings.shortcutCoolDown
import io.github.itsflicker.fltools.module.resourcepack.ResourcePack
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.*
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.kether.KetherShell

/**
 * Listeners
 * io.github.itsflicker.fltools.module.listeners
 *
 * @author wlys
 * @since 2021/8/4 20:42
 */
object Listeners {

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        ResourcePack.selected.remove(e.player.uniqueId)
    }

    @SubscribeEvent
    fun e(e: PlayerKickEvent) {
        ResourcePack.selected.remove(e.player.uniqueId)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        if (shortcutCoolDown.get().hasNext(player.name)) {
            val view = player.location.pitch
            if (player.isSneaking) {
                e.isCancelled = true
                KetherShell.eval(Settings.sneakF, sender = adaptPlayer(player))
            }
            else if (view >= 80.0F) {
                e.isCancelled = true
                KetherShell.eval(Settings.lookDownF, sender = adaptPlayer(player))
            }
            else if (view <= -80.0F) {
                e.isCancelled = true
                KetherShell.eval(Settings.lookUpF, sender = adaptPlayer(player))
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: PlayerDropItemEvent) {
        val player = e.player
        if (shortcutCoolDown.get().hasNext(player.name)) {
            val view = player.location.pitch
            if (player.isSneaking) {
                e.isCancelled = true
                KetherShell.eval(Settings.sneakQ, sender = adaptPlayer(player))
            }
            else if (view >= 80.0F) {
                e.isCancelled = true
                KetherShell.eval(Settings.lookDownQ, sender = adaptPlayer(player))
            }
            else if (view <= -80.0F) {
                e.isCancelled = true
                KetherShell.eval(Settings.lookUpQ, sender = adaptPlayer(player))
            }
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun e(e: CreatureSpawnEvent) {
        val entity = e.entity
        if (entity is Phantom && e.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            entity.getNearbyEntities(11.0, 40.0, 11.0).forEach {
                if (it is Player && it.hasPermission("fltools.stopphantomspawn")) {
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
                resourcePack.onLoaded?.let { KetherShell.eval(it, sender = adaptPlayer(player)) }
            }
            PlayerResourcePackStatusEvent.Status.DECLINED -> {
                ResourcePack.selected.remove(player.uniqueId)
                resourcePack.onDeclined?.let { KetherShell.eval(it, sender = adaptPlayer(player)) }
            }
            PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> {
                ResourcePack.selected.remove(player.uniqueId)
                resourcePack.onFailedDownload?.let { KetherShell.eval(it, sender = adaptPlayer(player)) }
            }
            PlayerResourcePackStatusEvent.Status.ACCEPTED -> {
                resourcePack.onAccepted?.let { KetherShell.eval(it, sender = adaptPlayer(player)) }
            }
        }
    }

}