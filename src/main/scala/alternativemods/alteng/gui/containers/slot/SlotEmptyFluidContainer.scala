package alternativemods.alteng.gui.containers.slot

import net.minecraft.inventory.{Slot, IInventory}
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidContainerRegistry

/**
 * No description given
 *
 * @author jk-5
 */
class SlotEmptyFluidContainer(inv: IInventory, index: Int, x: Int, y: Int) extends Slot(inv, index, x, y) {
  override def isItemValid(stack: ItemStack) = FluidContainerRegistry.isEmptyContainer(stack)
}
