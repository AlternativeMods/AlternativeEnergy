package alternativemods.alteng.powertraits.tile

/**
 * No description given
 *
 * @author jk-5
 */
import Power._
import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import cpw.mods.fml.common.Optional
import ic2.api.energy.tile.IEnergySource
import buildcraft.api.power.IPowerEmitter
import net.minecraftforge.common.util.ForgeDirection
import alternativemods.alteng.util.Side
import cpw.mods.fml.common.Loader
import alternativemods.alteng.content.tileentities.NeighborAwareTile
import buildcraft.api.power.IPowerReceptor
import buildcraft.api.power.PowerHandler
import scala.collection.mutable
import net.minecraftforge.common.MinecraftForge
import ic2.api.energy.event.{EnergyTileUnloadEvent, EnergyTileLoadEvent}

trait TilePowerEjector extends TileEntity with NeighborAwareTile with BasePowerTrait {

  abstract override def readFromNBT(cmp: NBTTagCompound) {
    super.readFromNBT(cmp)
    energy = cmp.getDouble("energy")
  }

  abstract override def writeToNBT(cmp: NBTTagCompound) {
    super.writeToNBT(cmp)
    cmp.setDouble("energy", energy)
  }

  def powerEjected(power: Double){}
}

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = classIPowerEmitter, modid = bcid)
))
trait BCPowerEjector extends TilePowerEjector with IPowerEmitter {

  val bcRatio: Double
  val maxPerTick: Double

  @Optional.Method(modid = bcid)
  override def canEmitPowerFrom(p1: ForgeDirection) = true

  abstract override def updateEntity(){
    super.updateEntity()
    if(energy <= bcRatio) return
    if(getWorldObj != null && Side(getWorldObj).isServer && Loader.isModLoaded(bcid)){
      val receivers = mutable.ArrayBuffer[PowerHandler#PowerReceiver]()
      var i = 0
      this.neighborCache.foreach {
        case r: IPowerReceptor =>
          val side = ForgeDirection.getOrientation(i)
          val rec = r.getPowerReceiver(side.getOpposite)
          i += 1
          if(rec != null && rec.getEnergyStored < rec.getMaxEnergyStored){
            receivers += rec
          }
        case _ => i += 1
      }

      if(receivers.isEmpty) return
      val connected = receivers.size

      var drain = maxPerTick
      if(drain > energy){
        drain = Math.floor(energy).toInt
      }
      val eachDrain = (drain * bcRatio) / connected
      receivers.foreach(r => {
        var possible = r.getMaxEnergyStored - r.getEnergyStored
        if(possible > eachDrain){
          possible = eachDrain
        }
        if(possible > r.powerRequest){
          possible = r.powerRequest
        }
        val drain = possible / bcRatio
        energy -= drain
        r.receiveEnergy(PowerHandler.Type.MACHINE, possible, ForgeDirection.UP)
        this.powerEjected(drain)
      })
    }
  }
}

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = classIEnergySource, modid = icid)
))
trait ICPowerEjector extends TilePowerEjector with IEnergySource {

  val ic2Ratio: Double

  var registered = false

  @Optional.Method(modid = icid)
  override def emitsEnergyTo(tile: TileEntity, direction: ForgeDirection) = true

  @Optional.Method(modid = icid)
  override def getOfferedEnergy: Double = energy * ic2Ratio

  @Optional.Method(modid = icid)
  override def drawEnergy(amount: Double){
    if(amount > 0){
      val delta = (amount / ic2Ratio).max(energy)
      energy -= delta
      this.powerEjected(delta)
    }
  }

  @Optional.Method(modid = icid)
  def load(): Unit = if(Side(getWorldObj).isServer){
    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this))
  }

  @Optional.Method(modid = icid)
  def unload(): Unit = if(Side(getWorldObj).isServer){
    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this))
  }

  @Optional.Method(modid = icid)
  def reload(): Unit = if(Side(getWorldObj).isServer){
    unload()
    load()
  }

  abstract override def updateEntity(): Unit = {
    super.updateEntity()
    if(!registered){
      if(Loader.isModLoaded(icid)){
        load()
        registered = true
      }
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

trait TETileConverter extends TilePowerEjector

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = classIPowerEmitter, modid = bcid),
  new Optional.Interface(iface = classIEnergySource, modid = icid)
))
trait UniversalPowerEjector extends TileEntity
  with BCPowerEjector
  with ICPowerEjector
  //with TETileConverter
