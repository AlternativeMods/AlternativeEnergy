package jkmau5.alternativeenergy.gui.container;

import jkmau5.alternativeenergy.AltEngProxy;
import jkmau5.alternativeenergy.gui.container.slot.SlotCapacityUpgrade;
import jkmau5.alternativeenergy.gui.container.slot.SlotElectricItemCharge;
import jkmau5.alternativeenergy.gui.container.slot.SlotOutputSpeedUpgrade;
import jkmau5.alternativeenergy.gui.element.ElementIndicator;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class ContainerPowerBox extends AltEngContainer {

    private int lastTickPower;
    private final TileEntityPowerBox tileEntity;

    public ContainerPowerBox(InventoryPlayer inventoryPlayer, TileEntityPowerBox te){
        super(te);
        this.tileEntity = te;

        this.addSlot(new SlotCapacityUpgrade(te.getInventory(), 0, 131, 17));
        this.addSlot(new SlotOutputSpeedUpgrade(te.getInventory(), 1, 152, 17));

        if(AltEngProxy.hasIC2()){
            this.addSlot(new SlotElectricItemCharge(te.getInventory(), 2, 33, 23));
            this.addSlot(new SlotElectricItemCharge(te.getInventory(), 3, 33, 45));
        }

        this.addElement(new ElementIndicator(te.getEnergyIndicator(), 10, 19, 176, 12, 6, 48));

        this.addPlayerInventory(inventoryPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(ICrafting crafter : (List<ICrafting>) this.crafters){
            if(this.lastTickPower != this.tileEntity.getPowerStored()){
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
        if(id == 2){
            this.tileEntity.guiPower = data;
        }
    }
}
