package io.github.itsflicker.itstools.util

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import ink.ptms.zaphkiel.taboolib.common.reflect.Reflex.Companion.getProperty
import io.github.itsflicker.itstools.api.NMS
import io.github.itsflicker.itstools.conf
import org.bukkit.Bukkit
import taboolib.common.util.asList
import taboolib.common.util.unsafeLazy
import taboolib.common5.Baffle
import taboolib.common5.Baffle.BaffleCounter
import taboolib.common5.Baffle.BaffleTime
import taboolib.common5.util.parseMillis
import taboolib.library.configuration.Converter
import taboolib.module.nms.nmsProxy
import java.util.concurrent.TimeUnit

val nms = nmsProxy<NMS>()
private val jsonParser = JsonParser()

internal val isEcoHooked by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("eco") && conf.integrations.eco }
internal val isItemsAdderHooked by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("ItemsAdder") && conf.integrations.itemsAdder }
internal val isOraxenHooked by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("Oraxen") && conf.integrations.oraxen }
internal val isSandalphonHooked by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("Sandalphon") && conf.integrations.sandalphon }
internal val isZaphkielHooked by unsafeLazy { Bukkit.getPluginManager().isPluginEnabled("Zaphkiel") && conf.integrations.zaphkiel }

fun String.parseJson(): JsonElement = jsonParser.parse(this)!!

class BaffleConverter : Converter<Baffle, String> {
    override fun convertToField(value: String): Baffle {
        return if (value.endsWith('*')) {
            Baffle.of(value.removeSuffix("*").toInt())
        } else {
            Baffle.of(value.parseMillis(), TimeUnit.MILLISECONDS)
        }
    }

    override fun convertFromField(value: Baffle): String {
        return when (value) {
            is BaffleCounter -> value.getProperty<Int>("count")!!.toString() + "*"
            is BaffleTime -> TimeUnit.MILLISECONDS.toSeconds(value.getProperty<Long>("millis")!!).toString() + "s"
            else -> error("out of case")
        }
    }
}

class ArrayLikeConverter : Converter<Array<String>, Any> {
    override fun convertToField(value: Any): Array<String> {
        return value.asList().toTypedArray()
    }

    override fun convertFromField(value: Array<String>): Any {
        return if (value.size == 1) value[0] else value.toList()
    }
}