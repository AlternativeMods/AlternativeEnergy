package jkmau5.alternativeenergy.client.render;

import jkmau5.alternativeenergy.world.blocks.AltEngBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * No description given
 *
 * @author jk-5
 */
public class ItemRendererPowerCable implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType itemRenderType) {
        if(itemStack == null) {
            return false;
        }
        switch(itemRenderType) {
            case ENTITY:
            case INVENTORY:
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType itemRenderType, ItemStack itemStack, ItemRendererHelper itemRendererHelper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType itemRenderType, ItemStack itemStack, Object... objects) {
        GL11.glPushMatrix();
        switch(itemRenderType) {
            case ENTITY:
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case INVENTORY:
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case EQUIPPED:
                GL11.glTranslatef(0.0f, 0.50f, 0.35f);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(-0.4f, 0.50f, 0.35f);
                break;
        }
        this.doRenderCable((RenderBlocks) objects[0]);
        GL11.glPopMatrix();
    }

    public void doRenderCable(RenderBlocks renderBlocks) {
        float min = 0.355F;
        float max = 0.645F;
        Tessellator tessellator = Tessellator.instance;
        Block block = AltEngBlocks.blockPowerCable;
        Icon icon = AltEngBlocks.blockPowerCable.getIcon(0, 0);
        renderBlocks.setRenderBounds(min, 0, min, max, 1, max);
        renderBlocks.renderAllFaces = true;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
    }
}
