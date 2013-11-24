package jkmau5.alternativeenergy.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import jkmau5.alternativeenergy.client.render.BlockPowerCableRender;
import jkmau5.alternativeenergy.client.render.ItemRendererPowerCable;
import jkmau5.alternativeenergy.server.ProxyCommon;
import jkmau5.alternativeenergy.world.blocks.AltEngBlocks;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * No description given
 *
 * @author jk-5
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();

        TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
    }

    @Override
    public void registerRenderers() {
        RenderingRegistry.registerBlockHandler(new BlockPowerCableRender());

        MinecraftForgeClient.registerItemRenderer(AltEngBlocks.blockPowerCable.blockID, new ItemRendererPowerCable());
    }

    @Override
    public long getTicks(World world) {
        return TickHandlerClient.getTicks();
    }
}
