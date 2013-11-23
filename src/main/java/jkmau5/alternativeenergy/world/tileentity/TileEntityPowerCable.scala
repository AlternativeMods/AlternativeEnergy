package jkmau5.alternativeenergy.world.tileentity

import jkmau5.alternativeenergy.power.traits.TBuildCraftPowerTransport
import jkmau5.alternativeenergy.util.{BufferedTileEntity, CableConnectionMatrix}

/**
 * No description given
 *
 * @author jk-5
 */
class TileEntityPowerCable extends AltEngTileEntity with TBuildCraftPowerTransport {

  private val connectionMatrix = new CableConnectionMatrix
  private val adjacentTiles = BufferedTileEntity.makeBuffer(this.worldObj, xCoord, yCoord, zCoord, loadUnloaded = false)

  def getWorld = this.worldObj

  //Mixin Methods
  override def updateEntity(){}
}
