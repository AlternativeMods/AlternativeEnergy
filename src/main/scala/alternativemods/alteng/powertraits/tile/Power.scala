package alternativemods.alteng.powertraits.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import cpw.mods.fml.common.{Loader, Optional}
import buildcraft.api.power.{PowerHandler, IPowerReceptor}
import net.minecraftforge.common.util.ForgeDirection
import ic2.api.energy.tile.IEnergySink
import net.minecraftforge.common.MinecraftForge
import ic2.api.energy.event.{EnergyTileUnloadEvent, EnergyTileLoadEvent}
import alternativemods.alteng.util.Side

/**
 * No description given
 *
 * @author jk-5
 */
object Power {
  final val bcid = "BuildCraft|Energy"
  final val classIPowerReceptor = "buildcraft.api.power.IPowerReceptor"
  final val classBCPowerHandler = "alternativemods.alteng.powertraits.tile.BCPowerHandler$class"

  final val cofhid = "CoFHCore"
  final val classIEnergyHandler = "cofh.api.energy.IEnergyHandler"
  final val classTEPowerHandler = "alternativemods.alteng.powertraits.tile.TEPowerHandler$class"

  final val icid = "IC2"
  final val classIEnergySink = "ic2.api.energy.tile.IEnergySink"
  final val classICPowerHandler = "alternativemods.alteng.powertraits.tile.ICPowerHandler$class"
}

import Power._

trait TilePowerHandler extends TileEntity {
  def energy: Double
  protected[this] def energy_=(en: Double)

  def maxEnergy: Double

  abstract override def readFromNBT(cmp: NBTTagCompound) {
    super.readFromNBT(cmp)
    energy = cmp.getDouble("energy")
  }

  abstract override def writeToNBT(cmp: NBTTagCompound) {
    super.writeToNBT(cmp)
    cmp.setDouble("energy", energy)
  }
}

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = classIPowerReceptor, modid = bcid)
))
trait BCPowerHandler extends TilePowerHandler with IPowerReceptor {

  val bcRatio: Double

  def minBcStored: Float = 1
  def maxBcStored: Float = 100

  /**
   * The actual powerHandler we use. Has a reference to AnyRef instead of PowerHandler so we don't crash without BC
   */
  private[this] var _powerHandler: Option[AnyRef] = None

  @Optional.Method(modid = bcid)
  def powerHandler = if(_powerHandler.isEmpty){
    val ph = new PowerHandler(this, PowerHandler.Type.STORAGE)
    ph.configure(
      minBcStored, // minEnergyReceived
      maxBcStored, // maxEnergyReceived
      minBcStored, // activationEnergy
      maxBcStored // maxStoredEnergy
    )
    ph.setPerdition(new PowerHandler.PerditionCalculator(0.01F))
    _powerHandler = Some(ph)
    ph
  }else _powerHandler.get.asInstanceOf[PowerHandler]

  abstract override def readFromNBT(cmp: NBTTagCompound){
    super.readFromNBT(cmp)
    if(Loader.isModLoaded(bcid)) powerHandler.readFromNBT(cmp)
  }

  abstract override def writeToNBT(cmp: NBTTagCompound){
    super.writeToNBT(cmp)
    if(Loader.isModLoaded(bcid)) powerHandler.writeToNBT(cmp)
  }

  @Optional.Method(modid = bcid)
  override def getPowerReceiver(side: ForgeDirection): PowerHandler#PowerReceiver = powerHandler.getPowerReceiver

  @Optional.Method(modid = bcid)
  override def doWork(workProvider: PowerHandler) = if(Side(getWorldObj).isServer){
    assert(workProvider == powerHandler)
    if(powerHandler.getEnergyStored >= minBcStored) {
      val avail = powerHandler.useEnergy(minBcStored, maxBcStored, false)
      val d1 = avail.min(((maxEnergy - energy) * bcRatio).toFloat)
      val delta = powerHandler.useEnergy(d1 min minBcStored, d1, true).toDouble / bcRatio
      energy += delta
    }
  }

  @Optional.Method(modid = bcid)
  override def getWorld = getWorldObj

  abstract override def updateEntity() {
    super.updateEntity()
    if(Side(getWorldObj).isServer && Loader.isModLoaded(bcid)) powerHandler.getPowerReceiver.update()
  }
}

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = classIEnergySink, modid = icid)
))
trait ICPowerHandler extends TilePowerHandler with IEnergySink {

  val ic2Ratio: Double

  var registered = false

  @Optional.Method(modid = icid)
  override def acceptsEnergyFrom(emitter: TileEntity, from: ForgeDirection): Boolean = true

  @Optional.Method(modid = icid)
  override def demandedEnergyUnits: Double = {
    (maxEnergy - energy) * ic2Ratio
  }

  @Optional.Method(modid = icid)
  override def injectEnergyUnits(from: ForgeDirection, amount: Double): Double = {
    val delta = (amount / ic2Ratio).min(maxEnergy - energy)
    energy += delta
    amount - delta * ic2Ratio
  }

  @Optional.Method(modid = icid)
  override def getMaxSafeInput: Int = Int.MaxValue //TODO have a reasonable amount here

  @Optional.Method(modid = icid)
  def load(): Unit = if(Side(getWorldObj).isServer){
    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this))
  }

  @Optional.Method(modid = icid)
  def unload(): Unit = if(Side(getWorldObj).isServer){
    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this))
  }

  abstract override def updateEntity(): Unit = {
    super.updateEntity()
    if(!registered){
      if(Loader.isModLoaded(icid)) load()
      registered = true
    }
  }

  abstract override def invalidate(): Unit = {
    super.invalidate()
    if(Loader.isModLoaded(icid)){
      unload()
      registered = false
    }
  }
}

trait TEPowerHandler extends TilePowerHandler

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = classIPowerReceptor, modid = bcid),
  new Optional.Interface(iface = classIEnergySink, modid = icid)
))
trait UniversalPowerTile extends TileEntity
  with BCPowerHandler
  with ICPowerHandler
  //with TETileConverter
