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

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        if(this.tileEntity.getPowerStored() > 0){
            int indicatorHeight = (this.tileEntity.getMaxStoredPower() / this.tileEntity.getPowerStored()) * 54;
            this.drawTexturedModalRect(x + 7, y + 70 - indicatorHeight, 176, 54 - indicatorHeight, 18, indicatorHeight + 31);
        }
    }

    private void debugMouse(int mouseX, int mouseY) {
        drawCreativeTabHoveringText("X: " + mouseX + " | Y: " + mouseY, mouseX + guiLeft, mouseY + guiTop);
    }
}