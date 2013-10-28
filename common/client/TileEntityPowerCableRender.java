package client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import tile.TileEntityPowerCable;

/**
 * Author: Lordmau5
 * Date: 28.10.13
 * Time: 10:59
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class TileEntityPowerCableRender extends TileEntitySpecialRenderer {
    private ModelPowerCable modelPowerCable = new ModelPowerCable();

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
    {
        modelPowerCable.render((TileEntityPowerCable)tileentity, d0, d1, d2);
    }
}