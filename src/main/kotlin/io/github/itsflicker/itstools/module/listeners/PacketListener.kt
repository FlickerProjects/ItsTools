package io.github.itsflicker.itstools.module.listeners

import io.github.itsflicker.itstools.conf
import org.bukkit.inventory.CraftingInventory
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.MinecraftVersion.majorLegacy
import taboolib.module.nms.PacketReceiveEvent
import taboolib.module.nms.PacketSendEvent

/**
 * PacketListener
 * io.github.itsflicker.itstools.module.listeners
 *
 * @author wlys
 * @since 2021/8/4 17:07
 */
@Suppress("unused")
object PacketListener {

    @SubscribeEvent
    fun patch0(e: PacketReceiveEvent) {
        if (e.packet.name == "PacketPlayInUseItem" || e.packet.name == "PacketPlayInUseEntity" || e.packet.name == "PacketPlayInArmAnimation") {
            if (e.player.openInventory.topInventory !is CraftingInventory) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    fun onSend(e: PacketSendEvent) {
        when (e.packet.name) {
            "PacketPlayOutLogin" -> {
                if (majorLegacy >= 11700) {
//                    e.packet.write("seed", conf.features.replacing_seed)
                } else {
                    e.packet.write("b", conf.features.replacing_seed)
                }
            }
            "PacketPlayOutRespawn" -> {
                if (majorLegacy >= 11700) {
//                    e.packet.write("seed", conf.features.replacing_seed)
                } else {
                    e.packet.write("b", conf.features.replacing_seed)
                }
            }
            "PacketPlayOutWorldParticles" -> {
                if (majorLegacy >= 11800) {
                    if (
                        e.packet.read<Any>("particle")!!.invokeMethod<String>("writeToString")!! == "minecraft:damage_indicator"
                        && conf.features.remove_damage_indicator_particles
                    ) {
                        e.isCancelled = true
                    }
                } else {
                    if (
                        e.packet.read<Any>("j")!!.invokeMethod<String>("a")!! == "minecraft:damage_indicator"
                        && conf.features.remove_damage_indicator_particles
                    ) {
                        e.isCancelled = true
                    }
                }
            }
        }
    }

}