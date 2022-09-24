package io.github.itsflicker.itstools.util

import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor

// 模 / 时间 = 速度
object InstantVector {

    private val tasks = mutableMapOf<Int, PlatformExecutor.PlatformTask>()

    fun useToVanilla(entity: Entity, x: Double, y: Double, z: Double, arrival: Long) {
        tasks[entity.entityId]?.cancel()
        entity.velocity = Vector(check(x / arrival), check(y / arrival), check(z / arrival))
        tasks[entity.entityId] = submit(delay = arrival) {
            entity.velocity = Vector()
        }
    }

    private fun check(num: Double) = if (num.isInfinite()) 0.0 else num

}