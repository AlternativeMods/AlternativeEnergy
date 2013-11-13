package jkmau5.alternativeenergy.client.gui;

import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.gui.container.ContainerLinkBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Author: Lordmau5
 * Date: 03.11.13
 * Time: 11:29
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class GuiLinkBox extends AltEngGuiContainer {

    private static final ResourceLocation background = new ResourceLocation(AlternativeEnergy.modid.toLowerCase(), "textures/gui/powerBox.png");
    private final TileEntityLinkBox tileEntity;

    public GuiLinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox tileEntity) {
        super(new ContainerLinkBox(inventoryPlayer, tileEntity), background);
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int posX, int posY) {
        super.drawGuiContainerForegroundLayer(posX, posY);
        fontRenderer.drawString("Link Box", 6, 5, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 4, 0x000000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2;

        if (tileEntity.getPowerStored() > 0) {
            int showUntil = 0;

            for (int i = 1; i < 54; i++) {
                if (tileEntity.getPowerStored() >= i * (tileEntity.getMaxStoredPower() / 54)) {
                    showUntil = i;
                }
            }

            drawTexturedModalRect(posX + 7, posY + 70 - showUntil, 176, 54 - showUntil, 18, showUntil + 31);
        }
    }
}