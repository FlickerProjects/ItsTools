package io.github.itsflicker.itstools.api

import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.module.chat.ComponentText

/**
 * NMS
 * io.github.itsflicker.itstools.api
 *
 * @author wlys
 * @since 2021/8/3 14:56
 */
abstract class NMS {

    abstract fun getTargetEntity(entity: LivingEntity): LivingEntity?

    abstract fun setTargetEntity(entity: LivingEntity, target: LivingEntity?)

    abstract fun makeMeleeHostile(
        entity: LivingEntity,
        damage: Double? = null,
        speed: Double = 1.0,
        priority: Int = 2,
        type: String = "EntityHuman",
        followingTargetEvenIfNotSeen: Boolean = false
    )

    abstract fun sendResourcePack(player: Player, url: String, hash: String)

    abstract fun addBossBar(player: Player, name: ComponentText, color: BarColor, style: BarStyle)

    abstract fun removeBossBar(player: Player)

    abstract fun updateBossBar(player: Player, progress: Float)

    abstract fun updateBossBar(player: Player, name: ComponentText)

    abstract fun updateBossBar(player: Player, color: BarColor)

}