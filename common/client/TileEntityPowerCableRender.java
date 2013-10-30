package client;

import core.Main;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Author: Lordmau5
 * Date: 28.10.13
 * Time: 10:59
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class TileEntityPowerCableRender extends TileEntitySpecialRenderer {
    private ModelPowerCable modelPowerCable;
    private static final ResourceLocation texture = new ResourceLocation(Main.modid.toLowerCase(), "textures/blocks/powerCable.png");

    public TileEntityPowerCableRender() {
        this.modelPowerCable = new ModelPowerCable();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
        //GL11.glTranslatef((float) d0, (float) d1, (float) d2);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        //bindTexture(texture);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        //-0.1F;
        this.modelPowerCable.render(tileentity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}