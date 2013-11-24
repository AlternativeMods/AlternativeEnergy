package jkmau5.alternativeenergy.client.gui;

import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.gui.container.ContainerPowerBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiPowerBox extends AltEngGuiContainer {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURE_DOMAIN, "textures/gui/powerBox.png");
    private final TileEntityPowerBox tileEntity;

    public GuiPowerBox(InventoryPlayer inventoryPlayer, TileEntityPowerBox tileEntity) {
        super(new ContainerPowerBox(inventoryPlayer, tileEntity), background);
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int posX, int posY) {
        super.drawGuiContainerForegroundLayer(posX, posY);
        fontRenderer.drawString("Power Box", 6, 5, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 4, 0x000000);
    }

    private void debugMouse(int mouseX, int mouseY) {
        drawCreativeTabHoveringText("X: " + mouseX + " | Y: " + mouseY, mouseX + guiLeft, mouseY + guiTop);
    }
}