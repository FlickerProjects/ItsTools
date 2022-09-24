package io.github.itsflicker.itstools.module.ai

import io.github.itsflicker.itstools.util.InstantVector
import org.bukkit.entity.Boat
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Minecart
import org.bukkit.util.Vector
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.pathfinderExecutor
import kotlin.properties.Delegates

class BoatingAi(val entity: LivingEntity) : SimpleAi() {

    private val empty = Vector()

    private var current: Vector? by Delegates.observable(null) { _, oldValue, newValue ->
        if (oldValue == newValue && entity.vehicle?.velocity != empty) {
            return@observable
        }
        if (newValue != null) {
            InstantVector.useToVanilla(
                entity.vehicle!!,
                newValue.x - entity.vehicle!!.location.x,
                newValue.y - entity.vehicle!!.location.y + 0.5,
                newValue.z - entity.vehicle!!.location.z,
                (newValue.distance(entity.vehicle!!.location.toVector()) / 2 * 20).toLong()
            )
        }
    }

    override fun startTask() {
        current = pathfinderExecutor.getEntityInsentient(entity)
            .getProperty<Any>("navigation")!!
            .getProperty<Any>("path")
            ?.getProperty<List<Any>>("a")?.lastOrNull()
            ?.let { Vector(it.getProperty<Int>("a")!!, it.getProperty<Int>("b")!!, it.getProperty<Int>("c")!!) }
    }

    override fun shouldExecute(): Boolean {
        return entity.vehicle is Boat || entity.vehicle is Minecart
    }

    override fun continueExecute(): Boolean {
        return false
    }

}