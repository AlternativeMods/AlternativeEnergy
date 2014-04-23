package alternativemods.alteng.blocks.energy

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.World
import alternativemods.alteng.tileentities.TileEntityPowerConsumer

/**
 * No description given
 *
 * @author jk-5
 */
class BlockEnergyConsumer extends Block(Material.iron) {
  override def createTileEntity(world: World, metadata: Int) = new TileEntityPowerConsumer
  override def hasTileEntity(metadata: Int) = true
}
