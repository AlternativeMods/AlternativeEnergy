package jkmau5.alternativeenergy.world.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.common.Optional;
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
import jkmau5.alternativeenergy.network.synchronisation.ISynchronized;
import jkmau5.alternativeenergy.network.synchronisation.objects.SynchronizedInteger;
import jkmau5.alternativeenergy.power.EnergyNetwork;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.util.BlockOutputMode;
import jkmau5.alternativeenergy.util.EnumOutputMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
@Optional.InterfaceList({
    @Optional.Interface(iface = "dan200.computer.api.IPeripheral", modid = "ComputerCraft"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.tile.IEnergyStorage", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2"),
    @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy")
})
public abstract class TileEntityPowerStorage extends SynchronizedTileEntity implements IPeripheral, IEnergySink, IEnergyStorage, IEnergySource, IPowerReceptor {

    protected boolean addedToEnet = false;
    protected int euToConvert = 0;
    private int oldEnergy = 0;

    @Setter(AccessLevel.PROTECTED) private int maxOutput = 32;
    @Getter @Setter(AccessLevel.PROTECTED) private int maxStoredPower = Config.powerBox_capacity;

    //Synchronized variables
    public BlockOutputMode outputMode;
    public SynchronizedInteger storedPower;

    @Override
    protected void createSynchronizedFields() {
        this.storedPower = new SynchronizedInteger();
        this.outputMode = new BlockOutputMode();
    }

    @Override
    public void onSynced(List<ISynchronized> changes){
        for(ISynchronized sync : changes){
            if(sync instanceof BlockOutputMode){
                this.markBlockForUpdate();
            }
        }
    }

