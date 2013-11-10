package jkmau5.alternativeenergy.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.client.render.BlockPowerCableRender;
import jkmau5.alternativeenergy.client.render.Render;
import jkmau5.alternativeenergy.server.ProxyCommon;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();

        Render.RENDER_BLOCKPOWERCABLE = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Render.RENDER_BLOCKPOWERCABLE, new BlockPowerCableRender());
    }

    @Override
    public void registerNetworkHandlers() {
        super.registerNetworkHandlers();

        NetworkRegistry.instance().registerChannel(new PacketHandlerClient(), "AltEng", Side.CLIENT);

        TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
    }

    @Override
    public void registerSideSensitiveHandlers() {
        //Do NOT! call the super method on this
        NetworkRegistry.instance().registerGuiHandler(AlternativeEnergy.instance, new GuiHandlerClient());
    }

    @Override
    public long getTicks(World world) {
        return super.getTicks(world);
    }
}
