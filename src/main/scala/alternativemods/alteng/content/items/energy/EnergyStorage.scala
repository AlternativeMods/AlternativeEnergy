package alternativemods.alteng.content.items.energy

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

class EnergyStorage {

  def addEnergy(is: ItemStack, amount: Double) {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("stored"))
      tag.setDouble("stored", 0)

    tag.setDouble("stored", tag.getDouble("stored") + amount)
    if(tag.getDouble("stored") > getMaxStoredEnergy(is))
      tag.setDouble("stored", getMaxStoredEnergy(is))
  }

  def removeEnergy(is: ItemStack, amount: Double) {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("stored"))
      tag.setDouble("stored", 0)

    tag.setDouble("stored", tag.getDouble("stored") - amount)
    if(tag.getDouble("stored") < 0)
      tag.setDouble("stored", 0)
  }

  def setStoredEnergy(is: ItemStack, amount: Double) {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    tag.setDouble("stored", amount)
    if(tag.getDouble("stored") > getMaxStoredEnergy(is))
      tag.setDouble("stored", getMaxStoredEnergy(is))
  }

  def getStoredEnergy(is: ItemStack): Double = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("stored"))
      tag.setDouble("stored", 0)

    tag.getDouble("stored")
  }

  def setMaxStoredEnergy(is: ItemStack, amount: Double) = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    is.getTagCompound.setDouble("maxStored", amount)
  }

  def getMaxStoredEnergy(is: ItemStack) = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("maxStored"))
      tag.setDouble("maxStored", 9999)

    tag.getDouble("maxStored")
  }

}
