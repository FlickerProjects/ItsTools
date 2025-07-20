package io.github.itsflicker.tools.api

import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.world.entity.EntityCreature
import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.EntityLiving
import net.minecraft.world.entity.ai.attributes.GenericAttributes
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_21_R5.CraftServer
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity
import org.bukkit.craftbukkit.v1_21_R5.util.CraftChatMessage
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.warning
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.chat.ComponentText
import taboolib.module.nms.nmsClass

/**
 * NMSImpl
 * io.github.itsflicker.tools.api
 *
 * @author wlys
 * @since 2021/8/3 14:59
 */
@Suppress("unused")
class NMSImpl : NMS() {

    private fun craftChatMessageFromComponent(component: ComponentText): IChatBaseComponent {
        return CraftChatMessage.fromJSON(component.toRawMessage())
    }

    private fun LivingEntity.getEntityLiving(): EntityLiving? {
        return (this as CraftEntity).handle as? EntityLiving
    }

    private fun LivingEntity.getEntityInsentient(): EntityInsentient? {
        return (this as CraftEntity).handle as? EntityInsentient
    }

    private fun LivingEntity.getEntityCreature(): EntityCreature? {
        return (this as CraftEntity).handle as? EntityCreature
    }

    private fun addGoalAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        entity.getEntityInsentient()?.goalSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    private fun addTargetAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        entity.getEntityInsentient()?.targetSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun getTargetEntity(entity: LivingEntity): LivingEntity? {
        val entityInsentient = entity.getEntityInsentient() ?: return null
        val target = entityInsentient.target ?: return null
        return CraftEntity.getEntity((entity as CraftEntity).server as CraftServer, target) as? LivingEntity
    }

    override fun setTargetEntity(entity: LivingEntity, target: LivingEntity?) {
        val entityInsentient = entity.getEntityInsentient()
            ?: return warning("${entity.type} is not EntityInsentient.")
        // setTarget -> Unknown target reason, please report on the issue tracker
        entityInsentient.setProperty("target", target?.getEntityLiving())
    }

    override fun getSpeed(entity: LivingEntity): Double {
        return entity.getEntityInsentient()!!.speed.toDouble()
    }

    override fun getTargetLocation(entity: LivingEntity): Location? {
        val navigation = entity.getEntityInsentient()!!.navigation
        return navigation.path?.endNode?.let { Location(entity.world, it.x.toDouble(), it.y.toDouble(), it.z.toDouble()) }
    }

    override fun makeMeleeHostile(entity: LivingEntity, damage: Double?, speed: Double, priority: Int, type: String, followingTargetEvenIfNotSeen: Boolean) {
        val entityCreature = entity.getEntityCreature()
            ?: return warning("${entity.type} is not EntityCreature.")
        if (damage != null) {
            entityCreature.attributes.invokeMethod<Void>("registerAttribute", GenericAttributes.ATTACK_DAMAGE)
            entityCreature.getAttribute(GenericAttributes.ATTACK_DAMAGE)!!.baseValue = damage
        }
        // add goal selector
        addGoalAi(entity, priority, PathfinderGoalMeleeAttack(entityCreature, speed, followingTargetEvenIfNotSeen))
        // add target selector
        addTargetAi(entity, 1, PathfinderGoalHurtByTarget(entityCreature))
        addTargetAi(entity, 2, PathfinderGoalNearestAttackableTarget(entityCreature, nmsClass(type).asSubclass(EntityLiving::class.java), true))
    }

}