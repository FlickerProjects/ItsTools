package io.github.itsflicker.itstools.api

import net.minecraft.network.protocol.game.PacketPlayOutResourcePackSend
import net.minecraft.world.entity.EntityCreature
import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.EntityLiving
import net.minecraft.world.entity.ai.attributes.AttributeBase
import net.minecraft.world.entity.ai.attributes.AttributeMapBase
import net.minecraft.world.entity.ai.attributes.AttributeModifiable
import net.minecraft.world.entity.ai.attributes.GenericAttributes
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.ai.pathfinderExecutor
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsClass
import taboolib.module.nms.sendPacket

/**
 * NMSImpl
 * io.github.itsflicker.itstools.api
 *
 * @author wlys
 * @since 2021/8/3 14:59
 */
class NMSImpl : NMS() {

    override fun addGoalAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        (pathfinderExecutor.getEntityInsentient(entity) as? EntityInsentient)?.goalSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun addTargetAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        (pathfinderExecutor.getEntityInsentient(entity) as? EntityInsentient)?.targetSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    override fun makeMeleeHostile(entity: LivingEntity, damage: Double?, speed: Double, priority: Int, type: String, followingTargetEvenIfNotSeen: Boolean) {
        val entityInsentient = pathfinderExecutor.getEntityInsentient(entity) as EntityInsentient
        if (damage != null) {
            val map = entityInsentient.invokeMethod<AttributeMapBase>("getAttributes")!!
            // add generic:attack_damage attribute
            map.getProperty<HashMap<AttributeBase, AttributeModifiable>>("b")!![GenericAttributes.ATTACK_DAMAGE] =
                AttributeModifiable(GenericAttributes.ATTACK_DAMAGE) { map.invokeMethod<Void>("a", it) }
            // set entity damage
            entityInsentient.invokeMethod<AttributeModifiable>("getAttribute", GenericAttributes.ATTACK_DAMAGE)!!
                .invokeMethod<Void>("a", damage)
        }
        // add goal selector
        addGoalAi(entity, priority, PathfinderGoalMeleeAttack(entityInsentient as EntityCreature, speed, followingTargetEvenIfNotSeen))
        // add target selector
        addTargetAi(entity, 1, PathfinderGoalHurtByTarget(entityInsentient))
        addTargetAi(entity, 2, PathfinderGoalNearestAttackableTarget(entityInsentient, nmsClass(type).asSubclass(EntityLiving::class.java), true))
    }

    override fun sendResourcePack(player: Player, url: String, hash: String) {
        if (MinecraftVersion.isUniversal) {
            player.sendPacket(PacketPlayOutResourcePackSend::class.java.unsafeInstance().also {
                it.setProperty("url", url)
                it.setProperty("hash", hash)
                it.setProperty("required", false)
            })
        } else {
            player.sendPacket(PacketPlayOutResourcePackSend::class.java.unsafeInstance().also {
                it.setProperty("a", url)
                it.setProperty("b", hash)
            })
        }
    }
}