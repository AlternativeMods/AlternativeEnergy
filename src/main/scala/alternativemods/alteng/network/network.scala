package alternativemods.alteng.network

import cpw.mods.fml.relauncher.{SideOnly, Side}
import cpw.mods.fml.common.network.{FMLOutboundHandler, FMLIndexedMessageToMessageCodec, FMLEmbeddedChannel, NetworkRegistry}
import java.util
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.ByteBuf

/**
 * No description given
 *
 * @author jk-5
 */
object NetworkHandler {

  private var channels: util.EnumMap[Side, FMLEmbeddedChannel] = _

  def init(side: Side){
    this.channels = NetworkRegistry.INSTANCE.newChannel("AltEng", AltEngPacketCodec)

    if(side.isClient) this.initClientHandlers()
  }

  @SideOnly(Side.CLIENT) def initClientHandlers(){

  }

  def channel(side: Side) = this.channels.get(side)

  def sendPacketToAllPlayersInDimension(packet: Packet, dimension: Int){
    val channel = this.channel(Side.SERVER)
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION)
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(Int.box(dimension)) //Ugly hack for netty not following standards
    channel.writeOutbound(packet)
  }
}

@Sharable
object AltEngPacketCodec extends FMLIndexedMessageToMessageCodec[Packet] {

  //this.addDiscriminator()

  override def decodeInto(ctx: ChannelHandlerContext, in: ByteBuf, out: Packet) = out.decode(in)
  override def encodeInto(ctx: ChannelHandlerContext, in: Packet, out: ByteBuf) = in.encode(out)
}
