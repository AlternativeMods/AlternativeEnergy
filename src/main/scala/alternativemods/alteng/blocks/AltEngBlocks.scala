package alternativemods.alteng.blocks

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.material.Material
import alternativemods.alteng.blocks.energy.{BlockEnergyPassthrough, BlockLiquidEnergy}
import alternativemods.alteng.items.itemblocks.{ItemBlockEnergyStorage, ItemBlockConveyor}

object AltEngBlocks {

  var blockConveyor: BlockConveyor = _
  var blockEnergyStorage: BlockEnergyPassthrough = _
  var blockLiquidEnergy: BlockLiquidEnergy = _

  def load(){
    blockConveyor = new BlockConveyor(Material.iron)
    blockEnergyStorage = new BlockEnergyPassthrough(Material.iron)
    blockLiquidEnergy = new BlockLiquidEnergy

    GameRegistry.registerBlock(blockConveyor, classOf[ItemBlockConveyor], "conveyor")
    GameRegistry.registerBlock(blockEnergyStorage, "energyPassthrough")
    GameRegistry.registerBlock(blockLiquidEnergy, "liquidEnergy")
  }
}
