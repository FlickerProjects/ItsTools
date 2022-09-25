package io.github.itsflicker.itstools.module.resourcepack

import com.electronwill.nightconfig.core.UnmodifiableConfig
import io.github.itsflicker.itstools.module.script.Condition
import io.github.itsflicker.itstools.module.script.Reaction
import io.github.itsflicker.itstools.util.ArrayLikeConverter
import taboolib.library.configuration.Conversion
import taboolib.library.configuration.Converter
import taboolib.library.configuration.ObjectConverter
import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.Configuration
import java.util.*

/**
 * @author wlys
 * @since 2022/6/29 12:32
 */
class ResourcePack(
    val id: String = "",
    val url: String = "http://cdn.moep.tv/files/Empty.zip",
    val hash: String = "01517226212d27586ea0c5d6aff1aa5492dd2484",
    val condition: Condition = Condition.EMPTY,
    @Conversion(ArrayLikeConverter::class) val loaded: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val declined: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val failed: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val accepted: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val removed: Reaction = Reaction.EMPTY
) {

    class ResourcePackConverter : Converter<Map<String, ResourcePack>, UnmodifiableConfig> {
        override fun convertToField(value: UnmodifiableConfig): Map<String, ResourcePack> {
            return value.valueMap().entries.associate { (k, v) ->
                val instance = ResourcePack(k)
                ObjectConverter(false).toObject((v as UnmodifiableConfig), instance)
                k to instance
            }
        }

        override fun convertFromField(value: Map<String, ResourcePack>): UnmodifiableConfig {
            return (Configuration.fromMap(value) as ConfigSection).root
        }
    }

    companion object {

        val selected = mutableMapOf<UUID, ResourcePack>()
    }
}