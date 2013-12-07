package jkmau5.alternativeenergy.gui.container.slot;

import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.world.item.AltEngItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public class SlotOutputSpeedUpgrade extends AltEngSlot {

    public SlotOutputSpeedUpgrade(IInventory inventory, int slotIndex, int posX, int posY) {
        super(inventory, slotIndex, posX, posY);

        ToolTip tip = new ToolTip(1000);
        tip.add("Put output speed upgrades in this slot to raise the output speed");
        this.setToolTip(tip);
    }

    @Override
    public int getSlotStackLimit() {
        return 2;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() == AltEngItems.itemUpgrade && stack.getItemDamage() == 1;
    }
}
