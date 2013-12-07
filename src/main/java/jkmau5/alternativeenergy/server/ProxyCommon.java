package jkmau5.alternativeenergy.server;

import cpw.mods.fml.common.network.NetworkRegistry;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.gui.GuiHandler;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public class ProxyCommon {

    public void registerEventHandlers() {
        NetworkRegistry.instance().registerChannel(new PacketHandler(), "AltEng");
        NetworkRegistry.instance().registerGuiHandler(AlternativeEnergy.getInstance(), new GuiHandler());
    }

    public void initCompat() {

    }

    public void registerRenderers() {

    }

    public long getTicks(World world) {
        return world.getTotalWorldTime();
    }
}
