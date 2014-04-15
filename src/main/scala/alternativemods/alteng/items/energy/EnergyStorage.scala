package alternativemods.alteng.items.energy

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * @author: Lordmau5
 * @date: 15.04.14 - 19:24
 */
class EnergyStorage {

  var maxCharge = 0

  def addEnergy(is: ItemStack, amount: Int) {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("stored"))
      tag.setInteger("stored", 0)

    tag.setInteger("stored", tag.getInteger("stored") + amount)
    if(tag.getInteger("stored") > maxCharge)
      tag.setInteger("stored", maxCharge)
  }

  def removeEnergy(is: ItemStack, amount: Int) {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("stored"))
      tag.setInteger("stored", 0)

    tag.setInteger("stored", tag.getInteger("stored") - amount)
    if(tag.getInteger("stored") < 0)
      tag.setInteger("stored", 0)
  }

  def setStoredEnergy(is: ItemStack, amount: Int) {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    tag.setInteger("stored", amount)
    if(tag.getInteger("stored") > maxCharge)
      tag.setInteger("stored", maxCharge)
  }

  def getStoredEnergy(is: ItemStack) = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("stored"))
      tag.setInteger("stored", 0)

    tag.getInteger("stored")
  }

  def setMaxStoredEnergy(is: ItemStack, amount: Int) = maxCharge = amount
  def getMaxStoredEnergy(is: ItemStack) = maxCharge

}
