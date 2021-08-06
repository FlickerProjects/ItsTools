package io.github.itsflicker.fltools

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.SecuredFile

/**
 * Settings
 * io.github.itsflicker.fltools
 *
 * @author wlys
 * @since 2021/8/6 14:33
 */
object Settings {

    @Config(migrate = true)
    lateinit var config: SecuredFile

    @ConfigNode(value = "replacing-seed")
    var replacingSeed = 1145141919810
        private set

    @ConfigNode(value = "Swap-Shortcut.cooldown")
    var swapCoolDown = 500L
        private set

    @ConfigNode(bind = "Swap-Shortcut.whenSneak")
    var sneakF = ""
        private set

    @ConfigNode(bind = "Swap-Shortcut.whenLookDown")
    var lookDownF = ""
        private set

    @ConfigNode(bind = "Swap-Shortcut.whenLookUp")
    var lookUpF = ""
        private set

    @ConfigNode(bind = "ResourcePack.url")
    var resUrl = ""
        private set

    @ConfigNode(bind = "ResourcePack.hash")
    var resHash = "null"
        private set

    @ConfigNode(bind = "ResourcePack.onLoaded")
    var rpLoaded = listOf("")
        private set

    @ConfigNode(bind = "ResourcePack.onDeclined")
    var rpDeclined = listOf("")
        private set

    @ConfigNode(bind = "ResourcePack.onAccepted")
    var rpAccepted = listOf("")
        private set

    @ConfigNode(bind = "ResourcePack.onFailedDownload")
    var rpFailedDownload = listOf("")
        private set
}