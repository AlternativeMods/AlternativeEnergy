package alternativemods.alteng.content.tileentities

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

/**
 * No description given
 *
 * @author jk-5
 */
trait NeighborAwareTile extends TileEntity {

  final val neighborCache = new Array[TileEntity](6)

  def onBlockAdded() = this.refreshNeighborCache()

  def refreshNeighborCache(){
    var i = 0
    ForgeDirection.VALID_DIRECTIONS.foreach(d => {
      this.neighborCache(i) = this.getWorldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ)
      i += 1
    })
  }

  def getTile(side: ForgeDirection): TileEntity = this.neighborCache(side.ordinal())
}
