package alternativemods.alteng.items.energy

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack

trait IEnergyItem {

  val energy = new EnergyStorage

  def getTransferLimit: Int = 0

  /**
   * Tries to add the specified amount of energy (EU) to the Item.
   *
   * @param is The Itemstack of the Item
   * @param energy Energy that should get added into the Item
   * @return Energy that didn't get in
   */
  def addEU(is: ItemStack, energy: Int): Int = 0

  /**
   * Tries to remove the specified amount of energy (EU) from the Item.
   *
   * @param is The Itemstack of the Item
   * @param energy Energy that should get removed from the Item
   * @return Energy that got removed
   */
  def removeEU(is: ItemStack, energy: Int): Int = 0

  def createNBT(is: ItemStack) = {
    is.setTagCompound(new NBTTagCompound)
  }
}
