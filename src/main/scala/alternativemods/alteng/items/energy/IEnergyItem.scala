package alternativemods.alteng.items.energy

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack

trait IEnergyItem {

  val energy = new EnergyStorage

  def getTransferLimit: Int = 0

  /**
   * Tries to add the specified amount of energy to the Item.
   *
   * @param energy Energy that should get added into the Item
   * @return Energy that didn't get in
   */
  def addEU(is: ItemStack, energy: Int): Int = 0

  def createNBT(is: ItemStack) = {
    is.setTagCompound(new NBTTagCompound)
  }
}
