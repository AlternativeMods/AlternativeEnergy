package alternativemods.alteng.content.tileentities

import net.minecraft.tileentity.TileEntity

/**
 * No description given
 *
 * @author jk-5
 */
trait TimeTrackingTile extends TileEntity {
  var ellapsed: Long = 0

  abstract override def updateEntity(){
    super.updateEntity()
    ellapsed += 1
  }
}
