package jkmau5.alternativeenergy.server;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import jkmau5.alternativeenergy.AlternativeEnergy;

/**
 * No description given
 *
 * @author jk-5
 */
public class ProxyCommon {

    public void registerEventHandlers(){

    }

    public void registerNetworkHandlers(){
        NetworkRegistry.instance().registerChannel(new PacketHandlerServer(), "AltEng", Side.SERVER);
    }

    public void registerSideSensitiveHandlers(){
        NetworkRegistry.instance().registerGuiHandler(AlternativeEnergy.instance, new GuiHandlerServer());
    }
}
