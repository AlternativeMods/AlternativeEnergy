package jkmau5.alternativeenergy.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.inventory.container.ContainerLinkBox;
import jkmau5.alternativeenergy.network.PacketLinkboxFrequencyServerUpdate;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
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
    GuiButtonLock lock;

    public GUI_LinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox tileEntity) {
        super(new ContainerLinkBox(inventoryPlayer));
        this.tileEntity = tileEntity;
    }

    class GuiButtonLock extends GuiButton {
        ResourceLocation texture;

        public GuiButtonLock(int par1, int par2, int par3)
        {
            super(par1, par2, par3, 20, 20, "");
            texture = new ResourceLocation(AlternativeEnergy.modid.toLowerCase(), "textures/gui/lock.png");
        }

        public void drawButton(Minecraft par1Minecraft, int par2, int par3)
        {
            par2 -= guiLeft;
            par3 -= guiTop;

            par1Minecraft.getTextureManager().bindTexture(texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int hoverState = getHoverStateThis(this.field_82253_i);
            int privateState = getPrivateStateThis();
            this.drawTexturedModalRect(this.xPosition, this.yPosition, privateState * 128, hoverState * 128, this.width, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
        }

        int getHoverStateThis(boolean par1) {
            byte b0 = 0;

            if (par1)
                b0 = 1;
            if (!enabled)
                b0 = 2;

            return b0;
        }

        int getPrivateStateThis() {
            byte b = 1;
            if(tileEntity.getOwner().equals("public"))
                b = 0;
            return b;
        }
    }

    public void initGui() {
        super.initGui();
        linkId = new GuiTextField(fontRenderer, guiLeft + 131, guiTop + 17, 37, 16);
        linkId.setFocused(false);
        linkId.setMaxStringLength(4);

        if(tileEntity.getLinkId() != 0) {
            linkId.setText(String.valueOf(tileEntity.getLinkId()));
        }

        lock = new GuiButtonLock(0, 131, 35);
        if(!tileEntity.getRealOwner().equals(mc.thePlayer.username))
            lock.enabled = false;
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
        PacketDispatcher.sendPacketToServer(new PacketLinkboxFrequencyServerUpdate(tileEntity, Integer.parseInt(linkId.getText())).getPacket());
    }

    public void mouseClicked(int i, int j, int k){
        super.mouseClicked(i, j, k);
        linkId.mouseClicked(i, j, k);
        if(lock.mousePressed(mc, i - guiLeft, j - guiTop)){
            //TODO!
            //PacketHandler.sendPacketToServer(PacketHandler.PRIVATE_UPDATE_SERVER, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, PacketHandler.UNDEFINED);
        }
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

        lock.drawButton(mc, posX, posY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        mc.renderEngine.bindTexture(new ResourceLocation(AlternativeEnergy.modid.toLowerCase(), "textures/gui/linkBox.png"));
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