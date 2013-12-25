package jkmau5.alternativeenergy.world.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import jkmau5.alternativeenergy.AltEngCompat;
import jkmau5.alternativeenergy.AltEngSupport;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.gui.element.AbstractIndicatorController;
import jkmau5.alternativeenergy.gui.element.IIndicatorController;
import jkmau5.alternativeenergy.network.synchronisation.ISynchronized;
import jkmau5.alternativeenergy.network.synchronisation.objects.SynchronizedInteger;
import jkmau5.alternativeenergy.power.EnergyNetwork;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.util.BlockOutputMode;
import jkmau5.alternativeenergy.util.EnumOutputMode;
import jkmau5.alternativeenergy.world.blocks.AltEngBlocks;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Random;

/**
 * No description given
 *
 * @author jk-5
 */
@Optional.InterfaceList( {
    @Optional.Interface(iface = "dan200.computer.api.IPeripheral", modid = "ComputerCraft"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.tile.IEnergyStorage", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2"),
    @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy"),
    @Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "ThermalExpansion")
})
public abstract class TileEntityPowerStorage extends SynchronizedTileEntity
        implements IPeripheral,
        IEnergySink, IEnergyStorage, IEnergySource,
        IPowerReceptor,
        IEnergyHandler {

    protected boolean addedToEnet = false;
    protected int euToConvert = 0;
    protected int rfToConvert = 0;

    @SideOnly(Side.CLIENT)
    public int guiPower = 0; //The amount of energy to display in the gui

    @Getter private IIndicatorController energyIndicator = new EnergyIndicatorController();

    @Setter(AccessLevel.PROTECTED) private int maxOutput = 32;
    @Getter @Setter(AccessLevel.PROTECTED) private int maxStoredPower = Config.powerBox_capacity;

    //Synchronized variables
    public BlockOutputMode outputMode;
    public SynchronizedInteger storedPower;

    private PowerHandler bcPowerHandler;

    @Override
    protected void createSynchronizedFields() {
        this.storedPower = new SynchronizedInteger();
        this.outputMode = new BlockOutputMode();

        this.addSyncedObject("storedPower", this.storedPower);
        this.addSyncedObject("outputMode", this.outputMode);
    }

    @Override
    public void onSynced(List<ISynchronized> changes) {
        for(ISynchronized sync : changes) {
            if(sync instanceof BlockOutputMode) {
                this.markBlockForUpdate();
            }
        }
    }

    public void setMode(ForgeDirection side, EnumOutputMode var) {
        if(side == ForgeDirection.UNKNOWN) {
            return;
        }
        this.outputMode.setMode(side, var);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

    public EnumOutputMode getMode(ForgeDirection side) {
        if(side == ForgeDirection.UNKNOWN) {
            return null;
        }
        return this.outputMode.getMode(side);
    }

    public int getPowerStored() {
        return this.storedPower.getValue();
    }

    public int getNeededPower() {
        return this.getMaxStoredPower() - this.storedPower.getValue();
    }

    public void setPowerStored(int storage) {
        this.storedPower.setValue(storage);
        if(storedPower.getValue() > this.maxStoredPower) {
            storedPower.setValue(this.maxStoredPower);
        }
    }

    public void constructFromItemStack(ItemStack itemStack, EntityLivingBase entity) {
        if(itemStack.getTagCompound() == null)
            return;
        readFromNBT(itemStack.getTagCompound());
    }

    public static void dropItemStackAsEntity(World world, int x, int y, int z, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        Random random = new Random();

        double f = 0.7D;
        double dx = random.nextFloat() * f + (1.0D - f) * 0.5D;
        double dy = random.nextFloat() * f + (1.0D - f) * 0.5D;
        double dz = random.nextFloat() * f + (1.0D - f) * 0.5D;

        EntityItem entityItem = new EntityItem(world, x + dx, y + dy, z + dz, itemStack.copy());
        entityItem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityItem);
    }

    public boolean removeBlockByPlayer(EntityPlayer player) {
        if(worldObj.isRemote) {
            return this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
        ItemStack itemStack = new ItemStack(AltEngBlocks.blockPowerStorage, 1, getBlockMetadata());
        AltEngSupport.initiateNBTTag(itemStack);
        writeToNBT(itemStack.getTagCompound());
        dropItemStackAsEntity(worldObj, xCoord, yCoord, zCoord, itemStack);
        return this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
    }

    public EnumOutputMode getNextMode(EnumOutputMode mode) {
        int ordinal = mode.ordinal() + 1;
        if(ordinal > EnumOutputMode.values().length - 1) {
            ordinal = 0;
        }
        return EnumOutputMode.values()[ordinal];
    }

    public boolean setNextMode(EntityPlayer player, ForgeDirection side) {
        this.setMode(side, this.getNextMode(this.getMode(side)));
        ChatMessageComponent component = ChatMessageComponent.createFromTranslationWithSubstitutions("altEng.chatmessage.storageSideModeChanged", this.getMode(side).toString().toLowerCase());
        player.sendChatToPlayer(component);
        return true;
    }

    @Override
    public boolean blockActivated(EntityPlayer player, int sideHit) {
        if(this instanceof TileEntityLinkBox)
            if(((TileEntityLinkBox)this).isLocked() && !this.isOwner(player.username)) {
                return super.blockActivated(player, sideHit);
            }
        if(player.getHeldItem() != null && AltEngCompat.isWrench(player.getHeldItem())) {
            ForgeDirection side = ForgeDirection.getOrientation(sideHit);
            if(this.worldObj.isRemote) {
                return true;
            }
            if(player.isSneaking()) {
                return false;
            } else {
                setNextMode(player, side);
                return true;
            }
        }
        return super.blockActivated(player, sideHit);
    }

    @Override
    public void invalidate() {
        if(!this.worldObj.isRemote) {
            this.onUnload();
        }
    }

    @Override
    public void onChunkUnload() {
        if(!this.worldObj.isRemote) {
            this.onUnload();
        }
    }

    private void onUnload() {
        if(AltEngCompat.hasIC2) {
            if(this.addedToEnet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                this.addedToEnet = false;
            }
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!this.worldObj.isRemote) {
            if(AltEngCompat.hasIC2) {
                if(!this.addedToEnet) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                    this.addedToEnet = true;
                }
            }

            if(this.euToConvert >= Ratios.EU.conversion) {
                while(this.euToConvert >= Ratios.EU.conversion) {
                    this.storedPower.clampMin(0);
                    this.storedPower.add(1);
                    this.euToConvert -= Ratios.EU.conversion;
                }
            }
            if(AltEngCompat.hasThermalExpansion) {
                if(this.rfToConvert >= Ratios.RF.conversion) {
                    while(this.rfToConvert >= Ratios.RF.conversion) {
                        this.storedPower.clampMin(0);
                        this.storedPower.add(1);
                        this.rfToConvert -= Ratios.RF.conversion;
                    }
                }
                outputToRF();
            }

            if(AltEngCompat.hasBC) {
                this.convertBC();
                this.tryOutputtingEnergy();
            }

            this.outputToPowerCables();

            this.storedPower.clampMin(0);
            this.storedPower.clampMax(this.maxStoredPower);
        }
    }

    private void outputToPowerCables() {
        if(this.storedPower.getValue() < 1) {
            return;
        }

        for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if(this.outputMode.getMode(side) == EnumOutputMode.OUTPUT) {
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);

                if(tmpTile != null && tmpTile instanceof TileEntityPowerCable) {
                    TileEntityPowerCable cable = (TileEntityPowerCable) tmpTile;
                    EnergyNetwork network = cable.getEnergyNetwork();

                    if(network != null) {
                        if(this.storedPower.getValue() >= 0.0) {
                            int toSend = (int) Math.ceil(this.storedPower.getValue());
                            if(this.storedPower.getValue() > 5.0) {
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
    public abstract String getType();

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public String[] getMethodNames() {
        return new String[] {"setMode", "getMode", "getEnergyStored", "getCapacity"};
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        if(worldObj.isRemote) {
            return new Object[0];
        }

        switch(method) {
            case 0: // setOutput
                if(arguments[0] instanceof String) {
                    ForgeDirection output = ForgeDirection.valueOf((String) arguments[0]);
                    if(output != null && output != ForgeDirection.UNKNOWN) {
                        if(arguments[1] instanceof String) {
                            EnumOutputMode mode = EnumOutputMode.valueOf((String) arguments[1]);
                            if(mode == null) {
                                return new Object[] {"Invalid Mode"};
                            }
                            if(this.outputMode.getMode(output) == mode) {
                                return new Object[] {false};
                            }
                            this.outputMode.setMode(output, mode);
                            this.markBlockForUpdate();
                            this.notifyBlocksOfNeighborChange();
                            return new Object[] {true};
                        }
                    } else {
                        return new Object[] {"Invalid Side"};
                    }
                }
            case 1: // getOutput
                if(arguments[0] instanceof String) {
                    ForgeDirection side = ForgeDirection.valueOf((String) arguments[0]);
                    if(side == null || side == ForgeDirection.UNKNOWN) {
                        return new Object[] {"Invalid Side"};
                    } else {
                        return new Object[] {this.outputMode.getMode(side)};
                    }
                }
            case 2: // getEnergyStored
                return new Object[] {Math.floor(this.storedPower.getValue())};
            case 3: // getCapacity
                return new Object[] {Math.floor(this.maxStoredPower)};
        }
        return new Object[] {"Invalid Method"};
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
        if(tileEntity == null) {
            return false;
        }
        if(tileEntity instanceof TileEntityPowerStorage) {
            return false;
        }
        if(tileEntity instanceof TileEntityPowerCable) {
            return false;
        }
        return this.outputMode.getMode(forgeDirection) == EnumOutputMode.INPUT;
    }

    /************************ IEnergyStorage ************************/

    /**
     * DO NOT USE THIS TO GET THE STORED POWER
     * It will crash when IC2 is not installed!
     *
     * @return The amount of stored EU
     */
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
            if(getOutput() > (int) Math.ceil(this.storedPower.getValue() / Ratios.EU.conversion)) {
                return (int) Math.ceil(this.storedPower.getValue() / Ratios.EU.conversion);
            } else {
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
        if(tileEntity == null) {
            return false;
        }
        if(tileEntity instanceof TileEntityPowerStorage) {
            return false;
        }
        if(tileEntity instanceof TileEntityPowerCable) {
            return false;
        }
        return this.outputMode.getMode(forgeDirection) == EnumOutputMode.OUTPUT;
    }

    /************************************************************************************/
    /************************ BuildCraft ***********************************************/
    /************************************************************************************/

    @Optional.Method(modid = "BuildCraft|Energy")
    public void convertBC() {
        if(!AltEngCompat.hasBC) {
            return;
        }

        if(this.bcPowerHandler == null) {
            this.getPowerHandler();
        }
        if(this.bcPowerHandler.getEnergyStored() <= 0) {
            return;
        }
        this.setPowerStored(getPowerStored() + (int) Math.floor(this.bcPowerHandler.useEnergy(1, this.bcPowerHandler.getMaxEnergyStored(), true) / Ratios.MJ.conversion));
    }

    public int getOutputSpeedMultiplier() {
        return 1;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public void tryOutputtingEnergy() {
        if(this.storedPower.getValue() <= 0) {
            return;
        }
        boolean[] conSides = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
        int connected = 0;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if(this.outputMode.getMode(dir) == EnumOutputMode.OUTPUT) {
                TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
                if(tmpTile != null) {
                    if(tmpTile instanceof IPowerReceptor && !(tmpTile instanceof TileEntityPowerCable)) {
                        connected += 1;
                        conSides[dir.ordinal()] = true;
                    }
                }
            }
        }
        if(connected == 0) {
            return;
        }

        int xPower = 25 * this.getOutputSpeedMultiplier();
        if(xPower > this.storedPower.getValue()) {
            xPower = this.storedPower.getValue();
        }

        int equalPower = xPower / connected;
        if(Math.floor(equalPower) < Ratios.MJ.conversion) {
            return;
        }

        int drainPower = 0;
        for(ForgeDirection dr : ForgeDirection.VALID_DIRECTIONS) {
            if(conSides[dr.ordinal()]) {
                TileEntity tmptile = worldObj.getBlockTileEntity(xCoord + dr.offsetX, yCoord + dr.offsetY, zCoord + dr.offsetZ);

                if(tmptile instanceof IPowerReceptor && !(tmptile instanceof TileEntityPowerCable) && !AltEngCompat.isInvalidPowerTile(tmptile)) {
                    if(((IPowerReceptor) tmptile).getPowerReceiver(dr.getOpposite()) != null) {
                        PowerHandler.PowerReceiver rec = ((IPowerReceptor)tmptile).getPowerReceiver(dr.getOpposite());
                        float neededPower = rec.powerRequest();
                        if(neededPower <= 0 || rec.getMaxEnergyStored() - rec.getEnergyStored() <= 5) {
                            continue;
                        }
                        if(neededPower > equalPower) {
                            neededPower = equalPower;
                        }

                        float restEnergy = rec.receiveEnergy(PowerHandler.Type.STORAGE, (float) Math.ceil(neededPower / Ratios.MJ.conversion), dr.getOpposite());
                        drainPower += (equalPower - restEnergy);
                    }
                }
            }
        }
        this.storedPower.setValue(this.storedPower.getValue() - drainPower / Ratios.MJ.conversion);
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler getPowerHandler() {
        if(this.bcPowerHandler == null) {
            this.bcPowerHandler = new PowerHandler(this, PowerHandler.Type.MACHINE);
            this.bcPowerHandler.configure(25, 500, 1337, 1000);
            this.bcPowerHandler.configurePowerPerdition(0, 0);
        }
        return this.bcPowerHandler;
    }

    @Override
    @Optional.Method(modid = "BuildCraft|Energy")
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        TileEntity tmpTile = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
        if(this.outputMode.getMode(side) != EnumOutputMode.DISABLED && tmpTile != null && !AltEngCompat.isInvalidPowerTile(tmpTile)) {
            return getPowerHandler().getPowerReceiver();
        }
        return null;
    }

    @Override
    @Optional.Method(modid = "BuildCraft|Energy")
    public void doWork(PowerHandler workProvider) {

    }

    @Override
    @Optional.Method(modid = "BuildCraft|Energy")
    public World getWorld() {
        return this.worldObj;
    }

    private class EnergyIndicatorController extends AbstractIndicatorController {

        @Override
        protected void refreshToolTip() {
            this.tipLine.setText(String.format("%d/%d", TileEntityPowerStorage.this.guiPower, TileEntityPowerStorage.this.getMaxStoredPower()));
        }

        @Override
        public int getScaledLevel(int scale) {
            int max = TileEntityPowerStorage.this.getMaxStoredPower();
            float f = Math.min(TileEntityPowerStorage.this.guiPower, max);
            return (int)(f * scale / max);
        }
    }

    @Optional.Method(modid = "ThermalExpansion")
    public int receiveEnergy(ForgeDirection dir, int maxReceive, boolean simulate) {
        if(outputMode.getMode(dir) != EnumOutputMode.INPUT)
            return 0;
        if(worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ) instanceof IPowerReceptor)
            return 0;
        if(maxReceive > Ratios.RF.conversion * 10)
            maxReceive = Ratios.RF.conversion * 10;
        rfToConvert += maxReceive;
        if(rfToConvert > Ratios.RF.conversion * 100) {
            maxReceive -= rfToConvert - (Ratios.RF.conversion * 100);
            rfToConvert = Ratios.RF.conversion * 100;
        }
        return maxReceive;
    }

    @Optional.Method(modid = "ThermalExpansion")
    public int extractEnergy(ForgeDirection dir, int maxExtract, boolean simulate) {
        if(outputMode.getMode(dir) != EnumOutputMode.OUTPUT)
            return 0;
        if(worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ) instanceof IPowerReceptor)
            return 0;
        int tickExtract = 500;
        if(tickExtract > (int) Math.floor(storedPower.getValue() / Ratios.RF.conversion))
            tickExtract = (int) Math.floor(storedPower.getValue() / Ratios.RF.conversion);
        if(tickExtract > maxExtract)
            tickExtract = maxExtract;

        storedPower.clampMin(0);
        storedPower.subtract(tickExtract);
        return tickExtract;
    }

    @Optional.Method(modid = "ThermalExpansion")
    public int getEnergyStored(ForgeDirection paramForgeDirection) {
        return storedPower.getValue();
    }

    @Optional.Method(modid = "ThermalExpansion")
    public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
        return maxStoredPower;
    }

    @Optional.Method(modid = "ThermalExpansion")
    public boolean canInterface(ForgeDirection dir) {
        if(outputMode.getMode(dir) == EnumOutputMode.DISABLED)
            return false;
        return true;
    }

    public void outputToRF() {
        if(storedPower.getValue() <= 0)
            return;

        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if(tile == null || !(tile instanceof IEnergyHandler))
                continue;

            IEnergyHandler handler = (IEnergyHandler) tile;
            if(!handler.canInterface(dir.getOpposite()))
                continue;

            int possible = 0;
            int stored = storedPower.getValue() * Ratios.RF.conversion;
            while(stored > 0 && stored % Ratios.RF.conversion == 0) {
                stored -= Ratios.RF.conversion;
                possible += Ratios.RF.conversion;
            }

            int received = handler.receiveEnergy(dir.getOpposite(), possible, false);
            storedPower.subtract((int) Math.ceil(received / Ratios.RF.conversion));
        }
    }
}
