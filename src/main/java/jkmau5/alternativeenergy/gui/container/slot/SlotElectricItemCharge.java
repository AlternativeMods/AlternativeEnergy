package jkmau5.alternativeenergy.gui.container.slot;

import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public class SlotElectricItemCharge extends AltEngSlot {

    public SlotElectricItemCharge(IInventory inventory, int slotIndex, int posX, int posY) {
        super(inventory, slotIndex, posX, posY);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IElectricItem && ((IElectricItem) stack.getItem()).getTier(stack) <= 3;
    }
}
