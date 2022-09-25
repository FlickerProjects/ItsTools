package io.github.itsflicker.itstools.module.script

import io.github.itsflicker.itstools.module.script.js.JavaScriptAgent
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.kether.KetherShell
import taboolib.module.kether.runKether

/**
 * @author wlys
 * @since 2022/6/15 18:05
 */
@JvmInline
value class Reaction(val script: Array<String>) {

    fun eval(player: Player, vararg additions: Pair<String, Any>): Any? {
        return if (script.isEmpty()) null
        else eval(player, script, *additions)
    }

    companion object {

        val EMPTY = Reaction(emptyArray())

        fun eval(player: Player, script: Array<String>, vararg additions: Pair<String, Any>): Any? {
            val (isJavaScript, js) = JavaScriptAgent.serialize(script[0])
            return if (isJavaScript) JavaScriptAgent.eval(player, js!!, *additions).get()
            else runKether { KetherShell.eval(script.toList(), sender = adaptPlayer(player), vars = KetherShell.VariableMap(additions.toMap())).get() }
        }

    }
}