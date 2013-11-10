package jkmau5.alternativeenergy.world.tileentity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngTileEntity extends TileEntity {

    @Getter
    @Setter
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

    /**
     * If you want to give the TileEntity data that is stored in the ItemStack, do it here
     *
     * @param itemStack The stack that this TileEntity was created from
     * @param entity The entity that created this TileEntity
     */
    public void constructFromItemStack(ItemStack itemStack, EntityLivingBase entity){}

    public boolean isOwner(String username){
        return this.owner.equals(username);
    }

    public final void sendUpdateToClient(){
        if(this.worldObj instanceof WorldServer){
            WorldServer world = (WorldServer) this.worldObj;
            PlayerInstance playerInstance = world.getPlayerManager().getOrCreateChunkWatcher(this.xCoord >> 4, this.zCoord >> 4, false);
            if(playerInstance != null){

            }
        }
    }
}