    public void setMode(ForgeDirection side, EnumOutputMode var) {
        if(side == ForgeDirection.UNKNOWN) return;
        this.outputMode.setMode(side, var);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

    public EnumOutputMode getMode(ForgeDirection side) {
        if(side == ForgeDirection.UNKNOWN) return null;
        return this.outputMode.getMode(side);
    }

    public int getPowerStored() {
        return this.storedPower.getValue();
    }

    public int getNeededPower() {
        return this.getMaxStoredPower() - this.storedPower.getValue();
    }

    public void setPowerStored(int storage){
        this.storedPower.setValue(storage);
        if(storedPower.getValue() > this.maxStoredPower){
            storedPower.setValue(this.maxStoredPower);
        }
    }

    public EnumOutputMode getNextMode(EnumOutputMode mode){
        int ordinal = mode.ordinal() + 1;
        if(ordinal > EnumOutputMode.values().length - 1) ordinal = 0;
        return EnumOutputMode.values()[ordinal];
    }

    @Override
    public void invalidate(){
        if(!this.worldObj.isRemote) this.onUnload();
    }

    @Override
    public void onChunkUnload(){
        if(!this.worldObj.isRemote) this.onUnload();
    }

    private void onUnload() {
        if(AlternativeEnergy.ICSupplied){
            if(this.addedToEnet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                this.addedToEnet = false;
            }
        }
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(worldObj == null || worldObj.isRemote) return;

        if(AlternativeEnergy.ICSupplied){
            if(!this.addedToEnet){
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                this.addedToEnet = true;
            }
        }

        if(this.euToConvert % Ratios.EU.conversion == 0) {
            this.storedPower.clampMin(1);
            this.storedPower.add((int) Math.ceil(euToConvert / Ratios.EU.conversion));
            this.euToConvert = 0;
        }

        if(AlternativeEnergy.BCSupplied) {
            this.convertBC();
            this.tryOutputtingEnergy();
        }

        this.outputToPowerCables();

        if(this.storedPower.getValue() <= 1) this.storedPower.setValue(0);

        this.storedPower.clampMin(0);
        this.storedPower.clampMax(this.maxStoredPower);

        if(oldEnergy != getPowerStored()) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        oldEnergy = getPowerStored();
    }

    private void outputToPowerCables() {
        if(this.storedPower.getValue() < 1) return;

        for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if(this.outputMode.getMode(side) == EnumOutputMode.OUTPUT){
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);

                if(tmpTile != null && tmpTile instanceof TileEntityPowerCable) {
                    TileEntityPowerCable cable = (TileEntityPowerCable) tmpTile;
                    EnergyNetwork network = cable.getEnergyNetwork();

                    if(network != null) {
                        if(this.storedPower.getValue() >= 0.0) {
                            int toSend = (int) Math.ceil(this.storedPower.getValue());
                            if(this.storedPower.getValue() > 5.0){
                                toSend = 5;
                            }

                            this.storedPower.subtract(toSend - network.addPower(toSend));
                        }
                    }
                }
            }
        }
    }

    /************************************************************************************/
    /*********************** ComputerCraft **********************************************/
    /************************************************************************************/

    /************************ IPeripheral ************************/

    @Override
    //@Optional.Method(modid = "ComputerCraft") //TODO: Keep this one ?
    public abstract String getType();

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public String[] getMethodNames() {
        return new String[]{"setMode", "getMode", "getEnergyStored", "getCapacity"};
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        if(worldObj.isRemote)
            return new Object[0];

        switch(method) {
            case 0: // setOutput
                if(arguments[0] instanceof String) {
                    ForgeDirection output = ForgeDirection.valueOf((String) arguments[0]);
                    if(output != null && output != ForgeDirection.UNKNOWN){
                        if(arguments[1] instanceof String) {
                            EnumOutputMode mode = EnumOutputMode.valueOf((String) arguments[1]);
                            if(mode == null){
                                return new Object[] {"Invalid Mode"};
                            }
                            if(this.outputMode.getMode(output) == mode){
                                return new Object[]{false};
                            }
                            this.outputMode.setMode(output, mode);
                            this.markBlockForUpdate();
                            this.notifyBlocksOfNeighborChange();
                            return new Object[] {true};
                        }
                    }else{
                        return new Object[] {"Invalid Side"};
                    }
                }
            case 1: // getOutput
                if(arguments[0] instanceof String) {
                    ForgeDirection side = ForgeDirection.valueOf((String) arguments[0]);
                    if(side == null || side == ForgeDirection.UNKNOWN){
                        return new Object[]{"Invalid Side"};
                    }else{
                        return new Object[]{this.outputMode.getMode(side)};
                    }
                }
            case 2: // getEnergyStored
                return new Object[]{Math.floor(this.storedPower.getValue())};
            case 3: // getCapacity
                return new Object[]{Math.floor(this.maxStoredPower)};
        }
        return new Object[]{"Invalid Method"};
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public void attach(IComputerAccess computer) {

    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public void detach(IComputerAccess computer) {

    }

    /************************************************************************************/
    /**************************** IC2 ***************************************************/
    /************************************************************************************/

    /************************ IEnergySink ************************/

    @Override
    @Optional.Method(modid = "IC2")
    public double demandedEnergyUnits() {
        return this.maxStoredPower - this.storedPower.getValue();
    }

    @Override
    @Optional.Method(modid = "IC2")
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v) {
        double returning = 0;

        euToConvert += v;
        if(euToConvert > (Ratios.EU.conversion * 10000)) {
            returning = euToConvert - (Ratios.EU.conversion * 10000);
            euToConvert = Ratios.EU.conversion * 10000;
        }
        return returning;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public int getMaxSafeInput() {
        return 2147483647;
    }

    /************************ IEnergyAcceptor ************************/

    @Override
    @Optional.Method(modid = "IC2")
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerStorage) return false;
        if(tileEntity instanceof TileEntityPowerCable) return false;
        return this.outputMode.getMode(forgeDirection) == EnumOutputMode.INPUT;
    }

    /************************ IEnergyStorage ************************/

    @Override
    @Optional.Method(modid = "IC2")
    public int getStored() {
        return this.storedPower.getValue();
    }

    @Override
    @Optional.Method(modid = "IC2")
    public void setStored(int i) {
        this.storedPower.setValue(i);
        this.storedPower.clampMax(this.maxStoredPower);
    }

    @Override
    @Optional.Method(modid = "IC2")
    public int addEnergy(int i) {
        int ret = 0;
        this.euToConvert += i;
        if(this.euToConvert > (Ratios.EU.conversion * 10000)) {
            ret = this.euToConvert - (Ratios.EU.conversion * 10000);
            this.euToConvert = Ratios.EU.conversion * 10000;
        }
        return ret;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public int getCapacity() {
        return this.maxStoredPower;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public int getOutput() {
        return this.maxOutput;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public double getOutputEnergyUnitsPerTick() {
        return 1;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public boolean isTeleporterCompatible(ForgeDirection forgeDirection) {
        return true;
    }

    /************************ IEnergySource ************************/

    @Override
    @Optional.Method(modid = "IC2")
    public double getOfferedEnergy() {
        if(this.storedPower.getValue() > 0) {
            if(getOutput() > (int) Math.ceil(this.storedPower.getValue() / Ratios.EU.conversion)){
                return (int) Math.ceil(this.storedPower.getValue() / Ratios.EU.conversion);
            }else{
                return getOutput();
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public void drawEnergy(double v) {
        this.storedPower.subtract((int) v / Ratios.EU.conversion);
        this.storedPower.clampMin(0);
    }

    /************************ IEnergyEmitter ************************/

    @Override
    @Optional.Method(modid = "IC2")
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if(tileEntity == null) return false;
        if(tileEntity instanceof TileEntityPowerStorage) return false;
        if(tileEntity instanceof TileEntityPowerCable) return false;
        return this.outputMode.getMode(forgeDirection) == EnumOutputMode.OUTPUT;
    }

    /************************************************************************************/
    /************************ BuildCraft ***********************************************/
    /************************************************************************************/

    @Optional.Method(modid = "BuildCraft|Energy")
    public void convertBC() {
        if(!AlternativeEnergy.BCSupplied) return;

        if(AlternativeEnergy.bcComp.getPowerHandler(this) == null){
            this.getPowerProvider();
        }
        if(AlternativeEnergy.bcComp.getPowerHandler(this).getEnergyStored() <= 0) return;
        this.setPowerStored(getPowerStored() + (int) Math.floor(AlternativeEnergy.bcComp.getPowerHandler(this).useEnergy(1, AlternativeEnergy.bcComp.getPowerHandler(this).getMaxEnergyStored(), true) / Ratios.MJ.conversion));
    }

    public int getOutputSpeedMultiplier(){
        return 1;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void tryOutputtingEnergy() {
        if(!AlternativeEnergy.BCSupplied) return;

        if(this.storedPower.getValue() <= 0) return;
        boolean[] conSides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
        int connected = 0;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if(this.outputMode.getMode(dir) == EnumOutputMode.OUTPUT){
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
                if(tmpTile != null) {
                    if(tmpTile instanceof IPowerReceptor && !(tmpTile instanceof TileEntityPowerCable)) {
                        connected += 1;
                        conSides[dir.ordinal()] = true;
                    }
                }
            }
        }
        if(connected == 0) return;

        int xPower = 25 * this.getOutputSpeedMultiplier();
        if(xPower > this.storedPower.getValue()){
            xPower = this.storedPower.getValue();
        }

        int equalPower = xPower / connected;
        if(Math.floor(equalPower) < Ratios.MJ.conversion) return;

        int drainPower = 0;
        for(int i=0; i<6; i++) {
            if(!conSides[i]) {
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
        this.storedPower.setValue(this.storedPower.getValue() - (int)(drainPower / Ratios.MJ.conversion));
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler getPowerProvider() {
        if(AlternativeEnergy.bcComp.getPowerHandler(this) == null){
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
        if(this.outputMode.getMode(side) != EnumOutputMode.DISABLED && tmpTile != null && !AlternativeEnergy.isInvalidPowerTile(tmpTile))
            return getPowerProvider().getPowerReceiver();
        return null;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void doWork(PowerHandler workProvider) {

    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public World getWorld() {
        return this.worldObj;
    }
}
