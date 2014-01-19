package jkmau5.alternativeenergy.network;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * No description given
 *
 * @author jk-5
 */
@ChannelHandler.Sharable
public class PacketCodec extends FMLIndexedMessageToMessageCodec<AbstractPacket> {

    public PacketCodec(){
        this.addDiscriminator(0, PacketCapacityUpgrade.class);
        this.addDiscriminator(1, PacketOutputspeedUpgrade.class);
        this.addDiscriminator(2, PacketLinkboxFrequencyUpdate.class);
        this.addDiscriminator(3, PacketLinkboxFrequencyServerUpdate.class);
        this.addDiscriminator(4, PacketLinkboxPrivateUpdate.class);
        this.addDiscriminator(5, PacketElementUpdate.class);
        this.addDiscriminator(6, PacketSynchronisation.class);
        this.addDiscriminator(7, PacketGuiString.class);
        this.addDiscriminator(8, PacketGuiCloseSaveData.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext channelHandlerContext, AbstractPacket packet, ByteBuf buf) throws Exception{
        packet.encode(buf);
    }

    @Override
    public void decodeInto(ChannelHandlerContext channelHandlerContext, ByteBuf buf, AbstractPacket packet){
        packet.decode(buf);
    }
}
