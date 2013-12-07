package jkmau5.alternativeenergy.client.gui;

import jkmau5.alternativeenergy.client.gui.button.AltEngGuiButton;
import jkmau5.alternativeenergy.client.render.RenderState;
import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.client.render.ToolTipLine;
import jkmau5.alternativeenergy.gui.container.AltEngContainer;
import jkmau5.alternativeenergy.gui.container.slot.AltEngSlot;
import jkmau5.alternativeenergy.gui.element.Element;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngGuiContainer extends GuiContainer {

    @Getter
    private final AltEngContainer container;
    private final ResourceLocation backgroundTexture;

    public AltEngGuiContainer(AltEngContainer container, ResourceLocation background) {
        super(container);
        this.container = container;
        this.backgroundTexture = background;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        super.drawScreen(mouseX, mouseY, partialTick);

        int left = this.guiLeft;
        int top = this.guiTop;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(left, top, 0);
        GL11.glColor4f(1, 1, 1, 1);
        RenderHelper.disableStandardItemLighting();

        InventoryPlayer inventoryPlayer = this.mc.thePlayer.inventory;
        if(inventoryPlayer.getItemStack() == null) { //If no ItemStack is hovered
            int mX = mouseX - left;
            int mY = mouseY - top;
            for(Element element : this.container.getElements()) {
                if(!element.isHidden) {
                    ToolTip toolTip = element.getToolTip();
                    if(toolTip != null) {
                        boolean mouseOver = element.isMouseOver(mX, mY);
                        toolTip.onTick(mouseOver);
                        if(mouseOver && toolTip.isReady()) {
                            toolTip.refresh();
                            this.drawToolTip(toolTip, mouseX, mouseY);
                        }
                    }
                }
            }
            for(GuiButton button : (List<GuiButton>) this.buttonList) {
                if(button.drawButton && button instanceof AltEngGuiButton) {
                    AltEngGuiButton altEngButton = (AltEngGuiButton) button;
                    ToolTip toolTip = altEngButton.getToolTip();
                    if(toolTip != null) {
                        boolean mouseOver = altEngButton.isMouseOverButton(mouseX, mouseY);
                        toolTip.onTick(mouseOver);
                        if(mouseOver && toolTip.isReady()) {
                            toolTip.refresh();
                            this.drawToolTip(toolTip, mouseX, mouseY);
                        }
                    }
                }
            }
            for(Slot s : (List<Slot>) this.inventorySlots.inventorySlots) {
                if(s instanceof AltEngSlot) {
                    AltEngSlot slot = (AltEngSlot) s;
                    if(slot.getStack() == null) {
                        ToolTip toolTip = slot.getToolTip();
                        if(toolTip != null) {
                            boolean mouseOver = this.isMouseOverSlot(slot, mouseX, mouseY);
                            toolTip.onTick(mouseOver);
                            if(mouseOver && toolTip.isReady()) {
                                toolTip.refresh();
                                this.drawToolTip(toolTip, mouseX, mouseY);
                            }
                        }
                    }
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);
        RenderState.bindTexture(this.backgroundTexture);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        int mX = mouseX - this.guiLeft;
        int mY = mouseY - this.guiTop;

        for(Element element : this.container.getElements()) {
            if(!element.isHidden) {
                element.draw(this, x, y, mX, mY);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        int mX = mouseX - this.guiLeft;
        int mY = mouseY - this.guiTop;

        for (Element element : this.container.getElements()) {
            if (!element.isHidden && element.isMouseOver(mX, mY)) {
                if (element.mouseClicked(mX, mY, button)) {
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void mouseClickMove(int x, int y, int mouseButton, long time) {
        Slot slot = getSlotAtPosition(x, y);
        if (mouseButton == 1 && (slot instanceof AltEngSlot && ((AltEngSlot) slot).isFakeSlot())) {
            return;
        }
        super.mouseClickMove(x, y, mouseButton, time);
    }

    private Slot getSlotAtPosition(int x, int y) {
        for (Slot slot : (List<Slot>) this.inventorySlots.inventorySlots) {
            if (isMouseOverSlot(slot, x, y)) {
                return slot;
            }
        }
        return null;
    }

    private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
        int left = this.guiLeft;
        int top = this.guiTop;
        mouseX -= left;
        mouseY -= top;
        return (mouseX >= slot.xDisplayPosition - 1) && (mouseX < slot.xDisplayPosition + 16 + 1) && (mouseY >= slot.yDisplayPosition - 1) && (mouseY < slot.yDisplayPosition + 16 + 1);
    }

    private void drawToolTip(ToolTip toolTip, int mouseX, int mouseY) {
        if (toolTip.size() > 0) {
            int left = this.guiLeft;
            int top = this.guiTop;
            int length = 0;
            for(ToolTipLine line : toolTip) {
                int y = this.fontRenderer.getStringWidth(line.getText());
                if (y > length) {
                    length = y;
                }
            }
            int x = mouseX - left + 12;
            int y = mouseY - top - 12;
            int var14 = 8;
            if (toolTip.size() > 1) {
                var14 += 2 + (toolTip.size() - 1) * 10;
            }
            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int var15 = -267386864;
            this.drawGradientRect(x - 3, y - 4, x + length + 3, y - 3, var15, var15);
            this.drawGradientRect(x - 3, y + var14 + 3, x + length + 3, y + var14 + 4, var15, var15);
            this.drawGradientRect(x - 3, y - 3, x + length + 3, y + var14 + 3, var15, var15);
            this.drawGradientRect(x - 4, y - 3, x - 3, y + var14 + 3, var15, var15);
            this.drawGradientRect(x + length + 3, y - 3, x + length + 4, y + var14 + 3, var15, var15);
            int var16 = 1347420415;
            int var17 = (var16 & 0xFEFEFE) >> 1 | var16 & 0xFF000000;
            this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + var14 + 3 - 1, var16, var17);
            this.drawGradientRect(x + length + 2, y - 3 + 1, x + length + 3, y + var14 + 3 - 1, var16, var17);
            this.drawGradientRect(x - 3, y - 3, x + length + 3, y - 3 + 1, var16, var16);
            this.drawGradientRect(x - 3, y + var14 + 2, x + length + 3, y + var14 + 3, var17, var17);

            for (ToolTipLine tip : toolTip) {
                String line = tip.getText();
                if (tip.getColor() == -1) {
                    line = "§7" + line;
                } else {
                    line = "§" + Integer.toHexString(tip.getColor()) + line;
                }
                this.fontRenderer.drawStringWithShadow(line, x, y, -1);
                y += 10 + tip.getSpacing();
            }

            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }

    public void drawTexture(int x, int y, int w, int h, float uMin, float vMin, float uMax, float vMax) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, this.zLevel, uMin, vMax);
        tessellator.addVertexWithUV(x + w, y + h, this.zLevel, uMax, vMax);
        tessellator.addVertexWithUV(x + w, y, this.zLevel, uMax, vMin);
        tessellator.addVertexWithUV(x, y, this.zLevel, uMin, vMin);
        tessellator.draw();
    }
}
