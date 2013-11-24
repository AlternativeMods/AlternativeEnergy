package jkmau5.alternativeenergy.util.interfaces;

import net.minecraft.nbt.NBTTagCompound;

/**
 * An common way of reading/writing from/to NBT. Not used for code, but used for an nice way of doing it
 *
 * @author jk-5
 */
public interface ISaveNBT {

    /**
     * Read the data from the {@link NBTTagCompound}
     * @param tag The data that can be read
     */
    void readFromNBT(NBTTagCompound tag);

    /**
     * Write the data to the {@link NBTTagCompound}
     * @param tag The tag to write the data to
     */
    void writeToNBT(NBTTagCompound tag);
}
