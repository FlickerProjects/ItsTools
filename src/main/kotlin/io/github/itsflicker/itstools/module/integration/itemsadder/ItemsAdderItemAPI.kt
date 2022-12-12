package io.github.itsflicker.itstools.module.integration.itemsadder

import dev.lone.itemsadder.api.CustomStack
import ink.ptms.sandalphon.ItemAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemsAdderItemAPI : ItemAPI {

    override fun getId(itemStack: ItemStack): String? {
        return CustomStack.byItemStack(itemStack)?.id
    }

    override fun getItem(id: String, player: Player?): ItemStack? {
        return CustomStack.getInstance(id)?.itemStack
    }

    override fun getData(itemStack: ItemStack, node: String): String? {
        return CustomStack.byItemStack(itemStack)?.config?.getString(node)
    }

    override fun getDataList(itemStack: ItemStack, node: String): List<String> {
        return CustomStack.byItemStack(itemStack)?.config?.getStringList(node) ?: emptyList()
    }

}