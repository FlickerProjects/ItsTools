package io.github.itsflicker.itstools.module.feature

import io.github.itsflicker.itstools.util.parseJson
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.module.configuration.Type
import taboolib.module.configuration.createLocal
import taboolib.module.configuration.util.getMap
import java.io.BufferedInputStream
import java.net.URL
import java.util.*
import java.util.concurrent.CompletableFuture

class IPInfo(
    val country: String,
    val short_name: String,
    val province: String,
    val city: String,
    val area: String,
    val isp: String
) {

    companion object {

        val local by unsafeLazy { createLocal("ipcaches.json", type = Type.FAST_JSON) }
        val caches = mutableMapOf<UUID, IPInfo>()

        fun cacheFromCloud(player: Player, retryTime: Int = 3) {
            val ip = player.address?.address?.hostAddress ?: return
            val key = ip.replace('.', '-')
            if (key in local) {
                loadFromLocal(player.uniqueId, key)
                return
            }
            CompletableFuture.supplyAsync {
                URL("https://ip.useragentinfo.com/jsonp?ip=$ip")
                    .openConnection()
                    .also { it.connectTimeout = 30 * 1000 }
                    .getInputStream()
                    .use { inputStream ->
                        BufferedInputStream(inputStream).use { bufferedInputStream ->
                            val json = bufferedInputStream.bufferedReader().readText()
                                .removePrefix("callback(").removeSuffix(");").parseJson().asJsonObject
                            if (json["code"].asInt != 200) {
                                error("Query fail.")
                            }
                            local[key] = mapOf(
                                "country" to json["country"].asString,
                                "short_name" to json["short_name"].asString,
                                "province" to json["province"].asString,
                                "city" to json["country"].asString,
                                "area" to json["area"].asString,
                                "isp" to json["isp"].asString,
                            )
                            loadFromLocal(player.uniqueId, key)
                        }
                    }
            }.whenCompleteAsync { _, e ->
                if (e != null && retryTime > 0) {
                    e.printStackTrace()
                    cacheFromCloud(player, retryTime - 1)
                }
            }
        }

        fun loadFromLocal(uuid: UUID, ip: String) {
            val map = local.getMap<String, String>(ip)
            caches[uuid] = IPInfo(
                map["country"]!!,
                map["short_name"]!!,
                map["province"]!!,
                map["city"]!!,
                map["area"]!!,
                map["isp"]!!
            )
        }

    }

}