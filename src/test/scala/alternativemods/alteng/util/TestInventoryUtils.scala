package alternativemods.alteng.util

import org.junit.{Assert, Test}
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.OreDictionary

/**
 * No description given
 *
 * @author jk-5
 */
class TestInventoryUtils {

  private val item1 = new Item
  private val item2 = new Item
  private val subitem1 = new Item().setHasSubtypes(true)
  private val subitem2 = new Item().setHasSubtypes(true)

  @Test def testItemStackMatchDifferentItem(){
    Assert.assertFalse(InventoryUtils.areItemsEqual(new ItemStack(item1), new ItemStack(item2)))
    Assert.assertFalse(InventoryUtils.areItemsEqual(new ItemStack(subitem1), new ItemStack(subitem2)))
  }

  @Test def testItemStackMatchDifferentStacksize(){
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(item1, 10), new ItemStack(item1, 11)))
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(subitem1, 10), new ItemStack(subitem1, 11)))
  }

  @Test def testItemStackMatchDifferentMeta(){
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(item1, 10, 0), new ItemStack(item1, 11, 1)))
    Assert.assertFalse(InventoryUtils.areItemsEqual(new ItemStack(subitem1, 10, 0), new ItemStack(subitem1, 11, 1)))
  }

  @Test def testItemStackMatchWildcardMeta(){
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(item1, 10, OreDictionary.WILDCARD_VALUE), new ItemStack(item1, 11, OreDictionary.WILDCARD_VALUE)))
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(subitem1, 10, OreDictionary.WILDCARD_VALUE), new ItemStack(subitem1, 11, OreDictionary.WILDCARD_VALUE)))
  }

  @Test def testItemStackMatchIgnoreMeta(){
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(item1, 10, 0), new ItemStack(item1, 11, 1), matchDamage = false))
    Assert.assertTrue(InventoryUtils.areItemsEqual(new ItemStack(subitem1, 10, 0), new ItemStack(subitem1, 11, 1), matchDamage = false))
  }
}
