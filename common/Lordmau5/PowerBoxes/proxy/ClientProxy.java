package Lordmau5.PowerBoxes.proxy;

import Lordmau5.PowerBoxes.client.BlockPowerCableRender;
import Lordmau5.PowerBoxes.core.Render;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * Author: Lordmau5
 * Date: 28.10.13
 * Time: 10:57
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void initRendering()
    {
        Render.RENDER_BLOCKPOWERCABLE = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Render.RENDER_BLOCKPOWERCABLE, new BlockPowerCableRender());
    }
}
