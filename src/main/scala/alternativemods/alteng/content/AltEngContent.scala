package alternativemods.alteng.content

import alternativemods.alteng.content.blocks.BlockConveyor
import alternativemods.alteng.content.blocks.energy.{BlockEnergyConsumer, BlockEnergyPassthrough}
import net.minecraftforge.fluids.{BlockFluidFinite, FluidRegistry, Fluid, BlockFluidBase}
import net.minecraft.block.material.Material
import alternativemods.alteng.AlternativeEnergy
import cpw.mods.fml.common.registry.GameRegistry
import alternativemods.alteng.content.items.itemblocks.ItemBlockConveyor
import alternativemods.alteng.content.items.ItemEnergyConsumer
import alternativemods.alteng.content.tileentities.{TileEntityEnergyPassthrough, TileEntityPowerConsumer, TileEntityConveyorInsertion, TileEntityConveyor}

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngContent {

  var blockConveyor: BlockConveyor = _
  var blockEnergyStorage: BlockEnergyPassthrough = _
  var blockEnergyConsumer: BlockEnergyConsumer = _
  var blockLiquidEnergy: BlockFluidBase = _

  var fluidLiquidEnergy: Fluid = _

  var itemEnergyConsumer: ItemEnergyConsumer = _

  def load(){
    blockConveyor = new BlockConveyor(Material.iron)
    GameRegistry.registerBlock(blockConveyor, classOf[ItemBlockConveyor], "conveyor")
    GameRegistry.registerTileEntity(classOf[TileEntityConveyor], "alteng.conveyor")
    GameRegistry.registerTileEntity(classOf[TileEntityConveyorInsertion], "alteng.conveyorInsertion")

    blockEnergyStorage = new BlockEnergyPassthrough(Material.iron)
    GameRegistry.registerBlock(blockEnergyStorage, "energyPassthrough")
    GameRegistry.registerTileEntity(classOf[TileEntityEnergyPassthrough], "alteng.energyPassthrough")

    blockEnergyConsumer = new BlockEnergyConsumer
    GameRegistry.registerBlock(blockEnergyConsumer, "energyConsumer")
    GameRegistry.registerTileEntity(classOf[TileEntityPowerConsumer], "alteng.powerConsumer")


    this.fluidLiquidEnergy = new Fluid("altEng.liquidEnergy")
    FluidRegistry.registerFluid(this.fluidLiquidEnergy)

    blockLiquidEnergy = new BlockFluidFinite(this.fluidLiquidEnergy, Material.water)
    GameRegistry.registerBlock(blockLiquidEnergy, "liquidEnergy")

    fluidLiquidEnergy.setBlock(blockLiquidEnergy)
    blockLiquidEnergy.setCreativeTab(AlternativeEnergy.creativeTab)
    blockLiquidEnergy.setBlockName("altEng.liquidEnergy")

    itemEnergyConsumer = new ItemEnergyConsumer()
    GameRegistry.registerItem(itemEnergyConsumer, "energyConsumer")
  }
}
