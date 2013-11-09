package jkmau5.alternativeenergy.world.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngTileEntity extends TileEntity {

    private String owner = "[AlternativeEnergy]";

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setString("owner", this.owner);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.owner = tag.getString("owner");
    }

    public String getOwner() {
        return owner;
    }

    public boolean isOwner(String username){
        return this.owner.equals(username);
    }
}
