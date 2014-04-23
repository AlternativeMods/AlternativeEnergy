package alternativemods.alteng.content.blocks

import net.minecraft.block.Block
import net.minecraft.world.{IBlockAccess, World}
import alternativemods.alteng.content.tileentities.NeighborAwareTile

/**
 * No description given
 *
 * @author jk-5
 */
trait NeighborAwareBlock extends Block {
  abstract override def onBlockAdded(world: World, x: Int, y: Int, z: Int){
    super.onBlockAdded(world, x, y, z)
    world.getTileEntity(x, y, z) match {
      case t: NeighborAwareTile => t.onBlockAdded()
      case _ =>
    }
  }

  override def onNeighborChange(world: IBlockAccess, x: Int, y: Int, z: Int, tileX: Int, tileY: Int, tileZ: Int){
    super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ)
    world.getTileEntity(x, y, z) match {
      case t: NeighborAwareTile => t.refreshNeighborCache()
      case _ =>
    }
  }
}
