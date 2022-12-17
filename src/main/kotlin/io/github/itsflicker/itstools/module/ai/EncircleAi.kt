package io.github.itsflicker.itstools.module.ai

import io.github.itsflicker.itstools.util.nms
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.navigationMove
import kotlin.math.PI

class EncircleAi(
    val entity: LivingEntity,
    val distanceSqr: Double = 9.0,
    val speed: Double = 1.0
) : SimpleAi() {

    override fun shouldExecute(): Boolean {
        val target = nms.getTargetEntity(entity) ?: return false
        return entity.location.distanceSquared(target.location) <= distanceSqr
    }

    override fun startTask() {
        val target = nms.getTargetEntity(entity)?.location?.toVector() ?: return
        var relative = entity.location.toVector().subtract(target)
        relative = relative.rotateAroundY(PI / 3).normalize().multiply(2)
        val destination = target.add(relative).toLocation(entity.world)
        entity.navigationMove(destination, speed)
    }

    override fun updateTask() = startTask()

}