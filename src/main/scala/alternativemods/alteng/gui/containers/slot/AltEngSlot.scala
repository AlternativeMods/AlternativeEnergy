package alternativemods.alteng.gui.containers.slot

import net.minecraft.inventory.{IInventory, Slot}
import alternativemods.alteng.util.ToolTip

/**
 * No description given
 *
 * @author jk-5
 */
abstract class AltEngSlot(_inventory: IInventory, index: Int, x: Int, y: Int) extends Slot(_inventory, index, x, y) {
  var tooltip: ToolTip = null
  def isFakeSlot = false
  def canAdjustFakeSlot = true
  def canShiftClick = true
}
