package jkmau5.alternativeenergy.world.tileentity;

import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.network.synchronisation.objects.SynchronizedBoolean;
import jkmau5.alternativeenergy.network.synchronisation.objects.SynchronizedInteger;
import jkmau5.alternativeenergy.server.GuiHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Author: Lordmau5
 * Date: 02.11.13
 * Time: 23:22
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class TileEntityLinkBox extends TileEntityPowerStorage {

    public SynchronizedInteger linkedID;
    public SynchronizedBoolean isPrivate;

    @Override
    public int getGuiID() {
        return GuiHandlerServer.ID_GUI_LinkBox;
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

    public String getType(){
        return "linkBox";
    }

    public String getLinkIdentifier() {
        return String.format("%s:%d", this.getEnergyOwner(), this.linkedID.getValue());
    }

    public String getEnergyOwner() {
        return this.isPrivate.getValue() ? super.getOwner() : "public";
    }

    public void setLinkId(int linkId) {
        if(!this.worldObj.isRemote){
            AlternativeEnergy.linkBoxNetwork.removeFromLink(this, this.getLinkIdentifier());
            this.linkedID.setValue(linkId);
            AlternativeEnergy.linkBoxNetwork.addLinkBoxToNetwork(this, this.getLinkIdentifier());
        }
    }

    @Override
    public boolean removeBlockByPlayer(EntityPlayer player) {
        if(!this.isPrivate() && this.getEnergyOwner().equals(player.username)|| MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.username) || this.isPrivate() && this.getEnergyOwner().equals(player.username))
            return this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        if(!this.worldObj.isRemote){
            player.addChatMessage("This Link Box belongs to " + this.getOwner() + "!"); //TODO: localize!
        }
        return false;
    }

    public int getLinkId() {
        return this.linkedID.getValue();
    }

    public int getPowerStored() {
        if(this.linkedID.getValue() != 0){
            return AlternativeEnergy.linkBoxNetwork.getNetworkPower(getLinkIdentifier());
        }else{
            return super.getPowerStored();
        }
    }

    @Override
    public int getMaxStoredPower() {
        if(this.linkedID.getValue() != 0){
            return Config.powerBox_capacity;
        }else{
            return super.getMaxStoredPower();
        }
    }

    @Override
    public void setPowerStored(int power) {
        if(this.linkedID.getValue() != 0){
            AlternativeEnergy.linkBoxNetwork.setNetworkPower(getLinkIdentifier(), power);
        }else{
            super.setPowerStored(power);
        }
    }

    @Override
    public int getNeededPower() {
        if(this.linkedID.getValue() != 0){
            return AlternativeEnergy.linkBoxNetwork.neededPower(getLinkIdentifier());
        }else{
            return super.getNeededPower();
        }
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(worldObj == null || worldObj.isRemote) return;

        if(needsToInit) {
            needsToInit = false;
            AlternativeEnergy.linkBoxNetwork.addLinkBoxToNetwork(this, getLinkIdentifier());
            AlternativeEnergy.linkBoxNetwork.initiateNetworkPower(getLinkIdentifier(), needsToInit_Power);
        }
    }
}