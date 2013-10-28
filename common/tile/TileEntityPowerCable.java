package tile;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeTile;
import core.EnergyNetwork;
import core.Main;
import core.Ratios;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

/**
 * Author: Lordmau5
 * Date: 27.10.13
 * Time: 20:38
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
@Optional.InterfaceList(value = {@Optional.Interface(iface = "ic2.api.tile.IEnergyStorage", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2"),

        @Optional.Interface(iface = "dan200.computer.api.IPeripheral", modid = "ComputerCraft"),

        @Optional.Interface(iface = "buildcraft.api.power.IPowerEmitter", modid = "BuildCraft|Transport"),
        @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Transport")})

public class TileEntityPowerCable extends TileEntity implements IEnergyStorage, IEnergySink, IEnergySource, IPowerReceptor, IPowerEmitter {
    EnergyNetwork network;
    boolean initialized;
    boolean addedToENet;

    PowerHandler convHandler;

    int euForNetwork;

    public TileEntityPowerCable() {
        initializeNetwork();

        addedToENet = false;
    }

    public void initializeNetwork() {
        initialized = false;
        network = new EnergyNetwork(this);
    }

    public EnergyNetwork getEnergyNetwork() {
        return network;
    }

    public void setEnergyNetwork(EnergyNetwork newNetwork) {
        network = newNetwork;
    }

    public void onNeighborChange() {
        for(int i=0; i<6; i++) {
            ForgeDirection dr = ForgeDirection.getOrientation(i);
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

            if(tile != null && tile instanceof TileEntityPowerCable) {
                network = network.mergeNetworks(((TileEntityPowerCable)tile).network, network);
            }
        }
    }

    public void updateEntity() {
        if(worldObj == null || worldObj.isRemote)
            return;

        if(initialized == false) {
            onNeighborChange();
            initialized = true;
        }

        loadTile();

        if(euForNetwork >= Ratios.EU.conversion) {
            int toAdd = (int) Math.ceil(euForNetwork / Ratios.EU.conversion);

            network.addPower(toAdd);
            euForNetwork = 0;
        }

        convertBC();
        tryOutputtingEnergy();
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

    //------------------------------------------------

    public void loadTile() {
        if(!Main.ICSupplied)
            return;

        if(addedToENet == false) {
            addedToENet = true;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
    }

    public void unloadTile() {
        if(!Main.ICSupplied)
            return;

        if(addedToENet == true) {
            addedToENet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
    }

    //-- IEnergyStorage
    @Override
    public int getStored() {
        return network.networkPower;
    }

    @Override
    public void setStored(int energy) {
        network.setPower((int) Math.ceil(energy / Ratios.EU.conversion));
    }

    @Override
    public int addEnergy(int amount) {
        int returning = 0;

        euForNetwork += amount;
        if(euForNetwork > (Ratios.EU.conversion * 10000)) {
            returning = (int) (euForNetwork - (Ratios.EU.conversion * 10000));
            euForNetwork = (int) (Ratios.EU.conversion * 10000);
        }

        return returning;
    }

    @Override
    public int getCapacity() {
        return network.maxNetworkPower;
    }

    @Override
    public int getOutput() {
        return 64;
    }

    @Override
    public double getOutputEnergyUnitsPerTick() {
        return 1;
    }

    @Override
    public boolean isTeleporterCompatible(ForgeDirection side) {
        return false;
    }

    //------------------------------------------------

    //-- IEnergySink

    @Override
    public double demandedEnergyUnits() {
        return network.maxNetworkPower - network.networkPower;
    }

    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
        double returning = 0;

        euForNetwork += amount;
        if(euForNetwork > (Ratios.EU.conversion * 10000)) {
            returning = euForNetwork - (Ratios.EU.conversion * 10000);
            euForNetwork = (int) (Ratios.EU.conversion * 10000);
        }
        return returning;
    }

    @Override
    public int getMaxSafeInput() {
        return 2147483647;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        if(emitter instanceof TileEntityPowerCable)
            return false;
        return true;
    }

    //------------------------------------------------

    //-- IEnergySource

    @Override
    public double getOfferedEnergy() {
        if(network.networkPower > 0) {
            if(getOutput() > network.networkPower)
                return network.networkPower;
            else
                return getOutput();
        }
        return 0;
    }

    @Override
    public void drawEnergy(double amount) {
        network.drainPower((int) Math.ceil(amount / Ratios.EU.conversion));
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        if(receiver instanceof TileEntityPowerCable)
            return false;
        if(network.networkPower <= 0)
            return false;
        return true;
    }

    //------------------------------------------------

    //-- BC Stuffy

    public void convertBC() {
        if(convHandler == null)
            getPowerProvider();
        if(convHandler.getEnergyStored() <= 0)
            return;
        if(convHandler.getEnergyStored() < Ratios.MJ.conversion)
            return;

        network.addPower((int) Math.ceil(convHandler.useEnergy(1, convHandler.getMaxEnergyStored(), true) / Ratios.MJ.conversion));
    }

    public void tryOutputtingEnergy() {
        if(network.networkPower <= Ratios.MJ.conversion)
            return;

        boolean[] conSides = new boolean[6];
        int connected = 0;
        for(int i=0; i<6; i++) {
            TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);
            if(tmpTile != null) {
                if(tmpTile instanceof IPowerReceptor && !(tmpTile instanceof IPipeTile) && !(tmpTile instanceof TileEntityPowerCable)) {
                    connected += 1;
                    conSides[i] = true;
                }
            }
        }
        if(connected == 0) return;

        int equalPower = (int) Math.floor(network.networkPower / connected);

        int drainPower = 0;
        for(int i=0; i<6; i++) {
            if(conSides[i] != false) {
                TileEntity tmptile = worldObj.getBlockTileEntity(xCoord + ForgeDirection.getOrientation(i).offsetX, yCoord + ForgeDirection.getOrientation(i).offsetY, zCoord + ForgeDirection.getOrientation(i).offsetZ);

                if(tmptile instanceof IPowerReceptor && !(tmptile instanceof IPipeTile) && !(tmptile instanceof TileEntityPowerCable)) {
                    if(((IPowerReceptor) tmptile).getPowerReceiver(ForgeDirection.getOrientation(i)) != null) {
                        PowerHandler.PowerReceiver rec = ((IPowerReceptor)tmptile).getPowerReceiver(ForgeDirection.getOrientation(i));
                        float neededPower = rec.powerRequest();
                        if(neededPower <= 0 || rec.getMaxEnergyStored() - rec.getEnergyStored() <= Ratios.MJ.conversion)
                            continue;
                        if(neededPower > equalPower)
                            neededPower = equalPower;

                        float restEnergy = rec.receiveEnergy(PowerHandler.Type.STORAGE, (float) Math.floor(neededPower / Ratios.MJ.conversion), ForgeDirection.getOrientation(i).getOpposite());
                        drainPower += (equalPower - restEnergy);
                    }
                }
            }
        }
        network.drainPower((int) Math.floor(drainPower / Ratios.MJ.conversion));
    }

    public PowerHandler getPowerProvider() {
        if (convHandler == null)
        {
            convHandler = new PowerHandler(this, PowerHandler.Type.MACHINE);
            if (convHandler != null) {
                convHandler.configure(1, 1000, 1337, 5000);
                convHandler.configurePowerPerdition(0, 0);
            }
        }
        return convHandler;
    }

    @Optional.Method(modid = "BuildCraft|Transport")
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        return getPowerProvider().getPowerReceiver();
    }

    @Optional.Method(modid = "BuildCraft|Transport")
    public void doWork(PowerHandler workProvider) {

    }

    @Optional.Method(modid = "BuildCraft|Transport")
    public World getWorld() {
        return worldObj;
    }
    //---------------------------------

    //--- IPowerEmitter
    @Optional.Method(modid = "BuildCraft|Transport")
    public boolean canEmitPowerFrom(ForgeDirection side) {
        return true;
    }
}