package alternativemods.alteng.blocks

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.material.Material
import alternativemods.alteng.items.ItemBlockConveyor
import alternativemods.alteng.blocks.energy.BlockLiquidEnergy

object AltEngBlocks {

  var blockConveyor: BlockConveyor = _
  var blockLiquidEnergy: BlockLiquidEnergy = _

  def load(){
    blockConveyor = new BlockConveyor(Material.iron)
    blockLiquidEnergy = new BlockLiquidEnergy

    GameRegistry.registerBlock(blockConveyor, classOf[ItemBlockConveyor], "conveyor")
    GameRegistry.registerBlock(blockLiquidEnergy, "liquidEnergy")
  }
}
