package io.github.itsflicker.fltools.api

import io.github.itsflicker.fltools.Settings
import net.minecraft.network.protocol.game.PacketPlayOutResourcePackSend
import net.minecraft.world.entity.EntityCreature
import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.attributes.AttributeBase
import net.minecraft.world.entity.ai.attributes.AttributeMapBase
import net.minecraft.world.entity.ai.attributes.AttributeModifiable
import net.minecraft.world.entity.ai.attributes.GenericAttributes
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget
import net.minecraft.world.entity.player.EntityHuman
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.invokeMethod
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.common.reflect.Reflex.Companion.unsafeInstance
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.sendPacket

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
        (getEntityInsentient(entity) as? EntityInsentient)?.goalSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun addTargetAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        (getEntityInsentient(entity) as? EntityInsentient)?.targetSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun makeMeleeHostile(entity: LivingEntity, damage: Double, speed: Double) {
        val entityInsentient = getEntityInsentient(entity) as EntityInsentient
        val map = entityInsentient.invokeMethod<AttributeMapBase>("getAttributes")!!
        // add generic:attack_damage attribute
        map.getProperty<HashMap<AttributeBase, AttributeModifiable>>("b")!![GenericAttributes.ATTACK_DAMAGE] =
            AttributeModifiable(GenericAttributes.ATTACK_DAMAGE) { map.invokeMethod<Void>("a", it) }
        // set entity damage
        entityInsentient.invokeMethod<AttributeModifiable>("getAttribute", GenericAttributes.ATTACK_DAMAGE)!!
            .invokeMethod<Void>("a", damage)
        // add goal selector
        addGoalAi(entity, 2, PathfinderGoalMeleeAttack(entityInsentient as EntityCreature, speed, false))
        // add target selector
        addTargetAi(entity, 1, PathfinderGoalHurtByTarget(entityInsentient))
        addTargetAi(entity, 2, PathfinderGoalNearestAttackableTarget(entityInsentient, EntityHuman::class.java, true))
    }

    override fun sendResourcePack(player: Player) {
        if (MinecraftVersion.isUniversal) {
            player.sendPacket(PacketPlayOutResourcePackSend::class.java.unsafeInstance().also {
                it.setProperty("url", Settings.resUrl)
                it.setProperty("hash", Settings.resHash)
                it.setProperty("required", false)
            })
        } else {
            player.sendPacket(PacketPlayOutResourcePackSend::class.java.unsafeInstance().also {
                it.setProperty("a", Settings.resUrl)
                it.setProperty("b", Settings.resHash)
            })
        }
    }
}