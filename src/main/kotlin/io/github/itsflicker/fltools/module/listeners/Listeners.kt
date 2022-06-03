package io.github.itsflicker.fltools.module.listeners

import io.github.itsflicker.fltools.Settings
import io.github.itsflicker.fltools.Settings.shortcutCoolDown
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
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

}