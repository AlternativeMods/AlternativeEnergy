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
public class SynchronizedInteger extends SynchronizedBase {

    protected int value = 0;

    public SynchronizedInteger() {}
    public SynchronizedInteger(int value) {
        this.value = value;
    }

    @Override
    public void readFromStream(DataInput stream) throws IOException {
        value = stream.readInt();
    }

    public void modify(int by) {
        setValue(value + by);
    }

    public void setValue(int val) {
        if (val != value) {
            value = val;
            markDirty();
        }
    }

    public int getValue() {
        return value;
    }

    public void add(int val) {
        this.setValue(this.value + val);
    }

    public void subtract(int val) {
        this.setValue(this.value - val);
    }

    /**
     * Clamps the value on the given min and max values (inclusive)
     * @param min
     * @param max
     */
    public void clamp(int min, int max) {
        if(this.value < min) {
            this.setValue(min);
        }
        if(this.value > max) {
            this.setValue(max);
        }
    }

    public void clampMin(int min) {
        if(this.value < min) {
            this.setValue(min);
        }
    }

    public void clampMax(int max) {
        if(this.value > max) {
            this.setValue(max);
        }
    }

    @Override
    public void writeToStream(DataOutput stream, boolean fullData) throws IOException {
        stream.writeInt(value);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        if(!this.saveToNBT) {
            return;
        }
        tag.setInteger(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        if(!this.saveToNBT) {
            return;
        }
        if (tag.hasKey(name)) {
            value = tag.getInteger(name);
        }
    }
}
