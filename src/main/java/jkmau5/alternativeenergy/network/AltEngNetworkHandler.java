package jkmau5.alternativeenergy.network;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.EnumMap;

/**
 * No description given
 *
 * @author jk-5
 */
public class AltEngNetworkHandler {

    private static EnumMap<Side, FMLEmbeddedChannel> channels;

    public static void load(Side side){
        channels = NetworkRegistry.INSTANCE.newChannel("AltEng", new PacketCodec());



        if(side.isClient()){
            loadClientHandlers();
        }
    }

    @SideOnly(Side.CLIENT)
    private static void loadClientHandlers(){

    }
}
