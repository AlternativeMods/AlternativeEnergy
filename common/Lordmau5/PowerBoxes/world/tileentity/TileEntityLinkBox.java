package lordmau5.powerboxes.world.tileentity;

import lordmau5.powerboxes.PowerBoxes;
import lordmau5.powerboxes.Config;
import lordmau5.powerboxes.network.PacketHandler;
import lordmau5.powerboxes.power.Ratios;
import lordmau5.powerboxes.power.EnergyNetwork;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: Lordmau5
 * Date: 02.11.13
 * Time: 23:22
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
@Optional.InterfaceList(value = {@Optional.Interface(iface = "ic2.api.tile.IEnergyStorage", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2"),

        @Optional.Interface(iface = "dan200.computer.api.IPeripheral", modid = "ComputerCraft"),

        @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy")})
public class TileEntityLinkBox extends TileEntity implements IPeripheral, IEnergyStorage, IEnergySink, IEnergySource, IPowerReceptor {
    int linkedId;
    String owner;
    boolean isPrivate;

    boolean needsToInit;
    int needsToInit_Power;

    int oldEnergy;
    int storedPower;
    int maxStoredPower = Config.powerBox_capacity;

    int euToConvert;
    boolean addedToENet;

    String[] outputMode = new String[]{"disabled", "disabled", "disabled", "disabled", "disabled", "disabled"};

    public TileEntityLinkBox() {
        isPrivate = false;
    }

    public void setLinkId(int linkId) {
        int oldLinkedId = linkedId;
        linkedId = linkId;

        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return;

        PowerBoxes.linkBoxNetwork.removeFromLink(this, oldLinkedId);
        PowerBoxes.linkBoxNetwork.addLinkBoxToNetwork(this, linkedId);
        PacketHandler.sendPacketToPlayers(PacketHandler.NETWORKID_UPDATE, xCoord, yCoord, zCoord, linkedId);
    }

    public int getLinkId() {
        return linkedId;
    }

    public int getPowerStored() {
        if(linkedId != 0)
            return PowerBoxes.linkBoxNetwork.getNetworkPower(linkedId);
        return storedPower;
    }

    public int getMaxPower() {
        if(linkedId != 0)
            return Config.powerBox_capacity;
        return maxStoredPower;
    }

    public void setPowerStored(int power) {
        if(linkedId != 0) {
            PowerBoxes.linkBoxNetwork.setNetworkPower(linkedId, power);
            return;
        }

        storedPower = power;
        if(storedPower > maxStoredPower)
            storedPower = maxStoredPower;
    }

    public int neededPower() {
        if(linkedId != 0)
            return PowerBoxes.linkBoxNetwork.neededPower(linkedId);
        return maxStoredPower - storedPower;
    }

    public void setOwner(String username) {
        owner = username;
    }

    public String getOwner() {
        if(owner == null || !isPrivate)
            return "public";
        return owner;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        storedPower = tag.getInteger("storedPower");
        for(int i=0; i<outputMode.length; i++)
            outputMode[i] = tag.getString("outputMode_" + i);
        owner = tag.getString("owner");
        isPrivate = tag.getBoolean("isPrivate");
        linkedId = tag.getInteger("linkId");

        if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
            if(linkedId != 0) {
                needsToInit = true;
                needsToInit_Power = tag.getInteger("linkId_storedPower");
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        for(int i=0; i<outputMode.length; i++)
            if(!outputMode[i].isEmpty())
                tag.setString("outputMode_" + i, outputMode[i]);
        tag.setInteger("storedPower", storedPower);
        tag.setString("owner", owner);
        tag.setBoolean("isPrivate", isPrivate);
        tag.setInteger("linkId", linkedId);
        if(linkedId != 0)
            tag.setInteger("linkId_storedPower", PowerBoxes.linkBoxNetwork.getNetworkPower(linkedId));
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(worldObj == null || worldObj.isRemote)
            return;

        if(needsToInit) {
            needsToInit = false;
            PowerBoxes.linkBoxNetwork.addLinkBoxToNetwork(this, linkedId);
            PowerBoxes.linkBoxNetwork.initiateNetworkPower(linkedId, needsToInit_Power);
        }

        loadTile();

        if(euToConvert % Ratios.EU.conversion == 0) {
            if(getPowerStored() < 1)
                setPowerStored(1);

            int toAdd = (int) Math.ceil(euToConvert / Ratios.EU.conversion);

            setPowerStored(getPowerStored() + toAdd);
            euToConvert = 0;
        }

        outputToPowerCables();

        if(PowerBoxes.BCSupplied) {
            convertBC();
            tryOutputtingEnergy();
        }

        if(getPowerStored() > maxStoredPower)
            setPowerStored(maxStoredPower);

        if(getPowerStored() <= 1.0)
            setPowerStored(0);

        if(oldEnergy != getPowerStored())
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        oldEnergy = getPowerStored();
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
        if(getPowerStored() < 1)
            return;

        for(int i=0; i<6; i++) {
            if(outputMode[i].equalsIgnoreCase("output")) {
                ForgeDirection dr = ForgeDirection.getOrientation(i);
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

                if(tmpTile != null && tmpTile instanceof TileEntityPowerCable) {
                    TileEntityPowerCable cable = (TileEntityPowerCable) tmpTile;
                    EnergyNetwork network = cable.getEnergyNetwork();

                    if(network != null) {
                        if(getPowerStored() < 1)
                            break;
                        int toSend = getPowerStored();
                        if(getPowerStored() > 5)
                            toSend = 5;

                        setPowerStored(getPowerStored() - toSend - network.addPower(toSend));
                    }
                }
            }
        }
        if(getPowerStored() < 0)
            setPowerStored(0);
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

    //------------------------------------------------------------------------------------------------------------------
    //------- IPeripheral

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String[] getMethodNames() {
        return new String[0];
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        return new Object[0];
    }

    @Override
    public boolean canAttachToSide(int side) {
        return false;
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    //------------------------------------------------------------------------------------------------------------------
    //------- IEnergySink

    public void loadTile() {
        if(!PowerBoxes.ICSupplied)
            return;

        if(!addedToENet) {
            addedToENet = true;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
    }

    public void unloadTile() {
        if(!PowerBoxes.ICSupplied)
            return;

        if(addedToENet) {
            addedToENet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
    }

    @Optional.Method(modid = "IC2")
    public double demandedEnergyUnits() {
        return maxStoredPower - getPowerStored();
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

    //------------------------------------------------------------------------------------------------------------------
    //------- IEnergyAcceptor

    @Optional.Method(modid = "IC2")
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerBox || tileEntity instanceof TileEntityLinkBox) return false;
        if(tileEntity instanceof TileEntityPowerCable) return false;

        if(outputMode[forgeDirection.ordinal()].equalsIgnoreCase("input"))
            return true;

        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    //------- IEnergySource

    @Optional.Method(modid = "IC2")
    public double getOfferedEnergy() {
        if(getPowerStored() > 0) {
            if(getOutput() > (int) Math.ceil(getPowerStored() / Ratios.EU.conversion))
                return (int) Math.ceil(getPowerStored() / Ratios.EU.conversion);
            else
                return getOutput();
        }
        return 0;
    }

    @Optional.Method(modid = "IC2")
    public void drawEnergy(double v) {
        setPowerStored((int) Math.ceil(getPowerStored() - (v / Ratios.EU.conversion)));
        if(getPowerStored() < 0)
            setPowerStored(0);
    }

    //------------------------------------------------------------------------------------------------------------------
    //------- IEnergyEmitter

    @Optional.Method(modid = "IC2")
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerBox) return false;
        if(tileEntity instanceof TileEntityPowerCable) return false;

        if(outputMode[forgeDirection.ordinal()].equalsIgnoreCase("output"))
            return true;

        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    //------- IEnergyStorage

    @Optional.Method(modid = "IC2")
    public int getStored() {
        return getPowerStored();
    }

    @Optional.Method(modid = "IC2")
    public void setStored(int i) {
        setPowerStored(i);
        if(getPowerStored() > maxStoredPower)
            setPowerStored(maxStoredPower);
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
        return maxStoredPower;
    }

    @Optional.Method(modid = "IC2")
    public int getOutput() {
        return 20;
    }

    @Optional.Method(modid = "IC2")
    public double getOutputEnergyUnitsPerTick() {
        return 1;
    }

    @Optional.Method(modid = "IC2")
    public boolean isTeleporterCompatible(ForgeDirection forgeDirection) {
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    //------- IPowerReceptor

    @Optional.Method(modid = "BuildCraft|Energy")
    public void convertBC() {
        if(!PowerBoxes.BCSupplied)
            return;

        if(PowerBoxes.bcComp.getPowerHandler(this) == null)
            getPowerProvider();
        if(PowerBoxes.bcComp.getPowerHandler(this).getEnergyStored() <= 0)
            return;

        setPowerStored(getPowerStored() + (int) Math.floor(PowerBoxes.bcComp.getPowerHandler(this).useEnergy(1, PowerBoxes.bcComp.getPowerHandler(this).getMaxEnergyStored(), true) / Ratios.MJ.conversion));
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void tryOutputtingEnergy() {
        if(!PowerBoxes.BCSupplied)
            return;
        if(getPowerStored() <= 0)
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
        if(xPower > getPowerStored())
            xPower = getPowerStored();

        int equalPower = xPower / connected;
        if(Math.floor(equalPower) < Ratios.MJ.conversion)
            return;

        int drainPower = 0;
        for(int i=0; i<6; i++) {
            if(conSides[i]) {
                TileEntity tmptile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);

                if(tmptile instanceof IPowerReceptor && !(tmptile instanceof TileEntityPowerCable) && !PowerBoxes.isInvalidPowerTile(tmptile)) {
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
        setPowerStored((int) Math.floor(getPowerStored() - (drainPower / Ratios.MJ.conversion)));
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler getPowerProvider() {
        if(PowerBoxes.bcComp.getPowerHandler(this) == null)
        {
            PowerBoxes.bcComp.addPowerHandler(this, PowerHandler.Type.MACHINE);
            if (PowerBoxes.bcComp.getPowerHandler(this) != null) {
                PowerBoxes.bcComp.configurePowerHandler(PowerBoxes.bcComp.getPowerHandler(this), 25, 500, 1337, 1000);
                PowerBoxes.bcComp.configurePerdition(PowerBoxes.bcComp.getPowerHandler(this), 0, 0);
            }
        }
        return PowerBoxes.bcComp.getPowerHandler(this);
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
        if(!outputMode[side.ordinal()].equalsIgnoreCase("disabled") && tmpTile != null && !PowerBoxes.isInvalidPowerTile(tmpTile))
            return getPowerProvider().getPowerReceiver();
        return null;
    }

    @Override
    public void doWork(PowerHandler powerHandler) {
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    //------------------------------------------------------------------------------------------------------------------
}