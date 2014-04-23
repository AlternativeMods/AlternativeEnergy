package alternativemods.alteng.content.tileentities

import net.minecraft.tileentity.TileEntity
import cpw.mods.fml.common.Loader
import net.minecraftforge.common.util.ForgeDirection
import cpw.mods.fml.common.Optional.Method
import buildcraft.api.power.PowerHandler
import alternativemods.alteng.powertraits.tile.{Power, UniversalPowerTile}

class TileEntityEnergyPassthrough extends TileEntity with UniversalPowerTile {

  override def validate(): Unit = {
    if(getWorldObj != null && registered)
      updateConnections()
  }

  var energy = 0d
  val maxEnergy = 1000d
  val bcRatio = 0.2
  val ic2Ratio = 5.0

  var connections = Array.ofDim[Boolean](ForgeDirection.VALID_DIRECTIONS.length)

  def updateConnections(): Unit = {
    if(getWorldObj == null)
      return

    for(side: ForgeDirection <- ForgeDirection.VALID_DIRECTIONS) {
      if(worldObj.isAirBlock(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)) {
        connections(side.ordinal()) = false
      }
      else {
        val tile = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)
        if(tile == null) {
          connections(side.ordinal()) = false
        }
        else {
          connections(side.ordinal()) = true
          //TODO: How to check for a proper Tile without making MC crash?
        }
      }
    }
    if(Loader.isModLoaded(Power.icid) && registered)
      reload()
  }

  def validConnections(): Int = {
    var count: Int = 0

    for(isTrue: Boolean <- connections) {
      if(isTrue) {
        count += 1
      }
    }

    count
  }

  // Industrial Craft
  @Method(modid = Power.icid)
  override def acceptsEnergyFrom(emitter: TileEntity, from: ForgeDirection): Boolean = {
    if(validConnections() < 2)
      return false

    true
  }

  @Method(modid = Power.icid)
  override def demandedEnergyUnits: Double = {
    if(validConnections() < 2)
      return 0

    //TODO: Buffer or something?
    super.demandedEnergyUnits
  }

  @Method(modid = Power.icid)
  override def getOfferedEnergy: Double = {
    if(validConnections() < 2)
      return 0

    //TODO: Buffer or something?
    super.getOfferedEnergy
  }

  @Method(modid = Power.icid)
  override def drawEnergy(amount: Double): Unit = {
    if(validConnections() < 2)
      return

    //TODO: Buffer or something?
    super.drawEnergy(amount)
  }

  @Method(modid = Power.icid)
  override def injectEnergyUnits(from: ForgeDirection, amount: Double): Double = {
    if(validConnections() < 2)
      return 0

    //TODO: Buffer or something?
    super.injectEnergyUnits(from, amount)
  }
  //--------------------------------------------------------------------------------------------------------------------
  @Method(modid = Power.bcid)
  override def doWork(workProvider: PowerHandler): Unit = {
    if(validConnections() < 2)
      return

    //TODO: Buffer or something?
    super.doWork(workProvider)
  }

  @Method(modid = Power.bcid)
  override def getPowerReceiver(side: ForgeDirection): PowerHandler#PowerReceiver = {
    if(validConnections() < 2)
      return null

    powerHandler.getPowerReceiver
  }
}
