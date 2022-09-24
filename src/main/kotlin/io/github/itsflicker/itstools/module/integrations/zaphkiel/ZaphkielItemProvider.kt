package io.github.itsflicker.itstools.module.integrations.zaphkiel

import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.items.provider.ItemProvider

/**
 * @author wlys
 * @since 2022/6/6 21:29
 */
class ZaphkielItemProvider : ItemProvider("zaphkiel") {

    override fun provideForKey(key: String): TestableItem {
        return ZaphkielTestableItem(key)
    }

}