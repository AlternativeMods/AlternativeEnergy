package jkmau5.alternativeenergy.gui.container;

import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.gui.container.slot.SlotCapacityUpgrade;
import jkmau5.alternativeenergy.gui.container.slot.SlotElectricItemCharge;
import jkmau5.alternativeenergy.gui.container.slot.SlotOutputSpeedUpgrade;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * No description given
 *
 * @author jk-5
 */
public class ContainerPowerBox extends AltEngContainer {

    public ContainerPowerBox(InventoryPlayer inventoryPlayer, TileEntityPowerBox te){
        super(te);

        this.addSlot(new SlotCapacityUpgrade(te.getInventory(), 0, 131, 17));
        this.addSlot(new SlotOutputSpeedUpgrade(te.getInventory(), 1, 152, 17));

        if(AlternativeEnergy.ICSupplied){
            this.addSlot(new SlotElectricItemCharge(te.getInventory(), 2, 33, 23));
            this.addSlot(new SlotElectricItemCharge(te.getInventory(), 3, 33, 45));
        }

        this.addPlayerInventory(inventoryPlayer);
    }
}
