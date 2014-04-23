package alternativemods.alteng.blocks

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.material.Material
import alternativemods.alteng.blocks.energy.{BlockEnergyPassthrough, BlockEnergyConsumer}
import alternativemods.alteng.items.itemblocks.ItemBlockConveyor
import net.minecraftforge.fluids.{FluidRegistry, Fluid}

object AltEngBlocks {

  var blockConveyor: BlockConveyor = _
  var blockEnergyStorage: BlockEnergyPassthrough = _
  var blockEnergyConsumer: BlockEnergyConsumer = _
  var blockLiquidEnergy: BlockLiquidEnergy = _

  var fluidLiquidEnergy: Fluid = _

  def load(){
    this.fluidLiquidEnergy = new Fluid("altEng.liquidEnergy")

    FluidRegistry.registerFluid(this.fluidLiquidEnergy)

    blockConveyor = new BlockConveyor(Material.iron)
    blockEnergyStorage = new BlockEnergyPassthrough(Material.iron)
    blockEnergyConsumer = new BlockEnergyConsumer
    blockLiquidEnergy = new BlockLiquidEnergy(this.fluidLiquidEnergy)

    fluidLiquidEnergy.setBlock(blockLiquidEnergy)

    GameRegistry.registerBlock(blockConveyor, classOf[ItemBlockConveyor], "conveyor")
    GameRegistry.registerBlock(blockEnergyStorage, "energyPassthrough")
    GameRegistry.registerBlock(blockEnergyConsumer, "energyConsumer")
    GameRegistry.registerBlock(blockLiquidEnergy, "liquidEnergy")
  }
}
