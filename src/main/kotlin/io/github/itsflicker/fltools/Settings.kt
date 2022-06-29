package io.github.itsflicker.fltools

import taboolib.common5.Baffle
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.ConfigNodeTransfer
import taboolib.module.configuration.Configuration
import java.util.concurrent.TimeUnit

/**
 * Settings
 * io.github.itsflicker.fltools
 *
 * @author wlys
 * @since 2021/8/6 14:33
 */
object Settings {

    @Config(autoReload = true)
    lateinit var conf: Configuration
        private set

    @ConfigNode(value = "replacing-seed")
    var replacingSeed = 123456789
        private set

    @ConfigNode(value = "Swap-Shortcut.cooldown")
    val shortcutCoolDown = ConfigNodeTransfer<Long, Baffle> { Baffle.of(this, TimeUnit.MILLISECONDS) }

    @ConfigNode(value = "Swap-Shortcut.whenSneakF")
    var sneakF = ""
        private set

    @ConfigNode(value = "Swap-Shortcut.whenLookDownF")
    var lookDownF = ""
        private set

    @ConfigNode(value = "Swap-Shortcut.whenLookUpF")
    var lookUpF = ""
        private set

    @ConfigNode(value = "Swap-Shortcut.whenSneakQ")
    var sneakQ = ""
        private set

    @ConfigNode(value = "Swap-Shortcut.whenLookDownQ")
    var lookDownQ = ""
        private set

    @ConfigNode(value = "Swap-Shortcut.whenLookUpQ")
    var lookUpQ = ""
        private set
}