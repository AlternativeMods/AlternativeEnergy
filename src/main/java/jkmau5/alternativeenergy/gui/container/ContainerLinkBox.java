package jkmau5.alternativeenergy.gui.container;

import jkmau5.alternativeenergy.gui.element.ElementIndicator;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class ContainerLinkBox extends ContainerLockable {

    private int lastTickPower;
    private final TileEntityLinkBox tileEntity;

    public ContainerLinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox te) {
        super(inventoryPlayer, te);
        this.tileEntity = te;

        this.addElement(new ElementIndicator(te.getEnergyIndicator(), 7, 16, 176, 0, 17, 54));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(ICrafting crafter : (List <ICrafting>) this.crafters) {
            if(this.lastTickPower != this.tileEntity.getPowerStored()) {
                crafter.sendProgressBarUpdate(this, 2, this.tileEntity.getPowerStored());
            }
        }
        this.lastTickPower = this.tileEntity.getPowerStored();
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 2, this.tileEntity.getPowerStored());
    }

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        if(id == 2) {
            this.tileEntity.guiPower = data;
        }
    }
}
