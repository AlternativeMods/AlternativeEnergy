package jkmau5.alternativeenergy.gui.slot;

import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Author: Lordmau5
 * Date: 28.10.13
 * Time: 10:23
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class InvSlot {
    public final TileEntityPowerBox base;
    public final String name;
    public final int oldStartIndex;
    private final ItemStack[] contents;
    protected final Access access;
    public final InvSide preferredSide;

    private final ItemStack[] acceptIS;

    public InvSlot(TileEntityPowerBox base, String name, int oldStartIndex, Access access, int count, ItemStack allowedIS) {
        this(base, name, oldStartIndex, access, count, InvSide.ANY, new ItemStack[] {allowedIS});
    }

    public InvSlot(TileEntityPowerBox base, String name, int oldStartIndex, Access access, int count, ItemStack[] allowedIS) {
        this(base, name, oldStartIndex, access, count, InvSide.ANY, allowedIS);
    }

    public InvSlot(TileEntityPowerBox base, String name, int oldStartIndex, Access access, int count, InvSide preferredSide, ItemStack[] allowedIS) {
        contents = new ItemStack[count];

        this.base = base;
        this.name = name;
        this.oldStartIndex = oldStartIndex;
        this.access = access;
        this.preferredSide = preferredSide;

        this.acceptIS = allowedIS;

        //base.addInvSlot(this);
    }

    public void readFromNbt(NBTTagCompound nbtTagCompound) {
        NBTTagList contentsTag = nbtTagCompound.getTagList("Contents");

        for (int i = 0; i < contentsTag.tagCount(); i++) {
            NBTTagCompound contentTag = (NBTTagCompound)contentsTag.tagAt(i);

            int index = contentTag.getByte("Index") & 0xFF;
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(contentTag);
            if (itemStack != null) {
                put(index, itemStack);
            }
        }
    }

    public void writeToNbt(NBTTagCompound nbtTagCompound) {
        NBTTagList contentsTag = new NBTTagList();

        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                NBTTagCompound contentTag = new NBTTagCompound();

                contentTag.setByte("Index", (byte)i);
                contents[i].writeToNBT(contentTag);

                contentsTag.appendTag(contentTag);
            }
        }

        nbtTagCompound.setTag("Contents", contentsTag);
    }

    public int size() {
        return contents.length;
    }

    public ItemStack get() {
        return get(0);
    }

    public ItemStack get(int index) {
        return contents[index];
    }

    public void put(ItemStack content) {
        put(0, content);
    }

    public void put(int index, ItemStack content) {
        contents[index] = content;
    }

    public void clear() {
        for (int i = 0; i < contents.length; i++) {
            contents[i] = null;
        }
    }

    public boolean accepts(ItemStack itemStack) {
        if(acceptIS == null) {
            return true;
        }
        for(int i = 0; i < acceptIS.length; i++) {
            if(acceptIS[i] == null) {
                return true;
            }
            if(itemStack.itemID == acceptIS[i].itemID && itemStack.getItemDamage() == acceptIS[i].getItemDamage()) {
                return true;
            }
        }

        return false;
    }

    public boolean canInput() {
        return (access == Access.I) || (access == Access.IO);
    }

    public boolean canOutput() {
        return (access == Access.O) || (access == Access.IO);
    }

    public boolean isEmpty() {
        for (ItemStack itemStack : contents) {
            if (itemStack != null) {
                return false;
            }
        }

        return true;
    }

    public void organize() {
        for (int dstIndex = 0; dstIndex < contents.length - 1; dstIndex++) {
            ItemStack dst = contents[dstIndex];

            if ((dst == null) || (dst.stackSize < dst.getMaxStackSize())) {
                for (int srcIndex = dstIndex + 1; srcIndex < contents.length; srcIndex++) {
                    ItemStack src = contents[srcIndex];
                    if (src != null) {
                        if (dst == null) {
                            contents[srcIndex] = null;
                            ItemStack tmp85_83 = src;
                            dst = tmp85_83;
                            contents[dstIndex] = tmp85_83;
                        } else if (dst.itemID == src.itemID && dst.getItemDamage() == src.getItemDamage()) {
                            int space = dst.getMaxStackSize() - dst.stackSize;

                            if (src.stackSize <= space) {
                                contents[srcIndex] = null;
                                dst.stackSize += src.stackSize;
                            } else {
                                src.stackSize -= space;
                                dst.stackSize += space;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public String toString() {
        String ret = name + "[" + contents.length + "]: ";

        for (int i = 0; i < contents.length; i++) {
            ret = ret + contents[i];

            if (i < contents.length - 1) {
                ret = ret + ", ";
            }
        }

        return ret;
    }

    public static enum InvSide {
        ANY,
        TOP,
        BOTTOM,
        SIDE;

        public boolean matches(int side) {
            return (this == ANY) || ((side == 0) && (this == BOTTOM)) || ((side == 1) && (this == TOP)) || ((side >= 2) && (side <= 5) && (this == SIDE));
        }
    }

    public static enum Access {
        NONE,
        I,
        O,
        IO;
    }
}