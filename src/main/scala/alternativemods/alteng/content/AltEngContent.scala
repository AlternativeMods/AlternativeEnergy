package alternativemods.alteng.content

import alternativemods.alteng.content.blocks.BlockConveyor
import alternativemods.alteng.content.blocks.energy.{BlockEnergyConsumer, BlockEnergyPassthrough}
import net.minecraftforge.fluids._
import net.minecraft.block.material.Material
import alternativemods.alteng.AlternativeEnergy
import cpw.mods.fml.common.registry.GameRegistry
import alternativemods.alteng.content.items.itemblocks.ItemBlockConveyor
import alternativemods.alteng.content.items.{ItemFluidBucket, ItemEnergyConsumer}
import alternativemods.alteng.content.tileentities.{TileEntityEnergyPassthrough, TileEntityPowerConsumer, TileEntityConveyorInsertion, TileEntityConveyor}
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import alternativemods.alteng.content.blocks.tier1.BlockFluidEnergyProducer

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
  var blockFluidEnergyProducer: BlockFluidEnergyProducer = _

  var fluidLiquidEnergy: Fluid = _

  var bucketLiquidEnergy: ItemFluidBucket = _

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

    this.fluidLiquidEnergy = new Fluid("altEng.fluidEnergy")
    FluidRegistry.registerFluid(this.fluidLiquidEnergy)

    blockLiquidEnergy = new BlockFluidClassic(this.fluidLiquidEnergy, Material.water)
    GameRegistry.registerBlock(blockLiquidEnergy, "fluidEnergy")

    fluidLiquidEnergy.setBlock(blockLiquidEnergy)
    blockLiquidEnergy.setCreativeTab(AlternativeEnergy.creativeTab)
    blockLiquidEnergy.setBlockName("altEng.fluidEnergy")

    bucketLiquidEnergy = new ItemFluidBucket(blockLiquidEnergy)
    bucketLiquidEnergy.setUnlocalizedName("altEng.bucketLiquidEnergy").setTextureName("alteng:bucketLiquidEnergy")
    GameRegistry.registerItem(bucketLiquidEnergy, "bucketLiquidEnergy")
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidLiquidEnergy, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketLiquidEnergy), new ItemStack(Items.bucket))

    blockFluidEnergyProducer = new BlockFluidEnergyProducer
    GameRegistry.registerBlock(blockFluidEnergyProducer, "fluidEnergyProducer")

    //itemEnergyConsumer = new ItemEnergyConsumer()
    //GameRegistry.registerItem(itemEnergyConsumer, "energyConsumer")
  }
}
