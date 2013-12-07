package jkmau5.alternativeenergy.world.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public class TileEntityConveyor extends AltEngTileEntity implements IInventory, ISidedInventory {

    @Override
    public boolean canUpdate() {
        return false; //We don't use the ticks, so don't send them, to not rape your CPU with large conveyor networks
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isInvNameLocalized() {
        return true;
    }

    @Override
    public String getInvName() {
        return "Conveyor";
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        EntityItem item = new EntityItem(this.worldObj, this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, itemstack);
        this.worldObj.spawnEntityInWorld(item);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return new int[] {0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }
}
