package io.github.itsflicker.itstools.module.ai

import org.bukkit.entity.Boat
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.pathfinderExecutor
import kotlin.properties.Delegates

class BoatingAi(val entity: LivingEntity) : SimpleAi() {

    private var current: Vector? by Delegates.observable(null) { _, oldValue, newValue ->
        if (oldValue == newValue) {
            return@observable
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
        return entity.vehicle is Boat
    }

    override fun continueExecute(): Boolean {
        return false
    }

}