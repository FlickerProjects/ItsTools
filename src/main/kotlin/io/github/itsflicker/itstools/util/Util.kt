package io.github.itsflicker.itstools.util

import io.github.itsflicker.itstools.api.NMS
import io.github.itsflicker.itstools.module.script.Condition
import io.github.itsflicker.itstools.module.script.Reaction
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

class ConditionConverter : Converter<Condition, String> {

    override fun convertToField(value: String): Condition {
        return Condition(value)
    }

    override fun convertFromField(value: Condition): String {
        return value.script
    }

}

class ReactionConverter : Converter<Reaction, Any> {

    override fun convertToField(value: Any): Reaction {
        return Reaction(value.asList())
    }

    override fun convertFromField(value: Reaction): List<String> {
        return value.script
    }

}