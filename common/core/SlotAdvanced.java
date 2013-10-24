package core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tile.TileEntityPowerBox;

/**
 * Author: Lordmau5
 * Date: 23.08.13
 * Time: 17:18
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class SlotAdvanced extends Slot {

    private int stackLimit;
    private int index;
    public final InvSlot invSlot;

    public SlotAdvanced(InvSlot par1IInventory, int par2, int par3, int par4, int stackLimit) {
        super(par1IInventory.base, -1, par3, par4);
        this.stackLimit = stackLimit;
        this.index = par2;
        this.invSlot = par1IInventory;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return invSlot.accepts(itemStack);
    }

    public ItemStack getStack()
    {
        return invSlot.get(index);
    }

    @Override
    public int getSlotStackLimit() {
        return stackLimit;
    }

    @Override
    public void putStack(ItemStack par1ItemStack)
    {
        invSlot.put(index, par1ItemStack);
        this.onSlotChanged();
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
        ItemStack itemStack = invSlot.get(index);
        if (itemStack == null) return null;

        if (itemStack.stackSize <= amount) {
            invSlot.put(index, null);
            onSlotChanged();

            return itemStack;
        }
        ItemStack ret = itemStack.copy();
        ret.stackSize = amount;

        itemStack.stackSize -= amount;
        onSlotChanged();

        return ret;
    }

    @Override
    public boolean isSlotInInventory(IInventory inventory, int index)
    {
        if (inventory != this.invSlot.base) return false;

        for (InvSlot invSlot : this.invSlot.base.invSlots) {
            if (index < invSlot.size()) {
                return index == this.index;
            }
            index -= invSlot.size();
        }

        return false;
    }
}