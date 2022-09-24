package io.github.itsflicker.itstools.module.resourcepack

import io.github.itsflicker.itstools.module.script.Condition
import io.github.itsflicker.itstools.module.script.Reaction
import io.github.itsflicker.itstools.util.ConditionConverter
import io.github.itsflicker.itstools.util.ReactionConverter
import taboolib.library.configuration.Conversion
import java.util.*

/**
 * @author wlys
 * @since 2022/6/29 12:32
 */
class ResourcePack(
    val url: String = "http://cdn.moep.tv/files/Empty.zip",
    val hash: String = "01517226212d27586ea0c5d6aff1aa5492dd2484",
    @Conversion(ConditionConverter::class) val condition: Condition? = null,
    @Conversion(ReactionConverter::class) val loaded: Reaction? = null,
    @Conversion(ReactionConverter::class) val declined: Reaction? = null,
    @Conversion(ReactionConverter::class) val failed: Reaction? = null,
    @Conversion(ReactionConverter::class) val accepted: Reaction? = null,
    @Conversion(ReactionConverter::class) val removed: Reaction? = null
) {

    companion object {

        val selected = mutableMapOf<UUID, ResourcePack>()

    }
}