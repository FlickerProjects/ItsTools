package io.github.itsflicker.itstools

import io.github.itsflicker.itstools.module.resourcepack.ResourcePack
import io.github.itsflicker.itstools.module.script.Reaction
import io.github.itsflicker.itstools.util.ArrayLikeConverter
import io.github.itsflicker.itstools.util.BaffleConverter
import taboolib.common5.Baffle
import taboolib.library.configuration.Conversion
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
    val replacing_seed: Long = 123456789L,
    val shortcuts: Shortcut = Shortcut(),
    val automatically_upload: AutomaticallyUpload = AutomaticallyUpload(),
    @Conversion(ResourcePack.ResourcePackConverter::class) val resource_packs: Map<String, ResourcePack> = emptyMap()
)

class Shortcut(
    @Conversion(BaffleConverter::class) val cooldown: Baffle = Baffle.of(500L, TimeUnit.MILLISECONDS),
    @Conversion(ArrayLikeConverter::class) val sneak_swap: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val down_swap: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val up_swap: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val sneak_drop: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val down_drop: Reaction = Reaction.EMPTY,
    @Conversion(ArrayLikeConverter::class) val up_drop: Reaction = Reaction.EMPTY,
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

)