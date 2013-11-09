package jkmau5.alternativeenergy.util.interfaces;

import net.minecraft.nbt.NBTTagCompound;

/**
 * No description given
 *
 * @author jk-5
 */
public interface ISaveNBT {

    void readFromNBT(NBTTagCompound tag);
    void writeToNBT(NBTTagCompound tag);
}
