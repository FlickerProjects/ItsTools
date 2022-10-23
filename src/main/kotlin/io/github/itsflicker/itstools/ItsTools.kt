package io.github.itsflicker.itstools

import com.willfp.eco.core.items.Items
import io.github.itsflicker.itstools.module.Void
import io.github.itsflicker.itstools.module.integrations.zaphkiel.ZaphkielItemProvider
import org.bukkit.Bukkit
import org.bukkit.generator.ChunkGenerator
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.toObject
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitWorldGenerator

/**
 * ItsTools
 * io.github.itsflicker.itstools
 *
 * @author wlys
 * @since 2021/7/31 21:42
 */
object ItsTools : Plugin(), BukkitWorldGenerator {

    @Config(autoReload = true)
    lateinit var config: Configuration
        private set

    private val isZaphkielHooked by lazy { Bukkit.getPluginManager().isPluginEnabled("Zaphkiel") }

    private val isEcoHooked by lazy { Bukkit.getPluginManager().isPluginEnabled("eco") }

    override fun onLoad() {
        config.onReload { reload() }
        reload()
    }

    override fun onEnable() {
        Metrics(12296, pluginVersion, Platform.BUKKIT)

        if (isEcoHooked) {
            if (isZaphkielHooked) {
                Items.registerItemProvider(ZaphkielItemProvider())
            }
        }
    }

    override fun getDefaultWorldGenerator(worldName: String, name: String?): ChunkGenerator {
        return Void
    }

    fun reload() {
        conf = config.toObject(false)
    }

}