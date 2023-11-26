package io.github.itsflicker.itstools.module.script.kether

import io.github.itsflicker.itstools.util.sendCommonPluginMessage
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.script

internal object KetherActions {

    @KetherParser(["server", "connect", "bungee"], shared = true)
    fun actionServer() = combinationParser {
        it.group(text()).apply(it) { str ->
            now { sendCommonPluginMessage(script().sender?.cast() ?: error("No sender"), "Connect", str) }
        }
    }

}