package io.github.itsflicker.fltools

import io.github.itsflicker.fltools.module.Void
import org.bukkit.generator.ChunkGenerator
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.module.configuration.createLocal
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin
import taboolib.platform.BukkitWorldGenerator

/**
 * FlTools
 * io.github.itsflicker.fltools
 *
 * @author wlys
 * @since 2021/7/31 21:42
 */
object FlTools : Plugin(), BukkitWorldGenerator {

    val plugin by lazy { BukkitPlugin.getInstance() }

    val local by lazy { createLocal("data.yml") }

    override fun onEnable() {
        Metrics(12296, plugin.description.version, Platform.BUKKIT)
        local
    }

    override fun getDefaultWorldGenerator(worldName: String, name: String?): ChunkGenerator {
        return Void()
    }
}