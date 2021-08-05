package io.github.itsflicker.fltools.api

import org.bukkit.entity.LivingEntity
import taboolib.module.nms.nmsProxy

/**
 * NMS
 * io.github.itsflicker.fltools.api
 *
 * @author wlys
 * @since 2021/8/3 14:56
 */
abstract class NMS {

    abstract fun getEntityInsentient(entity: LivingEntity): Any?

    abstract fun addGoalAi(entity: LivingEntity,  priority: Int, pathfinderGoal: Any)

    abstract fun addTargetAi(entity: LivingEntity,  priority: Int, pathfinderGoal: Any)

    abstract fun makeMeleeHostile(entity: LivingEntity, damage: Double = 2.0)

    companion object {

        val INSTANCE = nmsProxy<NMS>()
    }
}