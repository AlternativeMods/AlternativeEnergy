package lordmau5.powerboxes.client.gui;

import lordmau5.powerboxes.PowerBoxes;
import lordmau5.powerboxes.inventory.container.ContainerLinkBox;
import lordmau5.powerboxes.Config;
import lordmau5.powerboxes.network.PacketHandler;
import lordmau5.powerboxes.world.tileentity.TileEntityLinkBox;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

/**
 * Author: Lordmau5
 * Date: 03.11.13
 * Time: 11:29
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class GUI_LinkBox extends GuiContainer {

    TileEntityLinkBox tileEntity;
    GuiTextField linkId;

    public GUI_LinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox tileEntity) {
        super(new ContainerLinkBox(inventoryPlayer));
        this.tileEntity = PowerBoxes.linkBoxNetwork.getFirstOfLink(tileEntity.getLinkId());
        if(this.tileEntity == null)
            this.tileEntity = tileEntity;
    }

    public void initGui() {
        super.initGui();
        linkId = new GuiTextField(fontRenderer, guiLeft + 131, guiTop + 17, 37, 16);
        linkId.setFocused(false);
        linkId.setMaxStringLength(4);

        if(tileEntity.getLinkId() != 0) {
            linkId.setText(String.valueOf(tileEntity.getLinkId()));
        }
    }

    public void keyTyped(char c, int i){
        super.keyTyped(c, i);
        if(linkId.isFocused())
            checkForOnlyNumbers(c, i);
    }

    public void checkForOnlyNumbers(char c, int i) {
        if((c < '0' || c > '9') && c != '\b' && c != '\r')
            return;
        if(c == '\r')
            updateNetworkID();
        try {
            if(Integer.parseInt(linkId.getText() + c) > 1000)
                return;
        }
        catch(NumberFormatException e){}
        linkId.textboxKeyTyped(c, i);
    }

    public void updateNetworkID() {
        if(linkId.getText().isEmpty() || Integer.parseInt(linkId.getText()) == tileEntity.getLinkId())
            return;
        PacketHandler.sendPacketToServer(PacketHandler.NETWORKID_UPDATE_SERVER, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, Integer.parseInt(linkId.getText()));
    }

    public void mouseClicked(int i, int j, int k){
        super.mouseClicked(i, j, k);
        linkId.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float floatVar) {
        super.drawScreen(mouseX, mouseY, floatVar);

        drawMouse(mouseX - guiLeft, mouseY - guiTop);
        linkId.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int posX, int posY) {
        fontRenderer.drawString("Link Box", 6, 4, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 4, 0x000000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        mc.renderEngine.bindTexture(new ResourceLocation(PowerBoxes.modid.toLowerCase(), "textures/gui/linkBox.png"));
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
                if(tileEntity.getPowerStored() >= i * (tileEntity.getMaxPower() / 54))
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
            if(mouseX <= 23 && mouseY <= 69)
                func_102021_a(Arrays.asList(new String[]{"Energy: " + Config.convertNumber(tileEntity.getPowerStored()) + "/" + Config.convertNumber(tileEntity.getMaxPower()), "Linked ID: " + tileEntity.getLinkId()}), mouseX + guiLeft, mouseY + guiTop);
    }

}