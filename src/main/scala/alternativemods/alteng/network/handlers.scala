package alternativemods.alteng.network

import net.minecraft.client.Minecraft.{getMinecraft => mc}
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

object TileEntityDataHandler extends SimpleChannelInboundHandler[PacketTileEntityData] {
  override def channelRead0(ctx: ChannelHandlerContext, msg: PacketTileEntityData){
    val tile = mc.theWorld.getTileEntity(msg.coords._1, msg.coords._2, msg.coords._3)
    if(tile != null && tile.isInstanceOf[SyncedTileEntity]){
      tile.asInstanceOf[SyncedTileEntity].readData(msg.data)
    }
  }
}
