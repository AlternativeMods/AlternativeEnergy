package jkmau5.alternativeenergy.util;

import jkmau5.alternativeenergy.network.synchronisation.SynchronizedBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public class BlockOutputMode extends SynchronizedBase {

    private EnumOutputMode[] modes = new EnumOutputMode[ForgeDirection.VALID_DIRECTIONS.length];

    public BlockOutputMode() {
        for(int i = 0; i < this.modes.length; i++) {
            this.modes[i] = EnumOutputMode.DISABLED;
        }
    }

    public EnumOutputMode getMode(ForgeDirection direction) {
        return this.modes[direction.ordinal()];
    }

    public EnumOutputMode setMode(ForgeDirection direction, EnumOutputMode mode) {
        if(this.getMode(direction) != mode) {
            this.modes[direction.ordinal()] = mode;
            this.markDirty();
        }
        return this.modes[direction.ordinal()];
    }

    @Override
    public void readFromStream(DataInput stream) throws IOException {
        int length = stream.readByte();
        this.modes = new EnumOutputMode[length];
        for(int i = 0; i < length; i ++) {
            this.modes[stream.readByte()] = EnumOutputMode.values()[stream.readByte()];
        }
    }

    @Override
    public void writeToStream(DataOutput stream, boolean fullData) throws IOException {
        stream.writeByte(this.modes.length);
        for(int i = 0; i < this.modes.length; i ++) {
            stream.writeByte(i);
            stream.writeByte(this.modes[i].ordinal());
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String name) {
        if(!this.saveToNBT) {
            return;
        }
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < this.modes.length; i ++) {
            NBTTagInt intTag = new NBTTagInt(i + "", this.modes[i].ordinal());
            list.appendTag(intTag);
        }
        nbt.setTag(name, list);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, String name) {
        if(!this.saveToNBT) {
            return;
        }
        NBTTagList list = nbt.getTagList(name);
        this.modes = new EnumOutputMode[list.tagCount()];
        for(int i = 0; i < list.tagCount(); i ++) {
            NBTTagInt intTag = (NBTTagInt) list.tagAt(i);
            this.modes[i] = EnumOutputMode.values()[intTag.data];
        }
    }
}
