package io.github.itsflicker.itstools.module.resourcepack

import com.electronwill.nightconfig.core.UnmodifiableConfig
import dev.lone.itemsadder.api.ItemsAdder
import io.github.itsflicker.itstools.conf
import io.github.itsflicker.itstools.module.script.Condition
import io.github.itsflicker.itstools.module.script.Reaction
import io.github.itsflicker.itstools.util.ArrayLikeConverter
import io.github.itsflicker.itstools.util.isItemsAdderHooked
import org.bukkit.entity.Player
import taboolib.common.platform.function.warning
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
    val hash: String? = "01517226212d27586ea0c5d6aff1aa5492dd2484",
    val condition: Condition = Condition.EMPTY,
    val worlds: ArrayList<String> = ArrayList(),
    @Conversion(ArrayLikeConverter::class) val loaded: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val declined: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val failed: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val accepted: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val removed: Reaction = Reaction.EMPTY
) {

    class ResourcePackConverter : Converter<Map<String, ResourcePack>, UnmodifiableConfig> {
        override fun convertToField(value: UnmodifiableConfig): Map<String, ResourcePack> {
            return value.valueMap().entries.associate { (k, v) ->
                val instance = ResourcePack(id = k)
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

        fun send(player: Player, id: String) {
            val resourcePack = conf.resource_packs[id] ?: return
            selected.remove(player.uniqueId)?.removed?.eval(player)
            when {
                resourcePack.url.equals("itemsadder", ignoreCase = true) -> {
                    if (isItemsAdderHooked) {
                        ItemsAdder.applyResourcepack(player)
                    } else {
                        warning("ItemsAdder not loaded.")
                    }
                }
//                resourcePack.url.equals("oraxen", ignoreCase = true) -> {
//                    if (isOraxenHooked) {
//                        OraxenPlugin.get().uploadManager.sender.sendPack(player)
//                    } else {
//                        warning("Oraxen not loaded.")
//                    }
//                }
                else -> {
                    player.setResourcePack(resourcePack.url, resourcePack.hash?.toByteArray())
                }
            }
            selected[player.uniqueId] = resourcePack
        }

    }
}