package io.github.itsflicker.itstools.util

import io.github.itsflicker.itstools.api.NMS
import taboolib.common.util.asList
import taboolib.common5.Baffle
import taboolib.library.configuration.Converter
import taboolib.module.nms.nmsProxy
import java.util.concurrent.TimeUnit

val nms = nmsProxy<NMS>()

class BaffleConverter : Converter<Baffle, Long> {
    override fun convertToField(value: Long): Baffle {
        return Baffle.of(value, TimeUnit.MILLISECONDS)
    }

    override fun convertFromField(value: Baffle): Long {
        error("Not supported.")
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