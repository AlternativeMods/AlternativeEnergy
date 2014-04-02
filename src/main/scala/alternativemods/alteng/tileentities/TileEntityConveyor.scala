package alternativemods.alteng.tileentities

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound

/**
 * Author: Lordmau5
 * Date: 02.04.14
 * Time: 17:00
 */
class TileEntityConveyor extends TileEntity {
  var facing: Int = 0

  override def readFromNBT(tag : NBTTagCompound) = {
    super.readFromNBT(tag)
    facing = tag.getInteger("facing");
  }

  override def writeToNBT(tag : NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setInteger("facing", facing)
  }
}
