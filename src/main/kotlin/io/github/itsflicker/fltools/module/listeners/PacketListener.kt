package io.github.itsflicker.fltools.module.listeners

import io.github.itsflicker.fltools.FlTools
import io.github.itsflicker.fltools.Settings
import net.minecraft.network.protocol.game.PacketPlayInResourcePackStatus
import net.minecraft.network.protocol.game.PacketPlayInResourcePackStatus.EnumResourcePackStatus.*
import taboolib.common.platform.SubscribeEvent
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
                when (e.packet.read<PacketPlayInResourcePackStatus.EnumResourcePackStatus>("action")) {
                    SUCCESSFULLY_LOADED -> TODO()
                    DECLINED -> TODO()
                    FAILED_DOWNLOAD -> TODO()
                    ACCEPTED -> TODO()
                    null -> error("ResourcePackStatus shouldn't be null!")
                }
            }
        }
    }
}