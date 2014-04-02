package alternativemods.alteng.tileentities

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import alternativemods.alteng.network.SyncedTileEntity
import io.netty.buffer.ByteBuf

/**
 * Author: Lordmau5
 * Date: 02.04.14
 * Time: 17:00
 */
class TileEntityConveyor(var facing: Int = 0) extends TileEntity with SyncedTileEntity {

  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    facing = tag.getInteger("facing")
  }

  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setInteger("facing", facing)
  }

  override def readData(buffer: ByteBuf){
    this.facing = buffer.readInt()
  }

  override def writeData(buffer: ByteBuf){
    buffer.writeInt(this.facing)
  }
}
