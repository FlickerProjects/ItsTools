package io.github.itsflicker.itstools.module.feature

import io.github.itsflicker.itstools.module.command.CommandOperation
import io.github.itsflicker.itstools.module.feature.DebugItem.Mode.*
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Baffle
import taboolib.library.xseries.XMaterial
import taboolib.module.ai.navigationMove
import taboolib.module.chat.colored
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.buildItem
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import taboolib.platform.util.modifyLore
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * @author wlys
 * @since 2022/4/23 22:19
 */
object DebugItem {

    val cooldown = Baffle.of(200L, TimeUnit.MILLISECONDS)

    val item = buildItem(XMaterial.BLAZE_ROD) {
        name = "&f&lItsTools&c Debug Item"
        lore.add("&7Current mode: &fGET_ENTITY_UUID")
        shiny()
        colored()
    }.also {
        makeDebugItem(it)
        setMode(it, GET_ENTITY_UUID)
    }

    enum class Mode(val index: Int) {

        GET_ENTITY_UUID(0),

        NAVIGATE(1),

        REMOVE_ARMOR_STAND(2)

    }

    val cache = ConcurrentHashMap<String, UUID>()

    fun getMode(item: ItemStack): Mode {
        val index = item.getItemTag()["mode"]?.asInt() ?: return GET_ENTITY_UUID
        return entries[index]
    }

    fun setMode(item: ItemStack, mode: Mode) {
        val component = item.getItemTag()
        component["mode"] = ItemTagData(mode.index)
        component.saveTo(item)
    }

    fun isDebugItem(item: ItemStack): Boolean {
        return item.isNotAir() && item.getItemTag()["itstools_debug_item"]?.asInt() == 1
    }

    fun makeDebugItem(item: ItemStack) {
        val component = item.getItemTag()
        component["itstools_debug_item"] = ItemTagData(1)
        component.saveTo(item)
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEntityEvent) {
        val entity = e.rightClicked as? LivingEntity ?: return
        val player = e.player
        val operation = CommandOperation.cacheOperations.getIfPresent(player.uniqueId)
        if (operation != null) {
            operation.accept(entity)
            CommandOperation.cacheOperations.invalidate(player.uniqueId)
            return
        }
        val item = player.inventory.itemInMainHand
        if (isDebugItem(item) && cooldown.hasNext(player.name)) {
            when (getMode(item)) {
                GET_ENTITY_UUID -> {
                    cache[player.name] = entity.uniqueId
                    player.sendMessage("&cUUID: &f${entity.uniqueId}".colored())
                }
                NAVIGATE -> {
                    cache[player.name] = entity.uniqueId
                    player.sendMessage("&f${entity.uniqueId} &cCached.".colored())
                }
                else -> Unit
            }
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand.also { if (it.isAir()) return }
        if (isDebugItem(item)) {
            val mode = getMode(item).index
            val newMode = if (mode < entries.size - 1) mode + 1 else 0
            setMode(item, entries[newMode].also {
                item.modifyLore {
                    set(0, "&7当前模式: &f${it.name}".colored())
                }
                adaptPlayer(player).sendActionBar("§cCurrent mode: ${it.name}")
            })
            e.isCancelled = true
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun e(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand
        if (isDebugItem(item)) {
            when (getMode(item)) {
                NAVIGATE -> {
                    if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
                        val entityUUID = cache[player.name] ?: return
                        val entity = Bukkit.getEntity(entityUUID) as? LivingEntity ?: return
                        entity.navigationMove(e.clickedBlock?.location ?: player.location, 1.0)
                        e.isCancelled = true
                    }
                }
                REMOVE_ARMOR_STAND -> {
                    if (e.action == Action.RIGHT_CLICK_BLOCK) {
                        val block = e.clickedBlock ?: return
                        block.world.getNearbyEntities(block.location, 3.0, 3.0, 3.0) {
                            it.type == EntityType.ARMOR_STAND
                        }.forEach {
                            it.remove()
                        }
                        e.isCancelled = true
                    }
                }
                else -> Unit
            }
        }
    }

}