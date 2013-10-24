package tile;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import core.*;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import item.Items;
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
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class TileEntityPowerBox extends TileEntity implements IInventory, IEnergySink, IEnergySource, IEnergyStorage, IPowerReceptor, IPowerEmitter, IPeripheral {

    private String[] outputMode = new String[]{"disabled", "disabled", "disabled", "disabled", "disabled", "disabled"};
    private boolean addedToENet;
    private boolean hasToUpdateENet;

    public float storedPower;
    public float maxPowers = Config.powerBox_capacity;
    public int maxOutput;

    private PowerHandler convHandler;

    public final List<InvSlot> invSlots = new ArrayList();


    int oldEnergy;
    float oldMaxPowers;
    int oldMaxOutput;

    public final InvSlot capacitySlot;
    public final InvSlot outputSpeedSlot;
    public final InvSlotCharge chargeSlot;
    public final InvSlotDisCharge dischargeSlot;

    //--- Basic functions
    public TileEntityPowerBox() {
        capacitySlot = new InvSlot(this, "capacity", 0, InvSlot.Access.NONE, 1, new ItemStack(Items.upgrade_Capacity));
        outputSpeedSlot = new InvSlot(this, "outputSpeed", 1, InvSlot.Access.NONE, 1, new ItemStack(Items.upgrade_OutputSpeed));
        chargeSlot = new InvSlotCharge(this, 2);
        dischargeSlot = new InvSlotDisCharge(this, 3);

        addedToENet = false;

        convHandler = new PowerHandler(this, PowerHandler.Type.MACHINE);
        convHandler.configure(1, 500, 1337, 1000);
        convHandler.configurePowerPerdition(0, 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        storedPower = tag.getFloat("powerStored");

        for(int i=0; i<6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            outputMode[i] = tag.getString("outputSide_" + dir.toString().toLowerCase());
        }

        if(tag.hasKey("capacityUpgrade"))
            capacitySlot.put(new ItemStack(Items.upgrade_Capacity, tag.getInteger("capacityUpgrade"), 0));
        if(tag.hasKey("outputSpeedUpgrade"))
            outputSpeedSlot.put(new ItemStack(Items.upgrade_OutputSpeed, tag.getInteger("outputSpeedUpgrade"), 0));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setFloat("powerStored", storedPower);

        for(int i=0; i<6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            tag.setString("outputSide_" + dir.toString().toLowerCase(), outputMode[i]);
        }

        if(capacitySlot.get() != null)
            tag.setInteger("capacityUpgrade", capacitySlot.get().stackSize);
        if(outputSpeedSlot.get() != null)
            tag.setInteger("outputSpeedUpgrade", outputSpeedSlot.get().stackSize);
    }

    public int getPowerStored() {
        return (int) storedPower;
    }

    public void setPowerStored(int power) {
        storedPower = power;
        if(storedPower > maxPowers)
            storedPower = maxPowers;
        return;
    }

    public int getMaxPower() {
        return (int) maxPowers;
    }

    public void setMode(int side, String var) {
        if(side < 0 || side > 5) return;

        if(!var.equalsIgnoreCase("input") && !var.equalsIgnoreCase("output") && !var.equalsIgnoreCase("disabled"))
            return;

        outputMode[side] = var;
        hasToUpdateENet = true;
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

    private void convertBC() {
        setPowerStored(getPowerStored() + (int) Math.floor(convHandler.useEnergy(1, convHandler.getMaxEnergyStored(), true) / Ratios.MJ.conversion));
        //setPowerStored(getPowerStored() + (int) Config.convertInput(Ratios.MJ, convHandler.useEnergy(1, convHandler.getMaxEnergyStored(), true)));
    }

    @Override
    public void updateEntity()
    {
        if(worldObj == null || worldObj.isRemote)
            return;

        super.updateEntity();
        if (!addedToENet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            addedToENet = true;
        }

        if(hasToUpdateENet == true) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            hasToUpdateENet = false;
        }


        if(convHandler.getEnergyStored() > 0)
            convertBC();

        tryOutputtingEnergy();

        if(capacitySlot != null && capacitySlot.get() != null) {
            maxPowers = Config.powerBox_capacity;

            for(int i=1; i<=capacitySlot.get().stackSize; i++) {
                maxPowers += i * Config.powerBox_capacity_multiplier;
            }
        }
        else
            maxPowers = Config.powerBox_capacity;

        if(outputSpeedSlot != null && outputSpeedSlot.get() != null) {
            int tmpOutput = 32 * (4 ^ outputSpeedSlot.get().stackSize);
            if(tmpOutput > 512) tmpOutput = 512;

            maxOutput = tmpOutput;
        }
        else
            maxOutput = 32;

        if(storedPower > maxPowers)
            storedPower = maxPowers;
        if(storedPower == 1)
            storedPower = 0;

        if(storedPower >= 2) {
            int sent = chargeSlot.charge((int) (storedPower * Ratios.EU.conversion));

            storedPower -= sent / Ratios.EU.conversion;
        }

        if (demandedEnergyUnits() > 0.0D) {
            int gain = dischargeSlot.discharge((int) demandedEnergyUnits(), false);

            storedPower += Config.convertInput(Ratios.EU, gain);
        }

        if(storedPower < 0)
            storedPower = 0;

        if(oldEnergy != getPowerStored())
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        oldEnergy = getPowerStored();

        if(oldMaxPowers != maxPowers) {
            if(!worldObj.isRemote)
                if(capacitySlot.get() == null)
                    PacketHandler.sendPacketToPlayers(PacketHandler.CAPACITY_UPGRADE, xCoord, yCoord, zCoord, 0);
                else
                    PacketHandler.sendPacketToPlayers(PacketHandler.CAPACITY_UPGRADE, xCoord, yCoord, zCoord, capacitySlot.get().stackSize);
        }
        oldMaxPowers = maxPowers;

        if(oldMaxOutput != maxOutput) {
            if(!worldObj.isRemote) {
                if(outputSpeedSlot.get() == null)
                    PacketHandler.sendPacketToPlayers(PacketHandler.OUTPUTSPEED_UPGRADE, xCoord, yCoord, zCoord, 0);
                else
                    PacketHandler.sendPacketToPlayers(PacketHandler.OUTPUTSPEED_UPGRADE, xCoord, yCoord, zCoord, outputSpeedSlot.get().stackSize);
            }
        }
        oldMaxOutput = maxOutput;
    }

    @Override
    public void validate() {
        if(worldObj == null)
            return;
        if(worldObj.isRemote) {
            return;
        }

        if(addedToENet == false) {
            addedToENet = true;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }

        super.validate();
    }
    @Override
    public void invalidate() {
        if(worldObj == null || worldObj.isRemote)
            return;

        if(addedToENet == true) {
            addedToENet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        super.invalidate();
    }
    @Override
    public void onChunkUnload() {
        if(worldObj == null || worldObj.isRemote)
            return;

        if(addedToENet == true) {
            addedToENet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        super.onChunkUnload();
    }
    //------------------------------

    //--- IEnergySink
    @Override
    public double demandedEnergyUnits() {
        return maxPowers - storedPower;
    }

    @Override
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v) {
        double returning = 0;

        storedPower += Config.convertInput(Ratios.EU, v);
        if(storedPower > maxPowers) {
            returning = storedPower - maxPowers;
            storedPower = maxPowers;
        }
        return returning;
    }

    @Override
    public int getMaxSafeInput() {
        return 2048;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerBox) return false;

        if(outputMode[forgeDirection.ordinal()].equalsIgnoreCase("input"))
            return true;

        return false;
    }
    //---------------------------------

    //-- IEnergySource
    @Override
    public double getOfferedEnergy() {
        if(storedPower > 0) {
            if(getOutput() > (int) (storedPower / Ratios.EU.conversion))
                return (int) (storedPower / Ratios.EU.conversion);
            else
                return getOutput();
        }
        return 0;
    }

    @Override
    public void drawEnergy(double v) {
        storedPower -= v / Ratios.EU.conversion;
        if(storedPower < 0)
            storedPower = 0;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerBox) return false;

        if(outputMode[forgeDirection.ordinal()].equalsIgnoreCase("output"))
            return true;

        return false;
    }
    //---------------------------------

    //--- IEnergyStorage
    @Override
    public int getStored() {
        return (int) storedPower;
    }

    @Override
    public void setStored(int i) {
        storedPower = i;
        if(storedPower > maxPowers)
            storedPower = maxPowers;
        return;
    }

    @Override
    public int addEnergy(int i) {
        int returning = 0;

        storedPower += Config.convertInput(Ratios.EU, i);
        if(storedPower > maxPowers) {
            returning = (int) (storedPower - maxPowers);
            storedPower = maxPowers;
        }

        return returning;
    }

    @Override
    public int getCapacity() {
        return (int) maxPowers;
    }

    @Override
    public int getOutput() {
        return maxOutput;
    }

    @Override
    public double getOutputEnergyUnitsPerTick() {
        return 1;
    }

    @Override
    public boolean isTeleporterCompatible(ForgeDirection forgeDirection) {
        return false;
    }
    //---------------------------------

    //--- IPowerReceptor
    public void tryOutputtingEnergy() {
        if(storedPower <= 0)
            return;

        boolean[] conSides = new boolean[6];
        int connected = 0;
        for(int i=0; i<6; i++) {
            if(outputMode[i].equalsIgnoreCase("output")) {
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);
                if(tmpTile != null) {
                    if(tmpTile instanceof IPowerReceptor) {
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
            xPower = (int) storedPower;

        int equalPower = xPower / connected;
        if(Math.floor(equalPower) < Ratios.MJ.conversion)
            return;

        int drainPower = 0;
        for(int i=0; i<6; i++) {
            if(conSides[i] != false) {
                TileEntity tmptile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);

                if(tmptile instanceof IPowerReceptor) {
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

    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        if(!outputMode[side.ordinal()].equalsIgnoreCase("disabled"))
            return convHandler.getPowerReceiver();
        return null;
    }

    @Override
    public void doWork(PowerHandler workProvider) {

    }

    @Override
    public World getWorld() {
        return worldObj;
    }
    //---------------------------------

    //--- IPowerEmitter
    @Override
    public boolean canEmitPowerFrom(ForgeDirection side) {
        if(outputMode[side.ordinal()].equalsIgnoreCase("output"))
            return true;

        return false;
    }
    //---------------------------------

    //--- IPeripheral
    @Override
    public String getType() {
        return "powerBox";
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"setMode", "getMode", "getEnergyStored", "getCapacity"};
    }

    @Override
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
                            hasToUpdateENet = true;
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

    @Override
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }
    //-----------------------------------------------

    //--- IInventory
    @Override
    public int getSizeInventory() {
        int ret = 0;

        for (InvSlot invSlot : invSlots) {
            ret += invSlot.size();
        }

        return ret;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        for (InvSlot invSlot : invSlots) {
            if (slot < invSlot.size()) {
                return invSlot.get(slot);
            }
            slot -= invSlot.size();
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        for (InvSlot invSlot : invSlots) {
            if (slot < invSlot.size()) {
                invSlot.put(slot, stack);
                break;
            }
            slot -= invSlot.size();
        }
    }

    @Override
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

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack ret = getStackInSlot(slot);

        if (ret != null) setInventorySlotContents(slot, null);

        return ret;
    }

    @Override
    public int getInventoryStackLimit() {
        return 16;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        InvSlot invSlot = getInvSlot(slot);

        return (invSlot != null) && (invSlot.canInput()) && (invSlot.accepts(itemstack));
    }

    @Override
    public String getInvName() {
        return "pboxes.tileentitypbox";
    }

    @Override
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
            PacketHandler.sendPacketToPlayers(PacketHandler.CAPACITY_UPGRADE, xCoord, yCoord, zCoord, 0);
        else
            PacketHandler.sendPacketToPlayers(PacketHandler.CAPACITY_UPGRADE, xCoord, yCoord, zCoord, capacitySlot.get().stackSize);

        if(outputSpeedSlot.get() == null)
            PacketHandler.sendPacketToPlayers(PacketHandler.OUTPUTSPEED_UPGRADE, xCoord, yCoord, zCoord, 0);
        else
            PacketHandler.sendPacketToPlayers(PacketHandler.OUTPUTSPEED_UPGRADE, xCoord, yCoord, zCoord, outputSpeedSlot.get().stackSize);
    }
    //---------------------------------
}