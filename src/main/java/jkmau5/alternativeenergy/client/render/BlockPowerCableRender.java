package jkmau5.alternativeenergy.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerCable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

/**
 * Author: Lordmau5
 * Date: 31.10.13
 * Time: 22:20
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class BlockPowerCableRender implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        float min = 0.355F;
        float max = 0.645F;
        renderer.setRenderBounds(min, min, min, max, max, max);
        renderer.renderAllFaces = true;
        renderer.renderStandardBlock(block, x, y, z);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof TileEntityPowerCable) {
            TileEntityPowerCable cable = (TileEntityPowerCable) tile;
            for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if(cable.getConnectionMatrix().isConnected(direction)) {
                    if(direction == ForgeDirection.UP) {
                        renderer.setRenderBounds(min, max, min, max, 1, max);
                    } else if(direction == ForgeDirection.DOWN) {
                        renderer.setRenderBounds(min, 0, min, max, min, max);
                    } else if(direction == ForgeDirection.NORTH) {
                        renderer.setRenderBounds(min, min, 0, max, max, min);
                    } else if(direction == ForgeDirection.SOUTH) {
                        renderer.setRenderBounds(min, min, max, max, max, 1);
                    } else if(direction == ForgeDirection.EAST) {
                        renderer.setRenderBounds(max, min, min, 1, max, max);
                    } else if(direction == ForgeDirection.WEST) {
                        renderer.setRenderBounds(0, min, min, min, max, max);
                    }
                    renderer.renderStandardBlock(block, x, y, z);
                }
            }
        }
        return true;
    }

    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return RenderIDs.POWERCABLE.getRenderID();
    }
}
