package io.github.itsflicker.fltools.module.resourcepack

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
    val onRemoved: String?,
    val permission: String?
) {

    companion object {

        val selected = mutableMapOf<UUID, ResourcePack>()

    }
}