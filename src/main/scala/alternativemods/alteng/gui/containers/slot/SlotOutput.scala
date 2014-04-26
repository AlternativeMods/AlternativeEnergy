package alternativemods.alteng.gui.containers.slot

import net.minecraft.inventory.{Slot, IInventory}
import net.minecraft.item.ItemStack

/**
 * No description given
 *
 * @author jk-5
 */
class SlotOutput(inv: IInventory, index: Int, x: Int, y: Int) extends Slot(inv, index, x, y) {
  override def isItemValid(par1ItemStack: ItemStack) = false
}
