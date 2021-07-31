package io.github.username.project

import taboolib.common.platform.Plugin
import taboolib.common.platform.info

object ExamplePlugin : Plugin() {

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
    }
}