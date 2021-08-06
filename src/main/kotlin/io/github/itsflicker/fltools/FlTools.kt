package io.github.itsflicker.fltools

import io.github.itsflicker.fltools.Settings.config
import io.github.itsflicker.fltools.module.Void
import io.github.itsflicker.fltools.module.listeners.Listeners
import org.bukkit.generator.ChunkGenerator
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common5.FileWatcher
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.SecuredFile
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

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    override fun onEnable() {
        // bstats metrics
        Metrics(12296, plugin.description.version, Platform.BUKKIT)
        // config watcher
        FileWatcher.INSTANCE.addSimpleListener(config.file) {
            onReload()
        }
    }

    override fun getDefaultWorldGenerator(worldName: String, name: String?): ChunkGenerator {
        return Void()
    }

    private fun onReload() {
        config.reload()
        Listeners.reload()
    }
}