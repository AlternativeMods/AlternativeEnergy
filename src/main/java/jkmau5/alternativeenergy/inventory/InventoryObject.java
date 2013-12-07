package jkmau5.alternativeenergy.inventory;

import com.google.common.collect.Lists;
import jkmau5.alternativeenergy.AltEngLog;
import jkmau5.alternativeenergy.util.interfaces.IInventoryObjectEventListener;
import jkmau5.alternativeenergy.util.interfaces.ISaveNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.LinkedList;

/**
 * No description given
 *
 * @author jk-5
 */
public class InventoryObject implements IInventory, ISaveNBT {

    private ItemStack[] contents;
    private final String name;
    private final int stackLimit;
    private final LinkedList<IInventoryObjectEventListener> listeners = Lists.newLinkedList();

    public InventoryObject(int size, String name, int stackLimit) {
        this.contents = new ItemStack[size];
        this.name = name;
        this.stackLimit = stackLimit;
    }

    @Override
    public int getSizeInventory() {
        return this.contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.contents[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if(this.contents[slot] == null) {
            return null;
        }
        if(this.contents[slot].stackSize > count) {
            ItemStack ret = this.contents[slot].copy();
            ret.stackSize = count;
            this.contents[slot].stackSize = this.contents[slot].stackSize - count;
            return ret;
        }
        ItemStack ret = this.contents[slot].copy();
        this.contents[slot] = null;
        return ret;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if(this.contents[slot] == null) {
            return null;
        }
        ItemStack ret = this.contents[slot].copy();
        this.contents[slot] = null;
        return ret;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.contents[slot] = stack;
    }

    @Override
    public String getInvName() {
        return this.name;
    }

    @Override
    public boolean isInvNameLocalized() {
        //TODO ?
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return this.stackLimit;
    }

    @Override
    public void onInventoryChanged() {
        for(IInventoryObjectEventListener listener : this.listeners) {
            listener.onInventoryChanged(this);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true; //TODO: something with this?
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.readFromNBT(tag, "");
    }

    public void readFromNBT(NBTTagCompound tag, String prefix) {
        NBTTagList nbttaglist = tag.getTagList(prefix + "items");
        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
            NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist.tagAt(j);
            int index = nbttagcompound2.getInteger("index");
            if(index < this.contents.length) {
                this.contents[index] = ItemStack.loadItemStackFromNBT(nbttagcompound2);
            } else {
                AltEngLog.severe("InventoryObject: java.lang.ArrayIndexOutOfBoundsException: " + index + " of " + this.contents.length);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        this.writeToNBT(tag, "");
    }

    public void writeToNBT(NBTTagCompound tag, String prefix) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int j = 0; j < this.contents.length; ++j) {
            if (this.contents[j] != null && this.contents[j].stackSize > 0) {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttaglist.appendTag(nbttagcompound2);
                nbttagcompound2.setInteger("index", j);
                this.contents[j].writeToNBT(nbttagcompound2);
            }
        }
        tag.setTag(prefix + "items", nbttaglist);
        tag.setInteger(prefix + "itemsCount", this.contents.length);
    }
}
