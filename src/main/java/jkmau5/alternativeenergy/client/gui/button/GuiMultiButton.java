package jkmau5.alternativeenergy.client.gui.button;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.gui.button.IButtonTextureSet;
import jkmau5.alternativeenergy.gui.button.IMultiButtonState;
import jkmau5.alternativeenergy.gui.button.MultiButtonController;
import jkmau5.alternativeenergy.gui.button.StandardButtonTextureSets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiMultiButton extends AltEngGuiButton {

    private final MultiButtonController control;
    public boolean canChange = true;

    public GuiMultiButton(int id, int x, int y, int width, MultiButtonController control) {
        super(id, x, y, width, StandardButtonTextureSets.LARGE_BUTTON, "");
        this.control = control;
    }

    @Override
    public int getHeight() {
        return this.texture.getHeight();
    }

    @Override
    public void drawButton(Minecraft minecraft, int x, int y) {
        if (!this.drawButton) {
            return;
        }
        FontRenderer fontrenderer = minecraft.fontRenderer;
        bindButtonTextures(minecraft);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        IMultiButtonState state = this.control.getButtonState();
        IButtonTextureSet tex = state.getTextureSet();
        int xOffset = tex.getX();
        int yOffset = tex.getY();
        int h = tex.getHeight();
        int w = tex.getWidth();
        boolean hover = (x >= this.xPosition) && (y >= this.yPosition) && (x < this.xPosition + this.width) && (y < this.yPosition + h);
        int hoverState = getHoverState(hover);
        drawTexturedModalRect(this.xPosition, this.yPosition, xOffset, yOffset + hoverState * h, this.width / 2, h);
        drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, xOffset + w - this.width / 2, yOffset + hoverState * h, this.width / 2, h);
        mouseDragged(minecraft, x, y);
        this.displayString = state.getLabel();
        if (!this.displayString.isEmpty()) {
            if (!this.enabled) {
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (h - 8) / 2, -6250336);
            } else if (hover) {
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (h - 8) / 2, 16777120);
            } else {
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (h - 8) / 2, 14737632);
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
        boolean pressed = super.mousePressed(minecraft, mouseX, mouseY);
        if(this.canChange && pressed && this.enabled) {
            if (Mouse.getEventButton() == 0) {
                this.control.incrementState();
            } else {
                this.control.decrementState();
            }
        }
        return pressed;
    }

    public MultiButtonController getController() {
        return this.control;
    }

    @Override
    public ToolTip getToolTip() {
        ToolTip tip = this.control.getButtonState().getToolTip();
        if (tip != null) {
            return tip;
        }
        return super.getToolTip();
    }
}