package jkmau5.alternativeenergy.client.gui;

import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.inventory.container.ContainerPowerBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

/**
 * Author: Lordmau5
 * Date: 23.08.13
 * Time: 14:21
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class GUI_PowerBox extends GuiContainer {

    TileEntityPowerBox tileEntity;

    public GUI_PowerBox(InventoryPlayer inventoryPlayer, TileEntityPowerBox tileEntity) {
        super(new ContainerPowerBox(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float floatVar) {
        super.drawScreen(mouseX, mouseY, floatVar);

        drawMouse(mouseX - guiLeft, mouseY - guiTop);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int posX, int posY) {
        fontRenderer.drawString("Power Box", 6, 4, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 4, 0x000000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        mc.renderEngine.bindTexture(new ResourceLocation(AlternativeEnergy.modid.toLowerCase(), "textures/gui/powerBox.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2;
        if(tileEntity.getPowerStored() > 0)
        {
            int showUntil = 0;

            for(int i=1; i<54; i++)
            {
                if(tileEntity.getPowerStored() >= i * (tileEntity.getMaxStoredPower() / 54))
                {
                    showUntil = i;
                }
            }

            drawTexturedModalRect(posX + 7, posY + 70 - showUntil, 176, 54 - showUntil, 18, showUntil + 31);
        }
    }

    private void drawMouse(int mouseX, int mouseY)
    {
        if(mouseX >= 7 && mouseY >= 16)
        {
            if(mouseX <= 23 && mouseY <= 69)
                drawCreativeTabHoveringText("Energy: " + Config.convertNumber(tileEntity.getPowerStored()) + "/" + Config.convertNumber(tileEntity.getMaxStoredPower()), mouseX + guiLeft, mouseY + guiTop);
        }
        if(mouseX >= 130 && mouseY >= 16 && mouseX <= 148 && mouseY <= 33 && tileEntity.capacitySlot.get() == null)  // Capacity Slot
            func_102021_a(Arrays.asList(new String[] {"This is the Capacity Slot.", "", "Put Capacity upgrades in", "to increase the maximum capacity."}),
                mouseX + guiLeft - (int) (fontRenderer.getStringWidth("to increase the maximum capacity.") * 1.1), mouseY + guiTop - (int) (3.75 * fontRenderer.FONT_HEIGHT));
        else if(mouseX >= 151 && mouseY >= 16 && mouseX <= 169 && mouseY <= 33 && tileEntity.outputSpeedSlot.get() == null) // Output Speed Slot
            func_102021_a(Arrays.asList(new String[] {"This is the Output Speed Slot.", "", "Put Output Speed upgrades in", "to increase the maximum output speed."}),
                mouseX + guiLeft - (int) (fontRenderer.getStringWidth("to increase the maximum output speed.") * 1.09), mouseY + guiTop - (int) (3.75 * fontRenderer.FONT_HEIGHT));

        if(AlternativeEnergy.ICSupplied) {
        if(mouseX >= 33 && mouseY >= 23 && mouseX <= 51 && mouseY <= 41 && tileEntity.chargeSlot.get() == null) // Charge Slot
            func_102021_a(Arrays.asList(new String[] {"This is the Charge Slot", "", "Put your electric items in here", "to charge them with energy."}),
                    mouseX + guiLeft - 5, mouseY + guiTop - (int) (3.75 * fontRenderer.FONT_HEIGHT));
        else if(mouseX >= 33 && mouseY >= 45 && mouseX <= 51 && mouseY <= 63 && tileEntity.dischargeSlot.get() == null) // DisCharge Slot
            func_102021_a(Arrays.asList(new String[] {"This is the Discharge Slot", "", "Put electric items (e.g. Batteries)", "in here to discharge them."}),
                    mouseX + guiLeft - 5, mouseY + guiTop - (int) (3.75 * fontRenderer.FONT_HEIGHT));
        }
    }

    private void debugMouse(int mouseX, int mouseY) {
        drawCreativeTabHoveringText("X: " + mouseX + " | Y: " + mouseY, mouseX + guiLeft, mouseY + guiTop);
    }
}