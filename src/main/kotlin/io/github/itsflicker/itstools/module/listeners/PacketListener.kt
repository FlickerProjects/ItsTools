package io.github.itsflicker.itstools.module.listeners

import io.github.itsflicker.itstools.conf
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.MinecraftVersion.majorLegacy
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
    fun onSend(e: PacketSendEvent) {
        when (e.packet.name) {
            "PacketPlayOutLogin" -> {
                if (majorLegacy < 11700) {
                    e.packet.write("b", conf.features.replacing_seed)
                }
            }
            "PacketPlayOutRespawn" -> {
                if (majorLegacy < 11700) {
                    e.packet.write("b", conf.features.replacing_seed)
                }
            }
            "PacketPlayOutWorldParticles" -> {
                if (majorLegacy >= 11800) {
                    if (
                        conf.features.remove_damage_indicator_particles
                        && e.packet.read<Any>("particle")!!.invokeMethod<String>("writeToString")!! == "minecraft:damage_indicator"
                    ) {
                        e.isCancelled = true
                    }
                } else {
                    if (
                        conf.features.remove_damage_indicator_particles
                        && e.packet.read<Any>("j")!!.invokeMethod<String>("a")!! == "minecraft:damage_indicator"
                    ) {
                        e.isCancelled = true
                    }
                }
            }
        }
    }

}