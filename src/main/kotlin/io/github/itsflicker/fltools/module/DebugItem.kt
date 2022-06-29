package io.github.itsflicker.fltools.module

import io.github.itsflicker.fltools.api.NMS
import io.github.itsflicker.fltools.module.DebugItem.Mode.*
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Baffle
import taboolib.library.xseries.XMaterial
import taboolib.module.ai.navigationMove
import taboolib.module.chat.colored
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.module.nms.inputSign
import taboolib.platform.util.buildItem
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyLore
import taboolib.platform.util.sendActionBar
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * @author wlys
 * @since 2022/4/23 22:19
 */
object DebugItem {

    private val cooldown = Baffle.of(100L, TimeUnit.MILLISECONDS)

    val item = buildItem(XMaterial.BLAZE_ROD) {
        name = "&f&lFlTools&c Debug Item"
        lore.add("&7当前模式: &fGET_ENTITY_UUID")
        shiny()
        colored()
    }.also {
        makeDebugItem(it)
        setMode(it, GET_ENTITY_UUID)
    }

    enum class Mode(val index: Int) {

        NULL(-1),

        GET_ENTITY_UUID(0),

        NAVIGATE(1),

        MAKE_MELEE_HOSTILE(2)
    }

    val negativeCache = ConcurrentHashMap<String, UUID>()

    fun getMode(item: ItemStack): Mode {
        val index = item.getItemTag()["mode"]?.asInt() ?: return NULL
        return values()[index]
    }

    fun setMode(item: ItemStack, mode: Mode) {
        val component = item.getItemTag()
        component["mode"] = ItemTagData(mode.index)
        component.saveTo(item)
    }

    fun isDebugItem(item: ItemStack): Boolean {
        return item.getItemTag()["fltools_debug_item"]?.asInt() == 1
    }

    fun makeDebugItem(item: ItemStack) {
        val component = item.getItemTag()
        component["fltools_debug_item"] = ItemTagData(1)
        component.saveTo(item)
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEntityEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand.also { if (it.isAir()) return }
        if (isDebugItem(item) && cooldown.hasNext(player.name)) {
            val entity = e.rightClicked
            when (getMode(item)) {
                GET_ENTITY_UUID -> {
                    player.sendMessage("&cUUID: &f${entity.uniqueId}".colored())
                }
                NAVIGATE -> {
                    if (entity is LivingEntity) {
                        negativeCache[player.name] = entity.uniqueId
                        player.sendMessage("&f${entity.uniqueId} &cCached.".colored())
                    }
                }
                MAKE_MELEE_HOSTILE -> {
                    if (entity is LivingEntity) {
                        if (player.isSneaking) {
                            player.inputSign(arrayOf("2.0", "1.0", "The first line: damage", "The second line: speed")) {
                                val damage = it[0].toDoubleOrNull() ?: 2.0
                                val speed = it[1].toDoubleOrNull() ?: 1.0
                                NMS.INSTANCE.makeMeleeHostile(entity, damage, speed)
                            }
                        } else {
                            NMS.INSTANCE.makeMeleeHostile(entity)
                        }
                    }
                }
                NULL -> {
                    player.sendMessage("&cInvalid Mode.".colored())
                }
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
            val newMode = if (mode < Mode.values().size - 2) mode + 1 else 0
            setMode(item, Mode.values()[newMode].also {
                item.modifyLore {
                    set(0, "&7当前模式: &f${it.name}".colored())
                }
                player.sendActionBar("§cCurrent mode: ${it.name}")
            })
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand.also { if (it.isAir()) return }
        if (e.action == Action.RIGHT_CLICK_BLOCK && isDebugItem(item) && getMode(item) == NAVIGATE) {
            val entityUUID = negativeCache.remove(player.name) ?: return
            val entity = Bukkit.getEntity(entityUUID) as? LivingEntity ?: return
            entity.navigationMove(e.clickedBlock?.location ?: player.location, 1.2)
            e.isCancelled = true
        }
    }

}