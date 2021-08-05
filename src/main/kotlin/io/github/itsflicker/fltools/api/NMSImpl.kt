package io.github.itsflicker.fltools.api

import net.minecraft.world.entity.EntityCreature
import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.attributes.AttributeBase
import net.minecraft.world.entity.ai.attributes.AttributeModifiable
import net.minecraft.world.entity.ai.attributes.GenericAttributes
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget
import net.minecraft.world.entity.player.EntityHuman
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity
import org.bukkit.entity.LivingEntity
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.invokeMethod

/**
 * NMSImpl
 * io.github.itsflicker.fltools.api
 *
 * @author wlys
 * @since 2021/8/3 14:59
 */
class NMSImpl : NMS() {

    override fun getEntityInsentient(entity: LivingEntity): Any? {
        return (entity as CraftLivingEntity).handle
    }

    override fun addGoalAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        (getEntityInsentient(entity) as? EntityInsentient)?.goalSelector?.a(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun addTargetAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        (getEntityInsentient(entity) as? EntityInsentient)?.targetSelector?.a(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun makeMeleeHostile(entity: LivingEntity, damage: Double) {
        // add generic:attack_damage attribute
        (getEntityInsentient(entity) as EntityInsentient).attributeMap
            .getProperty<HashMap<AttributeBase, AttributeModifiable>>("b")!![GenericAttributes.ATTACK_DAMAGE] =
            AttributeModifiable(GenericAttributes.ATTACK_DAMAGE) { (getEntityInsentient(entity) as EntityInsentient).attributeMap.invokeMethod<Unit>("a", it) }
        // set entity damage
        (getEntityInsentient(entity) as EntityInsentient)
            .getAttributeInstance(GenericAttributes.ATTACK_DAMAGE)!!
            .value = damage
        // add goal selector
        addGoalAi(entity, 2, PathfinderGoalMeleeAttack(getEntityInsentient(entity) as EntityCreature, 1.0, false))
        // add target selector
        addTargetAi(entity, 1, PathfinderGoalHurtByTarget(getEntityInsentient(entity) as EntityCreature))
        addTargetAi(entity, 2, PathfinderGoalNearestAttackableTarget(getEntityInsentient(entity) as EntityInsentient, EntityHuman::class.java, true))
    }
}