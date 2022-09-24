package io.github.itsflicker.itstools.module.integrations.zaphkiel

import com.willfp.eco.core.items.TestableItem
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack

/**
 * @author wlys
 * @since 2022/6/6 21:37
 */
class ZaphkielTestableItem(val id: String) : TestableItem {

    override fun matches(itemStack: ItemStack?): Boolean {
        return itemStack != null && ZaphkielAPI.getItem(itemStack)?.id == id
    }

    override fun getItem(): ItemStack? {
        return ZaphkielAPI.getItemStack(id)
    }

}