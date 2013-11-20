package jkmau5.alternativeenergy.power.traits

import buildcraft.api.power.{PowerHandler, IPowerReceptor}
import net.minecraftforge.common.ForgeDirection
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerCable
import cpw.mods.fml.common.Optional

/**
 * No description given
 *
 * @author jk-5
 */
trait TBuildCraftPowerTransport extends TileEntityPowerCable with IPowerReceptor {

  private var bcPowerHandler: PowerHandler = _

  abstract override def updateEntity(){

  }

  @Optional.Method(modid = "BuildCraft|Energy")
  def getPowerReceiver(side: ForgeDirection): PowerHandler#PowerReceiver = {

  }

  @Optional.Method(modid = "BuildCraft|Energy")
  def doWork(workProvider: PowerHandler){}
}
