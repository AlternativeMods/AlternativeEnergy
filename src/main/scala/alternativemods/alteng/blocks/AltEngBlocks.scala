package alternativemods.alteng.blocks

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.material.Material
import alternativemods.alteng.items.ItemBlockConveyor

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngBlocks {

  var blockConveyor: BlockConveyor = _
  def load(){
    blockConveyor = new BlockConveyor(Material.iron)

    GameRegistry.registerBlock(blockConveyor, classOf[ItemBlockConveyor], "conveyor")
  }
}
