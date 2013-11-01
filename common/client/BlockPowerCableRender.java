package client;

import core.Render;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Author: Lordmau5
 * Date: 31.10.13
 * Time: 22:20
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class BlockPowerCableRender implements ISimpleBlockRenderingHandler
{
    Tessellator tes = Tessellator.instance;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks)
    {
        if(modelID == Render.RENDER_BLOCKPOWERCABLE)
        {
            renderblocks.setRenderBounds(-0.25D, -0.25D, -0.25D, 0.25D, 0.25D, 0.25D);

            GL11.glTranslatef(0F, 0F, 0F);
            tes.startDrawingQuads();
            tes.setNormal(0.0F, -1.0F, 0.0F);
            renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
            tes.draw();
            tes.startDrawingQuads();
            tes.setNormal(0.0F, 1.0F, 0.0F);
            renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
            tes.draw();
            tes.startDrawingQuads();
            tes.setNormal(0.0F, 0.0F, -1.0F);
            tes.addTranslation(0.0F, 0.0F, 0);
            renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
            tes.addTranslation(0.0F, 0.0F, 0);
            tes.draw();
            tes.startDrawingQuads();
            tes.setNormal(0.0F, 0.0F, 1.0F);
            tes.addTranslation(0.0F, 0.0F, 0);
            renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
            tes.addTranslation(0.0F, 0.0F, 0);
            tes.draw();
            tes.startDrawingQuads();
            tes.setNormal(-1.0F, 0.0F, 0.0F);
            tes.addTranslation(0, 0.0F, 0.0F);
            renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
            tes.addTranslation(0, 0.0F, 0.0F);
            tes.draw();
            tes.startDrawingQuads();
            tes.setNormal(1.0F, 0.0F, 0.0F);
            tes.addTranslation(0, 0.0F, 0.0F);
            renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
            tes.addTranslation(0, 0.0F, 0.0F);
            tes.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        return false;
    }

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return 0;
    }
}