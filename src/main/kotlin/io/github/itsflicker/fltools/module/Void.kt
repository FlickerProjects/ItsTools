package io.github.itsflicker.fltools.module

import org.bukkit.HeightMap
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import java.util.*

object Void : ChunkGenerator() {

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return true
    }

    override fun getFixedSpawnLocation(world: World, random: Random): Location {
        return Location(world, 0.0, 66.0, 0.0)
    }

    override fun getBaseHeight(worldInfo: WorldInfo, random: Random, x: Int, z: Int, heightMap: HeightMap): Int {
        return 64
    }

    override fun getDefaultBiomeProvider(worldInfo: WorldInfo): BiomeProvider? {
        return worldInfo.invokeMethod("vanillaBiomeProvider")
    }

    override fun shouldGenerateMobs(): Boolean {
        return true
    }

    override fun shouldGenerateStructures(): Boolean {
        return true
    }

}