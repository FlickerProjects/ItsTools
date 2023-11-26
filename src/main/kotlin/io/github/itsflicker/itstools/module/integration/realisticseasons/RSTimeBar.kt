package io.github.itsflicker.itstools.module.integration.realisticseasons

import io.github.itsflicker.itstools.ItsTools
import io.github.itsflicker.itstools.util.nms
import me.casperge.realisticseasons.api.SeasonsAPI
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.unsafeLazy
import taboolib.module.chat.Components
import taboolib.module.chat.component
import taboolib.module.configuration.ConfigNode
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.onlinePlayers

object RSTimeBar {

    val created = mutableMapOf<String, BarColor>()
    private val enabled by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")
            && ItsTools.config.getBoolean("integrations.RealisticSeasons.timebar", false) }
    private val api by unsafeLazy { SeasonsAPI.getInstance() }

    @ConfigNode("integrations.RealisticSeasons.text")
    lateinit var text: String
        private set

    @Awake(LifeCycle.ACTIVE)
    internal fun init() {
        if (enabled) {
            submitAsync(period = ItsTools.config.getLong("integrations.RealisticSeasons.refresh_interval", 20L)) {
                onlinePlayers.forEach {
                    update(it)
                }
            }
        }
    }

    private fun update(player: Player) {
        val world = player.world
        val season = api.getSeason(world)
        val color = ItsTools.config.getString("integrations.RealisticSeasons.color.${season.name.lowercase()}")?.let { BarColor.valueOf(it) }
        if (color == null) {
            if (player.name in created) {
                nms.removeBossBar(player)
                created.remove(player.name)
            }
            return
        }
        if (player.name !in created) {
            nms.addBossBar(player, Components.empty(), BarColor.WHITE, BarStyle.SOLID)
            created[player.name] = BarColor.WHITE
        }
        if (created[player.name] != color) {
            nms.updateBossBar(player, color)
        }
        val progress = (api.getHours(world) * 3600 + api.getMinutes(world) * 60 + api.getSeconds(world)) / 86400F
        nms.updateBossBar(player, progress)
        nms.updateBossBar(player, text.replacePlaceholder(player).component().buildColored())
    }

}