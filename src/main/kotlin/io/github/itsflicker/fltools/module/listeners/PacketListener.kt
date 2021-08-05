package io.github.itsflicker.fltools.module.listeners

import io.github.itsflicker.fltools.FlTools
import taboolib.common.platform.SubscribeEvent
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketSendEvent

/**
 * PacketListener
 * io.github.itsflicker.fltools.module.listeners
 *
 * @author wlys
 * @since 2021/8/4 17:07
 */
object PacketListener {

    @SubscribeEvent
    fun send(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutLogin") {
            if (MinecraftVersion.isUniversal) {
                e.packet.write("seed", FlTools.replacingSeed)
            }
        }
        if (e.packet.name == "PacketPlayOutRespawn") {
            if (MinecraftVersion.isUniversal) {
                e.packet.write("seed", FlTools.replacingSeed)
            }
        }
    }
}