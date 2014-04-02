package alternativemods.alteng.tileentities

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.network.{Packet, NetworkManager}
import net.minecraft.network.play.server.S35PacketUpdateTileEntity

/**
 * Author: Lordmau5
 * Date: 02.04.14
 * Time: 17:00
 */
class TileEntityConveyor extends TileEntity {
  var facing: Int = 0

  def this(facing: Int) {
    this()
    this.facing = facing
  }

  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    facing = tag.getInteger("facing")
  }

  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setInteger("facing", facing)
  }

  override def getDescriptionPacket: Packet = {
    new S35PacketUpdateTileEntity()
  }

  override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) = readFromNBT(pkt.func_148857_g())
}
