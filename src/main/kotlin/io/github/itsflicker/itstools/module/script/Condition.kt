package io.github.itsflicker.itstools.module.script

import io.github.itsflicker.itstools.module.script.js.JavaScriptAgent
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.kether.KetherShell
import taboolib.module.kether.runKether

/**
 * @author Arasple
 * @since 2021/8/29 15:16
 */
@JvmInline
value class Condition(val script: String) {

    fun eval(player: Player): Boolean {
        return if (script.isEmpty()) true
        else eval(player, script)
    }

    companion object {

        fun eval(player: Player, script: String): Boolean {
            val (isJavaScript, js) = JavaScriptAgent.serialize(script)
            return if (isJavaScript) JavaScriptAgent.eval(player, js!!).thenApply { Coerce.toBoolean(it) }.get()
            else runKether { KetherShell.eval(script, sender = adaptPlayer(player)).thenApply { Coerce.toBoolean(it) }.get() } ?: false
        }
    }
}