package alternativemods.alteng.network

import net.minecraft.tileentity.TileEntity
import io.netty.buffer.{Unpooled, ByteBuf}
import cpw.mods.fml.relauncher.Side

/**
 * No description given
 *
 * @author jk-5
 */
trait SyncedTileEntity extends TileEntity {
  def writeData(buffer: ByteBuf)
  def readData(buffer: ByteBuf)

  def sendData(){
    //TODO: Only send this to players watching the chunk
    NetworkHandler.sendPacketToAllPlayersInDimension(this.getTileEntityPacket, this.worldObj.provider.dimensionId)
  }

  def getTileEntityPacket: PacketTileEntityData = {
    val packet = new PacketTileEntityData
    packet.data = Unpooled.buffer()
    this.writeData(packet.data)
    packet.coords = (this.xCoord, this.yCoord, this.zCoord)
    packet
  }

  override def getDescriptionPacket = NetworkHandler.channel(Side.SERVER).generatePacketFrom(this.getTileEntityPacket)
}
