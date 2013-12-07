package jkmau5.alternativeenergy.world.tileentity;

import jkmau5.alternativeenergy.inventory.InventoryObject;
import jkmau5.alternativeenergy.util.interfaces.IOwnable;
import jkmau5.alternativeenergy.util.interfaces.ISaveNBT;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngTileEntity extends TileEntity implements IInventory, IOwnable, ISaveNBT {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private InventoryObject inventory = null;

    @Getter
    @Setter
    private String owner = "[AlternativeEnergy]";

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setString("owner", this.owner);
        if(this.inventory != null) {
            this.inventory.writeToNBT(tag, "inventory");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.owner = tag.getString("owner");
        if(this.inventory != null) {
            this.inventory.readFromNBT(tag, "inventory");
        }
    }

    /**
     * If you want to give the TileEntity data that is stored in the ItemStack, do it here
     *
     * @param itemStack The stack that this TileEntity was created from
     * @param entity The entity that created this TileEntity
     */
    public void constructFromItemStack(ItemStack itemStack, EntityLivingBase entity) {}

    public boolean removeBlockByPlayer(EntityPlayer player) {
        return this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
    }

    public boolean isOwner(String username) {
        return this.owner.equals(username);
    }

    public final void markBlockForUpdate() {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public final void notifyBlocksOfNeighborChange() {
        this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

    public boolean blockActivated(EntityPlayer player, int side) {
        if(player.isSneaking()) {
            return false;
        }
        ItemStack holding = player.getCurrentEquippedItem();
        /*if(holding != null){
            //Do something item specific here
        }*/
        return this.openGui(player);
    }

    public boolean openGui(EntityPlayer player) {
        return false;
    }


    /******************** IInventory ********************/

    @Override
    public int getSizeInventory() {
        return this.inventory == null ? 0 : this.inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.inventory == null ? null : this.inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return this.inventory == null ? null : this.inventory.decrStackSize(i, j);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return this.inventory == null ? null : this.inventory.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if(this.inventory != null) {
            this.inventory.setInventorySlotContents(i, itemstack);
        }
    }

    @Override
    public String getInvName() {
        return this.inventory == null ? this.getClass().getSimpleName() : this.inventory.getInvName();
    }

    @Override
    public boolean isInvNameLocalized() {
        return this.inventory != null && this.inventory.isInvNameLocalized();
    }

    @Override
    public int getInventoryStackLimit() {
        return this.inventory == null ? 0 : this.inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.inventory != null && this.inventory.isUseableByPlayer(entityplayer);
    }

    @Override
    public void openChest() {
        if(this.inventory != null) {
            this.inventory.openChest();
        }
    }

    @Override
    public void closeChest() {
        if(this.inventory != null) {
            this.inventory.closeChest();
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return this.inventory != null && this.inventory.isItemValidForSlot(i, itemstack);
    }
}
