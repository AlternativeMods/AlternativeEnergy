package jkmau5.alternativeenergy.gui.slot;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Author: Lordmau5
 * Date: 24.08.13
 * Time: 16:52
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class InvSlotCharge extends InvSlot {

    public InvSlotCharge(TileEntityPowerBox base, int oldStartIndex) {
        super(base, "charge", oldStartIndex, InvSlot.Access.IO, 1, InvSlot.InvSide.TOP, null);
    }

    public boolean accepts(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if ((item instanceof IElectricItem)) {
            return ((IElectricItem)item).getTier(itemStack) <= 3;
        }

        return false;
    }

    public IElectricItem getItem() {
        ItemStack itemStack = get(0);
        if (itemStack == null) {
            return null;
        }

        return (IElectricItem)itemStack.getItem();
    }

    public int charge(int amount) {
        ItemStack itemStack = get(0);
        if (itemStack == null) {
            return 0;
        }

        Item item = itemStack.getItem();

        if ((item instanceof IElectricItem)) {
            return ElectricItem.manager.charge(itemStack, amount, 3, false, false);
        }

        return 0;
    }
}