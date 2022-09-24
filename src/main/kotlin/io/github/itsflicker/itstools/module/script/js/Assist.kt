package io.github.itsflicker.itstools.module.script.js

import org.bukkit.entity.Player
import taboolib.platform.compat.replacePlaceholder

class Assist {

    companion object {

        val INSTANCE = Assist()
    }

    fun parsePlaceholders(player: Player, string: String): String {
        return string.replacePlaceholder(player)
    }

}