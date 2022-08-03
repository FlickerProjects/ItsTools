package io.github.itsflicker.fltools

import com.willfp.eco.core.items.Items
import io.github.itsflicker.fltools.module.Void
import io.github.itsflicker.fltools.module.integrations.zaphkiel.ZaphkielItemProvider
import org.bukkit.Bukkit
import org.bukkit.generator.ChunkGenerator
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitWorldGenerator

/**
 * FlTools
 * io.github.itsflicker.fltools
 *
 * @author wlys
 * @since 2021/7/31 21:42
 */
object FlTools : Plugin(), BukkitWorldGenerator {

    private val isZaphkielHooked by lazy { Bukkit.getPluginManager().isPluginEnabled("Zaphkiel") }

    private val isEcoHooked by lazy { Bukkit.getPluginManager().isPluginEnabled("eco") }

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
}