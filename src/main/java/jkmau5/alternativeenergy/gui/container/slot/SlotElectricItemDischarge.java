package jkmau5.alternativeenergy.gui.container.slot;

import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public class SlotElectricItemDischarge extends AltEngSlot {

    public SlotElectricItemDischarge(IInventory inventory, int slotIndex, int posX, int posY) {
        super(inventory, slotIndex, posX, posY);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack == null) {
            return false;
        }

        Item item = stack.getItem();
        if(item instanceof IElectricItem) {
            return ((IElectricItem) item).canProvideEnergy(stack) && ((IElectricItem) item).getTier(stack) <= 3;
        }
        return item == ic2.api.item.Items.getItem("suBattery").getItem();
    }
}
