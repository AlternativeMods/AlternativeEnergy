package jkmau5.alternativeenergy.world.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.inventory.slot.InvSlot;
import jkmau5.alternativeenergy.inventory.slot.InvSlotCharge;
import jkmau5.alternativeenergy.inventory.slot.InvSlotDisCharge;
import jkmau5.alternativeenergy.network.PacketCapacityUpgrade;
import jkmau5.alternativeenergy.network.PacketOutputspeedUpgrade;
import jkmau5.alternativeenergy.power.EnergyNetwork;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.world.item.AltEngItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:13
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
@Optional.InterfaceList(value = {@Optional.Interface(iface = "ic2.api.tile.IEnergyStorage", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2"),

        @Optional.Interface(iface = "dan200.computer.api.IPeripheral", modid = "ComputerCraft"),

        @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy")})

public class TileEntityPowerBox extends TileEntity implements IInventory, IPeripheral, IEnergyStorage, IEnergySink, IEnergySource, IPowerReceptor {
    public String[] outputMode = new String[]{"disabled", "disabled", "disabled", "disabled", "disabled", "disabled"};
    public boolean addedToENet;

    public int storedPower;
    public int maxPowers = Config.powerBox_capacity;
    public int maxOutput;

    int euToConvert;

    int oldEnergy;

    public final List<InvSlot> invSlots = new ArrayList();

    public InvSlot capacitySlot;
    public InvSlot outputSpeedSlot;
    public InvSlotCharge chargeSlot;
    public InvSlotDisCharge dischargeSlot;


    //--- Basic functions
    public TileEntityPowerBox() {
        capacitySlot = new InvSlot(this, "capacity", 0, InvSlot.Access.NONE, 1, new ItemStack(AltEngItems.itemUpgrade, 1, 0));
        outputSpeedSlot = new InvSlot(this, "outputSpeed", 1, InvSlot.Access.NONE, 1, new ItemStack(AltEngItems.itemUpgrade, 1, 1));
        if(AlternativeEnergy.ICSupplied) {
            chargeSlot = new InvSlotCharge(this, 2);
            dischargeSlot = new InvSlotDisCharge(this, 3);
        }

        addedToENet = false;
    }

    public int getPowerStored() {
        return storedPower;
    }

    public void setPowerStored(int power) {
        storedPower = power;
        if(storedPower > maxPowers)
            storedPower = maxPowers;
        return;
    }

    public int neededPower() {
        return maxPowers - storedPower;
    }

    public int getMaxPower() {
        return maxPowers;
    }

    public void setMode(int side, String var) {
        if(side < 0 || side > 5) return;

        if(!var.equalsIgnoreCase("input") && !var.equalsIgnoreCase("output") && !var.equalsIgnoreCase("disabled"))
            return;

        outputMode[side] = var;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }
    public String getMode(int side) {
        if(side < 0 || side > 5) return "INVALID";

        return outputMode[side];
    }

    public String getNextMode(String mode) {
        if(mode.equalsIgnoreCase("input")) return "output";
        else if(mode.equalsIgnoreCase("output")) return "disabled";
        else if(mode.equalsIgnoreCase("disabled")) return "input";

        else return "INVALID";
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        readFromNBT(pkt.data);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        storedPower = tag.getInteger("powerStored");

        for(int i=0; i<6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            outputMode[i] = tag.getString("outputSide_" + dir.toString().toLowerCase());
        }

        if(tag.hasKey("capacityUpgrade"))
            capacitySlot.put(new ItemStack(AltEngItems.itemUpgrade, tag.getInteger("capacityUpgrade"), 0));
        if(tag.hasKey("outputSpeedUpgrade"))
            outputSpeedSlot.put(new ItemStack(AltEngItems.itemUpgrade, tag.getInteger("outputSpeedUpgrade"), 1));

        if(AlternativeEnergy.ICSupplied) {
            if(tag.hasKey("chargeSlot")) {
                chargeSlot.put(new ItemStack(Block.stone));
                chargeSlot.get().readFromNBT(tag.getCompoundTag("chargeSlot"));
            }
            if(tag.hasKey("dischargeSlot")) {
                dischargeSlot.put(new ItemStack(Block.stone));
                dischargeSlot.get().readFromNBT(tag.getCompoundTag("dischargeSlot"));
            }
        }

        forceMaxPowersUpdate();
        forceOutputSpeedUpdate();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("powerStored", storedPower);

        for(int i=0; i<6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            tag.setString("outputSide_" + dir.toString().toLowerCase(), outputMode[i]);
        }

        if(capacitySlot.get() != null)
            tag.setInteger("capacityUpgrade", capacitySlot.get().stackSize);
        if(outputSpeedSlot.get() != null)
            tag.setInteger("outputSpeedUpgrade", outputSpeedSlot.get().stackSize);

        if(AlternativeEnergy.ICSupplied) {
            if(chargeSlot.get() != null)
                tag.setCompoundTag("chargeSlot", chargeSlot.get().writeToNBT(new NBTTagCompound()));
            if(dischargeSlot.get() != null)
                tag.setCompoundTag("dischargeSlot", dischargeSlot.get().writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(worldObj == null || worldObj.isRemote)
            return;

        loadTile();

        if(euToConvert % Ratios.EU.conversion == 0) {
            if(storedPower < 1)
                storedPower = 1;

            int toAdd = (int) Math.ceil(euToConvert / Ratios.EU.conversion);

            storedPower += toAdd;
            euToConvert = 0;
        }

        outputToPowerCables();

        if(AlternativeEnergy.BCSupplied) {
            convertBC();
            tryOutputtingEnergy();
        }

        if(storedPower > maxPowers)
            storedPower = maxPowers;
        if(storedPower == 1)
            storedPower = 0;

        if(AlternativeEnergy.ICSupplied) {
            fill_chargeSlot();
            empty_dischargeSlot();
        }

        if(storedPower < 0)
            storedPower = 0;

        if(oldEnergy != getPowerStored())
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        oldEnergy = getPowerStored();
    }

    public void forceMaxPowersUpdate() {
        if(capacitySlot != null && capacitySlot.get() != null) {
            maxPowers = Config.powerBox_capacity;

            for(int i=1; i<=capacitySlot.get().stackSize; i++) {
                maxPowers += i * Config.powerBox_capacity_multiplier;
            }
        }
        else
            maxPowers = Config.powerBox_capacity;
    }

    public void forceOutputSpeedUpdate() {
        if(outputSpeedSlot != null && outputSpeedSlot.get() != null) {
            int tmpOutput = 32 * (4 ^ outputSpeedSlot.get().stackSize);
            if(tmpOutput > 512) tmpOutput = 512;

            maxOutput = tmpOutput;
        }
        else
            maxOutput = 32;
    }

    @Override
    public void validate() {
        super.validate();

        if(worldObj == null || worldObj.isRemote)
            return;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(worldObj == null || worldObj.isRemote)
            return;

        unloadTile();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        if(worldObj == null || worldObj.isRemote)
            return;

        unloadTile();
    }

    public void outputToPowerCables() {
        if(storedPower < 1.0)
            return;

        for(int i=0; i<6; i++) {
            if(outputMode[i].equalsIgnoreCase("output")) {
                ForgeDirection dr = ForgeDirection.getOrientation(i);
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

                if(tmpTile != null && tmpTile instanceof TileEntityPowerCable) {
                    TileEntityPowerCable cable = (TileEntityPowerCable) tmpTile;
                    EnergyNetwork network = cable.getEnergyNetwork();

                    if(network != null) {
                        if(storedPower >= 0.0) {
                            int toSend = (int) Math.ceil(storedPower);
                            if(storedPower > 5.0)
                                toSend = 5;

                            storedPower -= toSend - network.addPower(toSend);
                        }
                    }
                }
            }
        }
    }
    //------------------------------

    @Optional.Method(modid = "ComputerCraft")
    public String getType() {
        return "powerBox";
    }

    @Optional.Method(modid = "ComputerCraft")
    public String[] getMethodNames() {
        return new String[]{"setMode", "getMode", "getEnergyStored", "getCapacity"};
    }

    @Optional.Method(modid = "ComputerCraft")
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        if(worldObj.isRemote)
            return new Object[0];

        switch(method) {
            case 0: // setOutput
                if(arguments[0] instanceof String) {
                    boolean valid = false;
                    for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        if(arguments[0].toString().toUpperCase().equals(dir.name()))
                            valid = true;
                    }
                    if(valid == true) {
                        if(arguments[1] instanceof String) {
                            int side = ForgeDirection.valueOf(arguments[0].toString().toUpperCase()).ordinal();
                            String mode = (String) arguments[1];
                            if(!mode.equalsIgnoreCase("input") && !mode.equalsIgnoreCase("output") && !mode.equalsIgnoreCase("disabled"))
                                return new Object[] {"INVALID MODE!\nValid modes: input, output, disabled"};

                            if(outputMode[side].equalsIgnoreCase(mode)) {
                                return new Object[] {"Already \"" + mode + "\""};
                            }

                            outputMode[side] = mode;
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
                            return new Object[] { true };
                        }
                    }
                    else {
                        String sides = "";
                        for(int i=0; i<6; i++) {
                            sides = ForgeDirection.getOrientation(i).name().toLowerCase() + ", " + sides;
                        }
                        return new Object[] {"INVALID SIDE!\nSides available: " + sides};
                    }
                }
            case 1: // getOutput
                if(arguments[0] instanceof String) {
                    boolean valid = false;
                    for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        if(arguments[0].toString().toUpperCase().equals(dir.name())) {
                            valid = true;
                            break;
                        }
                    }
                    if(valid == true) {
                        return new Object[]{outputMode[ForgeDirection.valueOf(arguments[0].toString().toUpperCase()).ordinal()]};
                    }
                    else {
                        String sides = "";
                        for(int i=0; i<6; i++) {
                            sides = ForgeDirection.getOrientation(i).name().toLowerCase() + ", " + sides;
                        }
                        return new Object[] {"INVALID SIDE!\n", "Sides available: " + sides};
                    }
                }
            case 2: // getEnergyStored
                return new Object[]{Math.floor(storedPower)};
            case 3: // getCapacity
                return new Object[]{Math.floor(maxPowers)};
        }
        return new Object[]{"Error"};
    }

    @Optional.Method(modid = "ComputerCraft")
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Optional.Method(modid = "ComputerCraft")
    public void attach(IComputerAccess computer) {

    }

    @Optional.Method(modid = "ComputerCraft")
    public void detach(IComputerAccess computer) {

    }

    //-----------------------------------------------------------------

    @Optional.Method(modid = "IC2")
    public void fill_chargeSlot() {
        if(!AlternativeEnergy.ICSupplied || chargeSlot == null)
            return;

        if(storedPower >= Ratios.EU.conversion) {
            int sent = chargeSlot.charge((int) (storedPower * Ratios.EU.conversion));

            storedPower -= sent / Ratios.EU.conversion;
        }
    }

    @Optional.Method(modid = "IC2")
    public void empty_dischargeSlot() {
        if(!AlternativeEnergy.ICSupplied || dischargeSlot == null)
            return;

        if (demandedEnergyUnits() > 0.0D) {
            int gain = dischargeSlot.discharge((int) demandedEnergyUnits(), false);

            euToConvert += gain;
        }
    }

    public void loadTile() {
        if(!AlternativeEnergy.ICSupplied)
            return;

        if(addedToENet == false) {
            addedToENet = true;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
    }

    public void unloadTile() {
        if(!AlternativeEnergy.ICSupplied)
            return;

        if(addedToENet == true) {
            addedToENet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
    }

    @Optional.Method(modid = "IC2")
    public double demandedEnergyUnits() {
        return maxPowers - storedPower;
    }

    @Optional.Method(modid = "IC2")
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v) {
        double returning = 0;

        euToConvert += v;
        if(euToConvert > (Ratios.EU.conversion * 10000)) {
            returning = euToConvert - (Ratios.EU.conversion * 10000);
            euToConvert = (int) (Ratios.EU.conversion * 10000);
        }
        return returning;
    }

    @Optional.Method(modid = "IC2")
    public int getMaxSafeInput() {
        return 2147483647;
    }

    @Optional.Method(modid = "IC2")
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerBox || tileEntity instanceof TileEntityLinkBox) return false;
        if(tileEntity instanceof TileEntityPowerCable) return false;

        if(outputMode[forgeDirection.ordinal()].equalsIgnoreCase("input"))
            return true;

        return false;
    }
    //---------------------------------

    //-- IEnergySource
    @Optional.Method(modid = "IC2")
    public double getOfferedEnergy() {
        if(storedPower > 0) {
            if(getOutput() > (int) Math.ceil(storedPower / Ratios.EU.conversion))
                return (int) Math.ceil(storedPower / Ratios.EU.conversion);
            else
                return getOutput();
        }
        return 0;
    }

    @Optional.Method(modid = "IC2")
    public void drawEnergy(double v) {
        storedPower -= v / Ratios.EU.conversion;
        if(storedPower < 0)
            storedPower = 0;
    }

    @Optional.Method(modid = "IC2")
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerBox || tileEntity instanceof TileEntityLinkBox) return false;
        if(tileEntity instanceof TileEntityPowerCable) return false;

        if(outputMode[forgeDirection.ordinal()].equalsIgnoreCase("output"))
            return true;

        return false;
    }
    //---------------------------------

    //--- IEnergyStorage
    @Optional.Method(modid = "IC2")
    public int getStored() {
        return (int) storedPower;
    }

    @Optional.Method(modid = "IC2")
    public void setStored(int i) {
        storedPower = i;
        if(storedPower > maxPowers)
            storedPower = maxPowers;
        return;
    }

    @Optional.Method(modid = "IC2")
    public int addEnergy(int i) {
        int returning = 0;

        euToConvert += i;
        if(euToConvert > (Ratios.EU.conversion * 10000)) {
            returning = (int) (euToConvert - (Ratios.EU.conversion * 10000));
            euToConvert = (int) (Ratios.EU.conversion * 10000);
        }

        return returning;
    }

    @Optional.Method(modid = "IC2")
    public int getCapacity() {
        return maxPowers;
    }

    @Optional.Method(modid = "IC2")
    public int getOutput() {
        return maxOutput;
    }

    @Optional.Method(modid = "IC2")
    public double getOutputEnergyUnitsPerTick() {
        return 1;
    }

    @Optional.Method(modid = "IC2")
    public boolean isTeleporterCompatible(ForgeDirection forgeDirection) {
        return false;
    }

    //-------------------------------------------------------------------------------------

    @Optional.Method(modid = "BuildCraft|Energy")
    public void convertBC() {
        if(!AlternativeEnergy.BCSupplied)
            return;

        if(AlternativeEnergy.bcComp.getPowerHandler(this) == null)
            getPowerProvider();
        if(AlternativeEnergy.bcComp.getPowerHandler(this).getEnergyStored() <= 0)
            return;

        setPowerStored(getPowerStored() + (int) Math.floor(AlternativeEnergy.bcComp.getPowerHandler(this).useEnergy(1, AlternativeEnergy.bcComp.getPowerHandler(this).getMaxEnergyStored(), true) / Ratios.MJ.conversion));
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void tryOutputtingEnergy() {
        if(!AlternativeEnergy.BCSupplied)
            return;
        if(storedPower <= 0)
            return;

        boolean[] conSides = new boolean[6];
        int connected = 0;
        for(int i=0; i<6; i++) {
            if(outputMode[i].equalsIgnoreCase("output")) {
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);
                if(tmpTile != null) {
                    if(tmpTile instanceof IPowerReceptor && !(tmpTile instanceof TileEntityPowerCable)) {
                        connected += 1;
                        conSides[i] = true;
                    }
                }
            }
        }
        if(connected == 0) return;

        int xPower = 25;
        if(outputSpeedSlot != null && outputSpeedSlot.get() != null) {
            int tmpStackSize = outputSpeedSlot.get().stackSize;
            if(tmpStackSize > 2)
                tmpStackSize = 2;

            if(tmpStackSize == 1)
                xPower += 25;
            if(tmpStackSize == 2)
                xPower += 50;
        }
        if(xPower > storedPower)
            xPower = storedPower;

        int equalPower = xPower / connected;
        if(Math.floor(equalPower) < Ratios.MJ.conversion)
            return;

        int drainPower = 0;
        for(int i=0; i<6; i++) {
            if(conSides[i] != false) {
                TileEntity tmptile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);

                if(tmptile instanceof IPowerReceptor && !(tmptile instanceof TileEntityPowerCable) && !AlternativeEnergy.isInvalidPowerTile(tmptile)) {
                    if(((IPowerReceptor) tmptile).getPowerReceiver(ForgeDirection.getOrientation(i)) != null) {
                        PowerHandler.PowerReceiver rec = ((IPowerReceptor)tmptile).getPowerReceiver(ForgeDirection.getOrientation(i));
                        float neededPower = rec.powerRequest();
                        if(neededPower <= 0 || rec.getMaxEnergyStored() - rec.getEnergyStored() <= 5)
                            continue;
                        if(neededPower > equalPower)
                            neededPower = equalPower;

                        float restEnergy = rec.receiveEnergy(PowerHandler.Type.STORAGE, (float) Math.ceil(neededPower / Ratios.MJ.conversion), ForgeDirection.getOrientation(i).getOpposite());
                        drainPower += (equalPower - restEnergy);
                    }
                }
            }
        }
        storedPower -= drainPower / Ratios.MJ.conversion;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler getPowerProvider() {
        if(AlternativeEnergy.bcComp.getPowerHandler(this) == null)
        {
            AlternativeEnergy.bcComp.addPowerHandler(this, PowerHandler.Type.MACHINE);
            if (AlternativeEnergy.bcComp.getPowerHandler(this) != null) {
                AlternativeEnergy.bcComp.configurePowerHandler(AlternativeEnergy.bcComp.getPowerHandler(this), 25, 500, 1337, 1000);
                AlternativeEnergy.bcComp.configurePerdition(AlternativeEnergy.bcComp.getPowerHandler(this), 0, 0);
            }
        }
        return AlternativeEnergy.bcComp.getPowerHandler(this);
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
        if(!outputMode[side.ordinal()].equalsIgnoreCase("disabled") && tmpTile != null && !AlternativeEnergy.isInvalidPowerTile(tmpTile))
            return getPowerProvider().getPowerReceiver();
        return null;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void doWork(PowerHandler workProvider) {

    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public World getWorld() {
        return worldObj;
    }

    //---------------------------------------------------------------------------------------

    public int getSizeInventory() {
        int ret = 0;

        for (InvSlot invSlot : invSlots) {
            ret += invSlot.size();
        }

        return ret;
    }

    public ItemStack getStackInSlot(int slot) {
        for (InvSlot invSlot : invSlots) {
            if (slot < invSlot.size()) {
                return invSlot.get(slot);
            }
            slot -= invSlot.size();
        }

        return null;
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        for (InvSlot invSlot : invSlots) {
            if (slot < invSlot.size()) {
                invSlot.put(slot, stack);
                break;
            }
            slot -= invSlot.size();
        }
    }

    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack == null) return null;

        if (amt >= itemStack.stackSize) {
            setInventorySlotContents(slot, null);

            return itemStack;
        }

        itemStack.stackSize -= amt;

        ItemStack ret = itemStack.copy();
        ret.stackSize = amt;

        return ret;
    }

    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack ret = getStackInSlot(slot);

        if (ret != null) setInventorySlotContents(slot, null);

        return ret;
    }

    public int getInventoryStackLimit() {
        return 16;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        InvSlot invSlot = getInvSlot(slot);

        return (invSlot != null) && (invSlot.canInput()) && (invSlot.accepts(itemstack));
    }

    public String getInvName() {
        return "pboxes.tileentitypbox";
    }

    public boolean isInvNameLocalized() {
        return false;
    }

    //---------------------------------
    public void addInvSlot(InvSlot invSlot)
    {
        invSlots.add(invSlot);
    }

    private InvSlot getInvSlot(int index) {
        for (InvSlot invSlot : invSlots) {
            if (index < invSlot.size()) {
                return invSlot;
            }
            index -= invSlot.size();
        }

        return null;
    }

    public void onInventoryChanged()
    {
        if (worldObj == null || worldObj.isRemote)
            return;

        if(capacitySlot.get() == null)
            PacketDispatcher.sendPacketToAllPlayers(new PacketCapacityUpgrade(this, 0).getPacket());
        else
            PacketDispatcher.sendPacketToAllPlayers(new PacketCapacityUpgrade(this, capacitySlot.get().stackSize).getPacket());

        if(outputSpeedSlot.get() == null)
            PacketDispatcher.sendPacketToAllPlayers(new PacketOutputspeedUpgrade(this, 0).getPacket());
        else
            PacketDispatcher.sendPacketToAllPlayers(new PacketOutputspeedUpgrade(this, outputSpeedSlot.get().stackSize).getPacket());
    }
    //---------------------------------
}