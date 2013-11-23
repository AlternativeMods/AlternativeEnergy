package jkmau5.alternativeenergy.client.gui;

import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.client.gui.button.GuiMultiButton;
import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.gui.container.ContainerLinkBox;
import jkmau5.alternativeenergy.gui.container.ContainerLockable;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiLinkBox extends TileEntityGuiContainer {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURE_DOMAIN, "textures/gui/linkBox.png");
    private final TileEntityLinkBox tileEntity;
    private String prevTickOwner = "Unknown";

    private ToolTip lockedToolTip;
    private ToolTip unlockedToolTip;

    private GuiMultiButton lockButton;

    public GuiLinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox tileEntity) {
        super(new ContainerLinkBox(inventoryPlayer, tileEntity), background, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        if(this.tileEntity == null) return;
        int w = (this.width - this.xSize) / 2;
        int h = (this.height - this.ySize) / 2;
        this.buttonList.clear();

        this.lockButton = new GuiMultiButton(1, w + 150, h + 8, 16, this.tileEntity.getLockController());
        this.buttonList.add(this.lockButton);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        if(this.tileEntity == null) return;
        this.updateButtons();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.updateButtons();

    }

    private void updateButtons(){
        this.lockButton.enabled = ((ContainerLockable) this.getContainer()).isCanLock();
        String owner = ((ContainerLinkBox) this.getContainer()).getOwner();
        if(!owner.equals(this.prevTickOwner)){
            this.prevTickOwner = owner;

        }
        //TODO: update lockbutton tooltips from here
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int posX, int posY) {
        super.drawGuiContainerForegroundLayer(posX, posY);
        fontRenderer.drawString("Link Box", 6, 5, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 4, 0x000000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

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
