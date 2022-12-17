package io.github.itsflicker.itstools

import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import io.github.itsflicker.itstools.module.script.Reaction
import io.github.itsflicker.itstools.util.ArrayLikeConverter
import io.github.itsflicker.itstools.util.BaffleConverter
import taboolib.common5.Baffle
import taboolib.library.configuration.Conversion
import taboolib.library.configuration.Path
import java.util.concurrent.TimeUnit

/**
 * Settings
 * io.github.itsflicker.itstools
 *
 * @author wlys
 * @since 2021/8/6 14:33
 */

lateinit var conf: Config

class Config(
    val features: Features = Features(),
    val integrations: Integrations = Integrations(),
    val shortcuts: Shortcut = Shortcut(),
    val automatically_upload: AutomaticallyUpload = AutomaticallyUpload(),
    @Conversion(ResourcePack.ResourcePackConverter::class) val resource_packs: Map<String, ResourcePack> = emptyMap()
)

class Features(
    val replacing_seed: Long = 123456789L,
    val remove_damage_indicator_particles: Boolean = false
)

class Integrations(
    val eco: Boolean = true,
    @Path("ItemsAdder") val itemsAdder: Boolean = true,
    @Path("Oraxen") val oraxen: Boolean = true,
    @Path("Sandalphon") val sandalphon: Boolean = true,
    @Path("Zaphkiel") val zaphkiel: Boolean = true
)

class Shortcut(
    @Conversion(BaffleConverter::class) val cooldown: Baffle = Baffle.of(500L, TimeUnit.MILLISECONDS),
    @Conversion(ArrayLikeConverter::class) val sneak_swap: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val down_swap: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val up_swap: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val sneak_drop: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val down_drop: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val up_drop: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val click_player: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val sneak_click_player: Reaction = Reaction.EMPTY
)

class AutomaticallyUpload(
    val cos: COSUpload = COSUpload(),
    val oss: OSSUpload = OSSUpload()
)

class COSUpload(
    val secret_id: String = "",
    val secret_key: String = "",
    val region: String = "",
    val bucket: String = "",
    val key: String = "pack.zip"
)

class OSSUpload(
    val endpoint: String = "",
    val access_key_id: String = "",
    val access_key_secret: String = "",
    val bucket: String = "",
    val key: String = ""
)