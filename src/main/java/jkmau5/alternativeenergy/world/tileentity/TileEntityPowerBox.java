package jkmau5.alternativeenergy.world.tileentity;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Optional;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.gui.EnumGui;
import jkmau5.alternativeenergy.gui.GuiHandler;
import jkmau5.alternativeenergy.gui.slot.InvSlot;
import jkmau5.alternativeenergy.gui.slot.InvSlotCharge;
import jkmau5.alternativeenergy.gui.slot.InvSlotDisCharge;
import jkmau5.alternativeenergy.inventory.InventoryObject;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.world.item.AltEngItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:13
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class TileEntityPowerBox extends TileEntityPowerStorage implements IInventory {

    @Override
    protected void createSynchronizedFields() {
        super.createSynchronizedFields();
    }

    public final List<InvSlot> invSlots = Lists.newArrayList();

    public InvSlot capacitySlot;
    public InvSlot outputSpeedSlot;
    public InvSlotCharge chargeSlot;
    public InvSlotDisCharge dischargeSlot;

    public TileEntityPowerBox() {
        this.setInventory(new InventoryObject(4, "Power Box", 64));

        capacitySlot = new InvSlot(this, "capacity", 0, InvSlot.Access.NONE, 1, new ItemStack(AltEngItems.itemUpgrade, 1, 0));
        outputSpeedSlot = new InvSlot(this, "outputSpeed", 1, InvSlot.Access.NONE, 1, new ItemStack(AltEngItems.itemUpgrade, 1, 1));
        if(AlternativeEnergy.ICSupplied) {
            chargeSlot = new InvSlotCharge(this, 2);
            dischargeSlot = new InvSlotDisCharge(this, 3);
        }
    }

    @Override
    public void constructFromItemStack(ItemStack itemStack, EntityLivingBase entity) {
        if(itemStack.getTagCompound() == null || !itemStack.getTagCompound().hasKey("storedPower")) return;
        if(itemStack.getTagCompound().hasKey("capacityUpgrade")) {
            /*pBox.capacitySlot.put(new ItemStack(AltEngItems.itemUpgrade, itemStack.getTagCompound().getInteger("capacityUpgrade"), 0));

            for(int i=1; i<=pBox.capacitySlot.get().stackSize; i++) {
                pBox.maxPowers += i * Config.powerBox_capacity_multiplier;
            }*/
        }
        if(itemStack.getTagCompound().hasKey("outputSpeedUpgrade")) {
            /*pBox.outputSpeedSlot.put(new ItemStack(AltEngItems.itemUpgrade, itemStack.getTagCompound().getInteger("outputSpeedUpgrade"), 1));

            int tmpOutput = 32 * (4 ^ pBox.outputSpeedSlot.get().stackSize);
            if(tmpOutput > 512) tmpOutput = 512;
            pBox.maxOutput = tmpOutput;*/
        }
        this.setPowerStored(itemStack.getTagCompound().getInteger("storedPower"));
    }

    public String getType(){
        return "powerBox";
    }

    @Override
    public boolean openGui(EntityPlayer player) {
        GuiHandler.openGui(EnumGui.POWERBOX, player, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    @Override
    public boolean blockActivated(EntityPlayer player, int sideHit) {
        if(player.getHeldItem() != null && AlternativeEnergy.isWrench(player.getHeldItem().itemID)) {
            ForgeDirection side = ForgeDirection.getOrientation(sideHit);
            if(this.worldObj.isRemote) return true;
            if(player.isSneaking()) {
                return false;
            }else{
                this.setMode(side, this.getNextMode(this.getMode(side)));
                ChatMessageComponent component = ChatMessageComponent.createFromTranslationWithSubstitutions("altEng.chatmessage.storageSideModeChanged", this.getMode(side).toString().toLowerCase());
                player.sendChatToPlayer(component);
                return true;
            }
        }
        return super.blockActivated(player, sideHit);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        forceMaxPowersUpdate();
        forceOutputSpeedUpdate();
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(worldObj == null || worldObj.isRemote) return;

        if(AlternativeEnergy.ICSupplied) {
            fill_chargeSlot();
            empty_dischargeSlot();
        }
    }

    public void forceMaxPowersUpdate() {
        if(capacitySlot != null && capacitySlot.get() != null) {
            this.setMaxStoredPower(Config.powerBox_capacity);

            for(int i=1; i<=capacitySlot.get().stackSize; i++) {
                this.setMaxStoredPower(this.getMaxStoredPower() + (i * Config.powerBox_capacity_multiplier));
            }
        }else{
            this.setMaxStoredPower(Config.powerBox_capacity);
        }
    }

    public void forceOutputSpeedUpdate() {
        if(outputSpeedSlot != null && outputSpeedSlot.get() != null) {
            int tmpOutput = 32 * (4 ^ outputSpeedSlot.get().stackSize);
            if(tmpOutput > 512) tmpOutput = 512;

            this.setMaxOutput(tmpOutput);
        }else{
            this.setMaxOutput(32);
        }
    }

    //-----------------------------------------------------------------

    @Optional.Method(modid = "IC2")
    public void fill_chargeSlot() {
        if(!AlternativeEnergy.ICSupplied || chargeSlot == null)
            return;

        if(this.storedPower.getValue() >= Ratios.EU.conversion) {
            int sent = chargeSlot.charge(this.storedPower.getValue() * Ratios.EU.conversion);
            this.storedPower.subtract(sent / Ratios.EU.conversion);
        }
    }

    @Optional.Method(modid = "IC2")
    public void empty_dischargeSlot() {
        if(!AlternativeEnergy.ICSupplied || dischargeSlot == null)
            return;

        if (demandedEnergyUnits() > 0.0D) {
            int gain = dischargeSlot.discharge((int) demandedEnergyUnits(), false);

            this.euToConvert += gain;
        }
    }
}