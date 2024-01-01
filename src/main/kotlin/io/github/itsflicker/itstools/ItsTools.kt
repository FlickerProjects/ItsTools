package io.github.itsflicker.itstools

import com.willfp.eco.core.items.Items
import ink.ptms.sandalphon.Sandalphon
import io.github.itsflicker.itstools.module.feature.IPInfo
import io.github.itsflicker.itstools.module.feature.Void
import io.github.itsflicker.itstools.module.integration.itemsadder.ItemsAdderItemAPI
import io.github.itsflicker.itstools.module.integration.zaphkiel.ZaphkielItemProvider
import io.github.itsflicker.itstools.util.isEcoHooked
import io.github.itsflicker.itstools.util.isItemsAdderHooked
import io.github.itsflicker.itstools.util.isSandalphonHooked
import io.github.itsflicker.itstools.util.isZaphkielHooked
import org.bukkit.Bukkit
import org.bukkit.generator.ChunkGenerator
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.toObject
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitWorldGenerator
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.onlinePlayers

/**
 * ItsTools
 * io.github.itsflicker.itstools
 *
 * @author wlys
 * @since 2021/7/31 21:42
 */
@RuntimeDependencies(
    RuntimeDependency(value = "com.fasterxml.jackson.core:jackson-core:2.13.3", transitive = false),
    RuntimeDependency(value = "com.fasterxml.jackson.core:jackson-annotations:2.13.3", transitive = false),
    RuntimeDependency(value = "com.fasterxml.jackson.core:jackson-databind:2.13.3", transitive = false)
)
object ItsTools : Plugin(), BukkitWorldGenerator {

    @Config(autoReload = true)
    lateinit var config: Configuration
        private set

    override fun onLoad() {
        config.onReload { reload() }
        reload()
    }

    override fun onEnable() {
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(bukkitPlugin, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(bukkitPlugin, "BungeeCord")
        }
    }

    override fun onActive() {
        Metrics(12296, pluginVersion, Platform.BUKKIT)

        if (isEcoHooked && isZaphkielHooked) {
            Items.registerItemProvider(ZaphkielItemProvider())
        }
        if (isSandalphonHooked && isItemsAdderHooked) {
            Sandalphon.registerItemAPI(ItemsAdderItemAPI())
        }

        onlinePlayers.forEach { IPInfo.cacheFromCloud(it) }
    }

    override fun onDisable() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(bukkitPlugin)
    }

    override fun getDefaultWorldGenerator(worldName: String, name: String?): ChunkGenerator {
        return Void()
    }

    fun reload() {
        conf = config.toObject(false)
    }

}