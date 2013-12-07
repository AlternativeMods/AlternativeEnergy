package jkmau5.alternativeenergy.world.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.tile.IEnergyStorage;
import jkmau5.alternativeenergy.AltEngCompat;
import jkmau5.alternativeenergy.power.EnergyNetwork;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.util.CableConnectionMatrix;
import jkmau5.alternativeenergy.util.EnumOutputMode;
import net.minecraft.nbt.NBTTagCompound;
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
@Optional.InterfaceList(value = {
    @Optional.Interface(iface = "ic2.api.tile.IEnergyStorage", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
    @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy")
})
public class TileEntityPowerCable extends AltEngTileEntity implements IEnergyStorage, IEnergySink, IPowerReceptor {
    EnergyNetwork network;
    boolean initialized;
    boolean addedToENet;

    int euForNetwork;

    private CableConnectionMatrix connectionMatrix;

    private PowerHandler bcPowerHandler;

    public TileEntityPowerCable() {
        initializeNetwork();

        connectionMatrix = new CableConnectionMatrix();

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
        for(int i = 0; i < 6; i++) {
            ForgeDirection dr = ForgeDirection.getOrientation(i);
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

            if(tile != null) {
                boolean connect = false;
                if(tile instanceof TileEntityPowerCable) {
                    network = network.mergeNetworks(((TileEntityPowerCable)tile).network, network);
                    connect = true;
                } else if(AltEngCompat.isValidPowerTile(tile)) {
                    getEnergyNetwork().addInput(this, tile);
                    connect = true;
                }
                getConnectionMatrix().setConnected(dr, connect);
            } else {
                getConnectionMatrix().setConnected(dr, false);
            }
        }
    }

    public CableConnectionMatrix getConnectionMatrix() {
        return connectionMatrix;
    }

    public void updateEntity() {
        if(worldObj == null) {
            return;
        }
        if(worldObj.isRemote) {
            if(getConnectionMatrix().shouldUpdate()) {
                onNeighborChange();
                getConnectionMatrix().onUpdated();
            }
            return;
        }

        if(initialized == false) {
            onNeighborChange();
            initialized = true;
        }

        loadTile();

        if(euForNetwork % Ratios.EU.conversion == 0) {
            int toAdd = (int) (euForNetwork / Ratios.EU.conversion);

            network.addPower(toAdd);
            euForNetwork = 0;
        }

        tryOutputtingPBu();

        if(AltEngCompat.hasBC) {
            convertBC();
            tryOutputtingEnergy();
        }

        if(AltEngCompat.hasIC2) {
            tryOutputtingEU();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        network.setPower(tag.getInteger("storedPower"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("storedPower", network.getNetworkPower());
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(worldObj == null || worldObj.isRemote) {
            return;
        }

        unloadTile();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        if(worldObj == null || worldObj.isRemote) {
            return;
        }

        unloadTile();
    }

    //------------------------------------------------

    public void loadTile() {
        if(AltEngCompat.hasIC2 && !this.addedToENet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToENet = true;
        }
    }

    public void unloadTile() {
        if(AltEngCompat.hasIC2 && this.addedToENet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToENet = false;
        }
    }

    //-- IEnergyStorage
    @Optional.Method(modid = "IC2")
    public int getStored() {
        return (int) Math.ceil(network.networkPower / Ratios.EU.conversion);
    }

    @Optional.Method(modid = "IC2")
    public void setStored(int energy) {
        network.setPower((int) Math.ceil(energy / Ratios.EU.conversion));
    }

    @Optional.Method(modid = "IC2")
    public int addEnergy(int amount) {
        int returning = 0;

        euForNetwork += amount;
        if(euForNetwork > (Ratios.EU.conversion * 10000)) {
            returning = (int) (euForNetwork - (Ratios.EU.conversion * 10000));
            euForNetwork = (int) (Ratios.EU.conversion * 10000);
        }

        return returning;
    }

    @Optional.Method(modid = "IC2")
    public int getCapacity() {
        return network.maxNetworkPower;
    }

    @Optional.Method(modid = "IC2")
    public int getOutput() {
        return 32;
    }

    @Optional.Method(modid = "IC2")
    public double getOutputEnergyUnitsPerTick() {
        return 1;
    }

    @Optional.Method(modid = "IC2")
    public boolean isTeleporterCompatible(ForgeDirection side) {
        return false;
    }

    //------------------------------------------------

    public void tryOutputtingPBu() {
        for(ForgeDirection dr : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

            if(tmpTile == null) {
                continue;
            }

            if(tmpTile instanceof TileEntityPowerStorage) {
                TileEntityPowerStorage pBox = (TileEntityPowerStorage) tmpTile;

                if(pBox.getMode(dr.getOpposite()) != EnumOutputMode.INPUT) {
                    continue;
                }

                if(network.getNetworkPower() < 1) {
                    return;
                }

                if(pBox.getNeededPower() < 1) {
                    continue;
                }

                int toInsert = 25;
                if(toInsert > pBox.getMaxStoredPower() - pBox.getPowerStored()) {
                    toInsert = pBox.getMaxStoredPower() - pBox.getPowerStored();
                }
                if(toInsert > network.getNetworkPower()) {
                    toInsert = network.getNetworkPower();
                }

                pBox.setPowerStored(pBox.getPowerStored() + toInsert);
                network.drainPower(toInsert);
            }
        }
    }

    //-- IEnergySink

    public void tryOutputtingEU() {
        if(!AltEngCompat.hasIC2) {
            return;
        }
        if(network.getNetworkPower() < 1) {
            return;
        }

        for(int i = 0; i < 6; i++) {
            ForgeDirection dr = ForgeDirection.getOrientation(i);
            TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

            if(tmpTile != null) {
                if(tmpTile instanceof TileEntityPowerCable) {
                    continue;
                }

                if(tmpTile instanceof IEnergySink) {
                    IEnergySink sink = (IEnergySink) tmpTile;

                    if(sink.acceptsEnergyFrom(this, dr.getOpposite())) {
                        if(network.getNetworkPower() >= 1) {
                            int toDrain = Ratios.EU.conversion * 4;
                            if(sink.getMaxSafeInput() > toDrain) {
                                toDrain = (int) Math.floor(sink.getMaxSafeInput() / 5);
                            }
                            if(network.getNetworkPower() < toDrain) {
                                toDrain = network.getNetworkPower();
                            }
                            toDrain -= sink.injectEnergyUnits(dr.getOpposite(), toDrain * Ratios.EU.conversion);
                            if(toDrain < 0) {
                                toDrain = 0;
                            }
                            network.drainPower(toDrain);
                        }
                    }
                }
            }
        }
    }

    @Optional.Method(modid = "IC2")
    public double demandedEnergyUnits() {
        return network.maxNetworkPower - network.networkPower;
    }

    @Optional.Method(modid = "IC2")
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
        double returning = 0;

        euForNetwork += amount;
        if(euForNetwork > (Ratios.EU.conversion * 10000)) {
            returning = euForNetwork - (Ratios.EU.conversion * 10000);
            euForNetwork = (int) (Ratios.EU.conversion * 10000);
        }
        return returning;
    }

    @Optional.Method(modid = "IC2")
    public int getMaxSafeInput() {
        return 2147483647;
    }

    @Optional.Method(modid = "IC2")
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        if(emitter instanceof TileEntityPowerCable) {
            return false;
        }
        if(!AltEngCompat.isValidPowerTile(emitter)) {
            return false;
        }
        if(!getEnergyNetwork().isAcceptor(this, emitter)) {
            return true;
        }
        return true;
    }

    //------------------------------------------------

    //-- BC Stuffy

    @Optional.Method(modid = "BuildCraft|Energy")
    public void convertBC() {
        if(!AltEngCompat.hasBC) {
            return;
        }
        if(this.bcPowerHandler == null) {
            this.getPowerProvider();
        }
        if(this.bcPowerHandler.getEnergyStored() <= 0) {
            return;
        }
        network.addPower((int) Math.floor(this.bcPowerHandler.useEnergy(1, this.bcPowerHandler.getMaxEnergyStored(), true) / Ratios.MJ.conversion));
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void tryOutputtingEnergy() {
        if(!AltEngCompat.hasBC) {
            return;
        }

        if(network.networkPower <= Ratios.MJ.conversion) {
            return;
        }

        boolean[] conSides = new boolean[6];
        int connected = 0;
        for(ForgeDirection dr : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);
            if(tmpTile != null) {
                if(!AltEngCompat.isInvalidPowerTile(tmpTile) && !(tmpTile instanceof TileEntityPowerCable)) {
                    if(tmpTile instanceof IPowerReceptor) {
                        if(AltEngCompat.isICTile(tmpTile)) {
                            continue;
                        }
                        connected += 1;
                        conSides[dr.ordinal()] = true;
                    }
                }
            }
        }
        if(connected == 0) {
            return;
        }

        int equalPower = (int) Math.floor(network.networkPower / connected);

        int drainPower = 0;
        for(ForgeDirection dr : ForgeDirection.VALID_DIRECTIONS) {
            if(conSides[dr.ordinal()]) {
                TileEntity tmptile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

                if(tmptile instanceof IPowerReceptor && AltEngCompat.isValidPowerTile(tmptile) && !(tmptile instanceof TileEntityPowerCable)) {
                    if(((IPowerReceptor) tmptile).getPowerReceiver(dr.getOpposite()) != null) {
                        PowerHandler.PowerReceiver rec = ((IPowerReceptor)tmptile).getPowerReceiver(dr.getOpposite());
                        float neededPower = rec.powerRequest();
                        if(neededPower <= 0 || rec.getMaxEnergyStored() - rec.getEnergyStored() <= Ratios.MJ.conversion) {
                            continue;
                        }
                        if(neededPower > equalPower) {
                            neededPower = equalPower;
                        }

                        float restEnergy = rec.receiveEnergy(PowerHandler.Type.STORAGE, neededPower, dr.getOpposite());
                        drainPower += equalPower - restEnergy;
                    }
                }
            }
        }
        if(drainPower < 0) {
            return;
        }
        network.drainPower((int) Math.floor(drainPower / Ratios.MJ.conversion));
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler getPowerProvider() {
        if(this.bcPowerHandler == null) {
            this.bcPowerHandler = new PowerHandler(this, PowerHandler.Type.MACHINE);
            this.bcPowerHandler.configure(25, 500, 1337, 1000);
            this.bcPowerHandler.configurePowerPerdition(0, 0);
        }
        return this.bcPowerHandler;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
        if(tmpTile != null && AltEngCompat.isValidPowerTile(tmpTile)) {
            return getPowerProvider().getPowerReceiver();
        }
        return null;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void doWork(PowerHandler workProvider) {

    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public World getWorld() {
        return worldObj;
    }
    //---------------------------------
}