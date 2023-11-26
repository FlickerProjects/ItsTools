package io.github.itsflicker.itstools.module.feature

import io.github.itsflicker.itstools.util.spawnRandomFirework
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.Creeper
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityExplodeEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.configuration.ConfigNode
import taboolib.platform.util.kill

@Suppress("unused")
object CreeperConfetti {

    @ConfigNode("features.creeper_confetti.enabled")
    var enabled = false
        private set

    @ConfigNode("features.creeper_confetti.max_effects")
    var maxEffects = 3
        private set

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onExplode(e: EntityExplodeEvent) {
        if (!enabled) return
        val creeper = e.entity as? Creeper ?: return
        e.isCancelled = true
        spawnRandomFirework(creeper.eyeLocation, 1, if (creeper.isPowered) 6 else 3, maxEffects, creeper)
        spawnLingeringCloud(creeper)
        creeper.kill()
    }

    private fun spawnLingeringCloud(creeper: Creeper) {
        val effects = creeper.activePotionEffects
        if (effects.isNotEmpty()) {
            val cloud = creeper.world.spawnEntity(creeper.location, EntityType.AREA_EFFECT_CLOUD) as AreaEffectCloud
            cloud.radius = 2.5F
            cloud.radiusOnUse = -0.5F
            cloud.waitTime = 10
            cloud.duration /= 2
            cloud.radiusPerTick = -cloud.radius / cloud.duration
            effects.forEach {
                cloud.addCustomEffect(it, true)
            }
        }
    }

}