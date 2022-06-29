package io.github.itsflicker.fltools.api

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
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

    abstract fun makeMeleeHostile(entity: LivingEntity, damage: Double = 2.0, speed: Double = 1.0)

    abstract fun sendResourcePack(player: Player, url: String, hash: String)

    companion object {

        val INSTANCE = nmsProxy<NMS>()
    }
}