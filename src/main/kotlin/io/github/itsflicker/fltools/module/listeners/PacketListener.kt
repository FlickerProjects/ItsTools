package io.github.itsflicker.fltools.module.listeners

import io.github.itsflicker.fltools.Settings
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.kether.KetherShell
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketReceiveEvent
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
                e.packet.write("seed", Settings.replacingSeed)
            } else {
                e.packet.write("b", Settings.replacingSeed)
            }
        }
        if (e.packet.name == "PacketPlayOutRespawn") {
            if (MinecraftVersion.isUniversal) {
                e.packet.write("seed", Settings.replacingSeed)
            } else {
                e.packet.write("c", Settings.replacingSeed)
            }
        }
    }

    @SubscribeEvent
    fun receive(e: PacketReceiveEvent) {
        if (e.packet.name == "PacketPlayInResourcePackStatus") {
            if (MinecraftVersion.isUniversal) {
                when (e.packet.read<Any>("action")?.toString()) {
                    "SUCCESSFULLY_LOADED" -> KetherShell.eval(Settings.rpLoaded, sender = adaptPlayer(e.player))
                    "DECLINED" -> KetherShell.eval(Settings.rpDeclined, sender = adaptPlayer(e.player))
                    "FAILED_DOWNLOAD" -> KetherShell.eval(Settings.rpFailedDownload, sender = adaptPlayer(e.player))
                    "ACCEPTED" -> KetherShell.eval(Settings.rpAccepted, sender = adaptPlayer(e.player))
                    null -> error("ResourcePackStatus shouldn't be null!")
                }
            }
        }
    }
}