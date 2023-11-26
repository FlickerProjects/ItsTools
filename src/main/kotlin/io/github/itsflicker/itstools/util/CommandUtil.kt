package io.github.itsflicker.itstools.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandContext
import taboolib.platform.util.onlinePlayers
import kotlin.reflect.KProperty

val allSymbol = listOf("*")

operator fun CommandContext<*>.getValue(thisRef: Any?, property: KProperty<*>): String {
    return get(property.name)
}

fun CommandContext<*>.playerFor(id: String = "player", action: (Player) -> Unit) {
    val text = get(id)
    if (text == "*") {
        onlinePlayers.forEach { action(it) }
    } else {
        Bukkit.getPlayer(text)?.let { action(it) }
    }
}