package alternativemods.alteng.items.energy

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * @author: Lordmau5
 * @date: 15.04.14 - 17:26
 */
class EnergyBuffer {

  def addBuffer(prefix: String, is: ItemStack, amount: Int) = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("buffer_" + prefix))
      tag.setInteger("buffer_" + prefix, 0)

    tag.setInteger("buffer_" + prefix, tag.getInteger("buffer_" + prefix) + amount)
  }

  def removeBuffer(prefix: String, is: ItemStack, amount: Int) = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("buffer_" + prefix))
      tag.setInteger("buffer_" + prefix, 0)

    tag.setInteger("buffer_" + prefix, tag.getInteger("buffer_" + prefix) - amount)
    if(tag.getInteger("buffer_" + prefix) < 0)
      tag.setInteger("buffer_" + prefix, 0)
  }

  def getBuffer(prefix: String, is: ItemStack) = {
    if(is.getTagCompound == null)
      is.setTagCompound(new NBTTagCompound)

    val tag = is.getTagCompound
    if(!tag.hasKey("buffer_" + prefix))
      tag.setInteger("buffer_" + prefix, 0)

    tag.getInteger("buffer_" + prefix)
  }

}
