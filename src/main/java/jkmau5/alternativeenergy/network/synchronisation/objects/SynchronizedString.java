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
public class SynchronizedString extends SynchronizedBase {

    protected String value = "";

    public SynchronizedString() {}
    public SynchronizedString(String value) {
        this.value = value;
    }

    @Override
    public void readFromStream(DataInput stream) throws IOException {
        boolean isNull = stream.readBoolean();
        if(!isNull) {
            value = stream.readUTF();
        } else {
            value = null;
        }
    }

    public void setValue(String val) {
        if(!val.equals(this.value)) {
            this.value = val;
            markDirty();
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public void writeToStream(DataOutput stream, boolean fullData) throws IOException {
        stream.writeBoolean(this.value == null);
        if(this.value != null) {
            stream.writeUTF(this.value);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        if(!this.saveToNBT) {
            return;
        }
        tag.setString(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        if(!this.saveToNBT) {
            return;
        }
        if (tag.hasKey(name)) {
            value = tag.getString(name);
        }
    }
}
