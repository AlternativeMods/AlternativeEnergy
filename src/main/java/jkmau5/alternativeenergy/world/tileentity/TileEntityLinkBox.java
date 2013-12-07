package jkmau5.alternativeenergy.world.tileentity;

import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.gui.EnumGui;
import jkmau5.alternativeenergy.gui.GuiHandler;
import jkmau5.alternativeenergy.gui.button.LockButtonState;
import jkmau5.alternativeenergy.gui.button.MultiButtonController;
import jkmau5.alternativeenergy.network.synchronisation.objects.SynchronizedBoolean;
import jkmau5.alternativeenergy.network.synchronisation.objects.SynchronizedInteger;
import jkmau5.alternativeenergy.power.LinkBoxNetwork;
import jkmau5.alternativeenergy.util.Utils;
import jkmau5.alternativeenergy.util.interfaces.IGuiCloseSaveDataHandler;
import jkmau5.alternativeenergy.util.interfaces.ILockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The TileEntity for the linkbox. It is an {@link IGuiCloseSaveDataHandler}, so it is able to sync
 * gui data between client and server
 */
public class TileEntityLinkBox extends TileEntityPowerStorage implements ILockable, IGuiCloseSaveDataHandler {

    public SynchronizedInteger linkedID;
    public SynchronizedBoolean isPrivate;

    private final MultiButtonController lockController = MultiButtonController.getController(0, LockButtonState.VALUES);

    @Override
    public MultiButtonController getLockController() {
        return this.lockController;
    }

    @Override
    public boolean isLocked() {
        return this.lockController.getButtonState() == LockButtonState.LOCKED;
    }

    @Override
    public void writeGuiCloseData(DataOutput output) throws IOException {
        output.writeByte(this.lockController.getCurrentState());
    }

    @Override
    public void readGuiCloseData(DataInput input, EntityPlayer player) throws IOException {
        byte lock = input.readByte();
        if(player == null || !this.isLocked() || Utils.isOwnerOrOp(this, player.username)) {
            this.lockController.setCurrentState(lock);
        }
    }

    @Override
    protected void createSynchronizedFields() {
        this.linkedID = new SynchronizedInteger();
        this.isPrivate = new SynchronizedBoolean(false);

        this.setMaxOutput(20);
        super.createSynchronizedFields();
    }

    boolean needsToInit;
    int needsToInit_Power;

    public boolean isPrivate() {
        return this.isPrivate.getValue();
    }

    public void togglePrivate() {
        this.isPrivate.toggle();
        this.markBlockForUpdate();
    }

    public String getType() {
        return "linkBox";
    }

    @Override
    public boolean openGui(EntityPlayer player) {
        GuiHandler.openGui(EnumGui.LINKBOX, player, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    public String getLinkIdentifier() {
        return String.format("%s:%d", this.getEnergyOwner(), this.linkedID.getValue());
    }

    public String getEnergyOwner() {
        return this.isPrivate.getValue() ? super.getOwner() : "public";
    }

    public void setLinkId(int linkId) {
        if(!this.worldObj.isRemote) {
            LinkBoxNetwork.getInstance().removeFromLink(this, this.getLinkIdentifier());
            this.linkedID.setValue(linkId);
            LinkBoxNetwork.getInstance().addLinkBoxToNetwork(this, this.getLinkIdentifier());
        }
    }

    @Override
    public boolean removeBlockByPlayer(EntityPlayer player) {
        if(!this.isPrivate() && this.getEnergyOwner().equals(player.username) || MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.username) || this.isPrivate() && this.getEnergyOwner().equals(player.username)) {
            return this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        }
        if(!this.worldObj.isRemote) {
            player.addChatMessage("This Link Box belongs to " + this.getOwner() + "!"); //TODO: localize!
        }
        return false;
    }

    public int getLinkId() {
        return this.linkedID.getValue();
    }

    public int getPowerStored() {
        if(this.linkedID.getValue() != 0) {
            return LinkBoxNetwork.getInstance().getNetworkPower(getLinkIdentifier());
        } else {
            return super.getPowerStored();
        }
    }

    @Override
    public int getMaxStoredPower() {
        if(this.linkedID.getValue() != 0) {
            return Config.powerBox_capacity;
        } else {
            return super.getMaxStoredPower();
        }
    }

    @Override
    public void setPowerStored(int power) {
        if(this.linkedID.getValue() != 0) {
            LinkBoxNetwork.getInstance().setNetworkPower(getLinkIdentifier(), power);
        } else {
            super.setPowerStored(power);
        }
    }

    @Override
    public int getNeededPower() {
        if(this.linkedID.getValue() != 0) {
            return LinkBoxNetwork.getInstance().neededPower(getLinkIdentifier());
        } else {
            return super.getNeededPower();
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj == null || worldObj.isRemote) {
            return;
        }

        if(needsToInit) {
            needsToInit = false;
            LinkBoxNetwork.getInstance().addLinkBoxToNetwork(this, getLinkIdentifier());
            LinkBoxNetwork.getInstance().initiateNetworkPower(getLinkIdentifier(), needsToInit_Power);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.lockController.readFromNBT(tag, "lock");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        this.lockController.writeToNBT(tag, "lock");
        super.writeToNBT(tag);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }
}
