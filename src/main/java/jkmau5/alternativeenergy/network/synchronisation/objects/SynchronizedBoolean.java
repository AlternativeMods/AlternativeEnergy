package jkmau5.alternativeenergy.network.synchronisation.objects;

import jkmau5.alternativeenergy.network.synchronisation.SynchronizedBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public class SynchronizedBoolean extends SynchronizedBase {

    private boolean value;

    public SynchronizedBoolean() {} //We need the empty constructor here for reflection
    public SynchronizedBoolean(boolean value) {
        this.value = value;
    }

    public void setValue(boolean newValue) {
        if (newValue != value) {
            value = newValue;
            markDirty();
        }
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public void readFromStream(DataInput stream) throws IOException {
        value = stream.readBoolean();
    }

    @Override
    public void writeToStream(DataOutput stream, boolean fullData) throws IOException {
        stream.writeBoolean(value);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String prefix) {
        if(!this.saveToNBT) {
            return;
        }
        tag.setBoolean(prefix, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String prefix) {
        if(!this.saveToNBT) {
            return;
        }
        value = tag.getBoolean(prefix);
    }

    public void toggle() {
        value = !value;
        markDirty();
    }
}
