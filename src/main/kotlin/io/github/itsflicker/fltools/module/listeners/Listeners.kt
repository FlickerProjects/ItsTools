package io.github.itsflicker.fltools.module.listeners

import io.github.itsflicker.fltools.FlTools
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.*
import taboolib.common5.Baffle
import java.util.concurrent.TimeUnit

/**
 * Listeners
 * io.github.itsflicker.fltools.module.listeners
 *
 * @author wlys
 * @since 2021/8/4 20:42
 */
@PlatformSide([Platform.BUKKIT])
object Listeners {

    private var swapCoolDown: Baffle? = null

    @Awake(LifeCycle.ENABLE)
    fun reload() {
        swapCoolDown = Baffle.of(FlTools.swapCoolDown, TimeUnit.MILLISECONDS)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerSwapHandItems(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        if (swapCoolDown!!.hasNext(player.name)) {
            val view = player.location.pitch
            if (player.isSneaking) {
                player.chat(FlTools.sneakF)
                e.isCancelled = true
            }
            else if (view >= 80.0F) {
                player.chat(FlTools.lookDownF)
                e.isCancelled = true
            }
            else if (view <= -80.0F) {
                player.chat(FlTools.lookUpF)
                e.isCancelled = true
            }
        }
    }

}