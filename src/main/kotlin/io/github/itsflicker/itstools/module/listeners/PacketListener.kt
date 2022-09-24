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
object PacketListener {

    @SubscribeEvent
    fun send(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutLogin") {
            if (majorLegacy >= 11900) {
                e.packet.source.invokeMethod<Void>("seed", conf.replacing_seed)
            }
            else if (majorLegacy >= 11700) {
                e.packet.write("seed", conf.replacing_seed)
            } else {
                e.packet.write("b", conf.replacing_seed)
            }
        }
        if (e.packet.name == "PacketPlayOutRespawn") {
            if (majorLegacy >= 11900) {
                e.packet.source.invokeMethod<Void>("seed", conf.replacing_seed)
            }
            else if (majorLegacy >= 11700) {
                e.packet.write("seed", conf.replacing_seed)
            } else {
                e.packet.write("b", conf.replacing_seed)
            }
        }
    }

}