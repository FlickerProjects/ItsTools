package io.github.itsflicker.itstools.module.integration.attributesystem

import com.skillw.attsystem.AttributeSystem
import com.skillw.attsystem.api.event.AttributeUpdateEvent
import com.skillw.attsystem.util.DefaultAttribute
import io.github.itsflicker.itstools.ItsTools
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.unsafeLazy

object VanillaArmorTransfer {

    private val enabled by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("AttributeSystem")
            && ItsTools.config.getBoolean("integrations.AttributeSystem.vanilla-armor-transfer", false) }

    @SubscribeEvent
    fun onUpdate(e: AttributeUpdateEvent.Pre) {
        if (!enabled) return
        val player = e.entity as? Player ?: return
        var armor = 0.0
        var toughness = 0.0
        player.inventory.armorContents.filterNotNull().forEach {
            armor += DefaultAttribute.getArmor(it.type)
            armor += it.enchantments.getOrDefault(Enchantment.PROTECTION_ENVIRONMENTAL, 0)
            toughness += DefaultAttribute.getArmorToughness(it.type)
        }
        e.data.register(
            "VANILLA-ARMOR-TRANSFER",
            AttributeSystem.readManager.read(listOf("护甲: +$armor", "盔甲韧性: +$toughness"))
        )
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onDamage(e: EntityDamageByEntityEvent) {
        if (enabled && e.entity is Player) {
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0)
        }
    }

}