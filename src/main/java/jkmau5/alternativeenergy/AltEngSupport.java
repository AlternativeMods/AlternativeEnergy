package jkmau5.alternativeenergy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: Lordmau5
 * Date: 04.12.13
 * Time: 16:36
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class AltEngSupport {
    public static void initiateNBTTag(ItemStack is) {
        if(is.getTagCompound() == null) {
            is.setTagCompound(new NBTTagCompound());
        }
    }

    public static boolean hasNBTKey(ItemStack is, String field) {
        NBTTagCompound tag = is.getTagCompound();
        return tag.hasKey(field);
    }

    public static int initiateOrGetNBTInteger(ItemStack is, String field) {
        NBTTagCompound tag = is.getTagCompound();
        if(!tag.hasKey(field)) {
            tag.setInteger(field, 0);
        }
        return tag.getInteger(field);
    }

    public static void setNBTInteger(ItemStack is, String field, int var) {
        is.getTagCompound().setInteger(field, var);
    }
}