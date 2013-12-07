package jkmau5.alternativeenergy.client.gui.button;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.gui.button.IButtonTextureSet;
import jkmau5.alternativeenergy.gui.button.StandardButtonTextureSets;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Our button base class. Has things like tooltips and multiple textures
 *
 * @author jk-5
 */
@SideOnly(Side.CLIENT)
public abstract class AltEngGuiButton extends GuiButton {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.TEXTURE_DOMAIN + ":textures/gui/guiBasic.png");

    protected final IButtonTextureSet texture;

    @Getter
    @Setter
    private ToolTip toolTip;

    public AltEngGuiButton(int id, int x, int y, String label) {
        this(id, x, y, 200, StandardButtonTextureSets.LARGE_BUTTON, label);
    }

    public AltEngGuiButton(int id, int x, int y, int width, String label) {
        this(id, x, y, width, StandardButtonTextureSets.LARGE_BUTTON, label);
    }

    public AltEngGuiButton(int id, int x, int y, int width, IButtonTextureSet texture, String label) {
        super(id, x, y, width, texture.getHeight(), label);
        this.texture = texture;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.texture.getHeight();
    }

    public int getTextColor(boolean mouseOver) {
        if(!this.enabled) {
            return 0xA0A0A0;
        }
        if(mouseOver) {
            return 0xFFFFA0;
        }
        return 0xE0E0E0;
    }

    public boolean isMouseOverButton(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + getHeight());
    }

    protected void bindButtonTextures(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(TEXTURE);
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (!this.drawButton) {
            return;
        }
        FontRenderer fontrenderer = minecraft.fontRenderer;
        bindButtonTextures(minecraft);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int xOffset = this.texture.getX();
        int yOffset = this.texture.getY();
        int h = this.texture.getHeight();
        int w = this.texture.getWidth();
        boolean mouseOver = isMouseOverButton(mouseX, mouseY);
        int hoverState = getHoverState(mouseOver);

        this.drawTexturedModalRect(this.xPosition, this.yPosition, xOffset, yOffset + hoverState * h, this.width / 2, h);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, xOffset + w - this.width / 2, yOffset + hoverState * h, this.width / 2, h);
        this.mouseDragged(minecraft, mouseX, mouseY);
        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (h - 8) / 2, getTextColor(mouseOver));
    }
}
