package io.github.itsflicker.fltools.module.resourcepack

import taboolib.common.util.asList
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.ConfigNodeTransfer
import java.util.*

/**
 * @author wlys
 * @since 2022/6/29 12:32
 */
class ResourcePack(
    val id: String,
    val url: String,
    val hash: String,
    val onLoaded: String?,
    val onDeclined: String?,
    val onFailedDownload: String?,
    val onAccepted: String?,
    val onRemoved: String?
) {

    companion object {

        @ConfigNode(value = "ResourcePack")
        val packs = ConfigNodeTransfer<List<Map<*, *>>, Map<String, ResourcePack>> {
            associate {
                val id = it["id"]!!.toString()
                val url = it["url"]?.toString() ?: ""
                val hash = it["hash"]?.toString() ?: ""
                val onLoaded = it["onLoaded"]?.asList()?.joinToString("\n")
                val onDeclined = it["onDeclined"]?.asList()?.joinToString("\n")
                val onFailedDownload = it["onFailedDownload"]?.asList()?.joinToString("\n")
                val onAccepted = it["onAccepted"]?.asList()?.joinToString("\n")
                val onRemoved = it["onRemoved"]?.asList()?.joinToString("\n")
                id to ResourcePack(id, url, hash, onLoaded, onDeclined, onAccepted, onFailedDownload, onRemoved)
            }
        }

        val selected = mutableMapOf<UUID, ResourcePack>()

    }
}