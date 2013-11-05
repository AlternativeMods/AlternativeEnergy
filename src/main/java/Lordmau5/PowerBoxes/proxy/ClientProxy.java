package Lordmau5.PowerBoxes.proxy;

import Lordmau5.PowerBoxes.client.BlockPowerCableRender;
import Lordmau5.PowerBoxes.client.TileEntityPowerCableRender;
import Lordmau5.PowerBoxes.core.Render;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import Lordmau5.PowerBoxes.tile.TileEntityPowerCable;

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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerCable.class, new TileEntityPowerCableRender());

        Render.RENDER_BLOCKPOWERCABLE = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Render.RENDER_BLOCKPOWERCABLE, new BlockPowerCableRender());

    }

}