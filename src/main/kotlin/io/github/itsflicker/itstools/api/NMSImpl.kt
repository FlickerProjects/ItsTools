package io.github.itsflicker.itstools.api

import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.network.protocol.game.PacketPlayOutBoss
import net.minecraft.server.level.BossBattleServer
import net.minecraft.world.BossBattle
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
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.craftbukkit.v1_20_R2.CraftServer
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.function.warning
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.ai.pathfinderExecutor
import taboolib.module.chat.ComponentText
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
@Suppress("unused")
class NMSImpl : NMS() {

    lateinit var bossBarCache: BossBattle

    override fun getTargetEntity(entity: LivingEntity): LivingEntity? {
        val entityInsentient = entity.getEntityInsentient() ?: return null
        val target = entityInsentient.target ?: return null
        return CraftEntity.getEntity((entity as CraftEntity).server as CraftServer, target) as? LivingEntity
    }

    override fun setTargetEntity(entity: LivingEntity, target: LivingEntity?) {
        val entityInsentient = entity.getEntityInsentient() ?: return
        // Unknown target reason, please report on the issue tracker
        entityInsentient.setProperty("target", target?.getEntityInsentient())
    }

    override fun makeMeleeHostile(entity: LivingEntity, damage: Double?, speed: Double, priority: Int, type: String, followingTargetEvenIfNotSeen: Boolean) {
        val entityInsentient = entity.getEntityInsentient() ?: return warning("${entity.type} is not EntityInsentient.")
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
            // TODO
//            player.sendPacket(PacketPlayOutResourcePackSend(url, hash, false, null))
        } else {
            player.sendPacket(net.minecraft.server.v1_16_R3.PacketPlayOutResourcePackSend(url, hash))
        }
    }

    override fun addBossBar(player: Player, name: ComponentText, color: BarColor, style: BarStyle) {
        if (!::bossBarCache.isInitialized) {
            bossBarCache = BossBattleServer(
                craftChatMessageFromComponent(name),
                BossBattle.BarColor.entries[color.ordinal],
                BossBattle.BarStyle.entries[style.ordinal]
            )
        }
        player.sendPacket(PacketPlayOutBoss.createAddPacket(bossBarCache))
    }

    override fun removeBossBar(player: Player) {
        if (!::bossBarCache.isInitialized) return
        player.sendPacket(PacketPlayOutBoss.createRemovePacket(bossBarCache.id))
    }

    override fun updateBossBar(player: Player, progress: Float) {
        if (!::bossBarCache.isInitialized) return
        bossBarCache.progress = progress
        player.sendPacket(PacketPlayOutBoss.createUpdateProgressPacket(bossBarCache))
    }

    override fun updateBossBar(player: Player, name: ComponentText) {
        if (!::bossBarCache.isInitialized) return
        bossBarCache.setName(craftChatMessageFromComponent(name))
        player.sendPacket(PacketPlayOutBoss.createUpdateNamePacket(bossBarCache))
    }

    override fun updateBossBar(player: Player, color: BarColor) {
        if (!::bossBarCache.isInitialized) return
        bossBarCache.setColor(BossBattle.BarColor.entries[color.ordinal])
        player.sendPacket(PacketPlayOutBoss.createUpdateStylePacket(bossBarCache))
    }

    private fun craftChatMessageFromComponent(component: ComponentText): IChatBaseComponent {
        return CraftChatMessage.fromJSON(component.toRawMessage())
    }

    private fun LivingEntity.getEntityInsentient(): EntityInsentient? {
        return pathfinderExecutor.getEntityInsentient(this) as? EntityInsentient
    }

    private fun addGoalAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        entity.getEntityInsentient()?.goalSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

    private fun addTargetAi(entity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        entity.getEntityInsentient()?.targetSelector?.addGoal(priority, pathfinderGoal as PathfinderGoal)
    }

}