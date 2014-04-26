package alternativemods.alteng.content

import alternativemods.alteng.content.blocks.{BlockFluidEnergy, BlockConveyor}
import alternativemods.alteng.content.blocks.energy.{BlockEnergyConsumer, BlockEnergyPassthrough}
import net.minecraftforge.fluids._
import net.minecraft.block.material.Material
import cpw.mods.fml.common.registry.GameRegistry
import alternativemods.alteng.content.items.{ItemFluidEnergyBottle, ItemBlockMulti, ItemFluidBucket, ItemEnergyConsumer}
import alternativemods.alteng.content.tileentities.{TileEntityEnergyPassthrough, TileEntityPowerConsumer, TileEntityConveyorInsertion, TileEntityConveyor}
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import alternativemods.alteng.content.blocks.tier1.BlockTier1Machine
import alternativemods.alteng.content.blocks.tier1.tile.{TileFluidEnergyConsumer, TileFluidEnergyProducer}

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngContent {

  var blockConveyor: BlockConveyor = _
  var blockEnergyStorage: BlockEnergyPassthrough = _
  var blockEnergyConsumer: BlockEnergyConsumer = _

  var blockFluidEnergy: BlockFluidEnergy = _
  var blockTier1Machine: BlockTier1Machine = _

  var fluidLiquidEnergy: Fluid = _

  var bucketLiquidEnergy: ItemFluidBucket = _

  var itemEnergyConsumer: ItemEnergyConsumer = _

  def load(){
    blockConveyor = new BlockConveyor(Material.iron)
    GameRegistry.registerBlock(blockConveyor, classOf[ItemBlockMulti], "conveyor")
    GameRegistry.registerTileEntity(classOf[TileEntityConveyor], "alteng.conveyor")
    GameRegistry.registerTileEntity(classOf[TileEntityConveyorInsertion], "alteng.conveyorInsertion")

    blockEnergyStorage = new BlockEnergyPassthrough(Material.iron)
    GameRegistry.registerBlock(blockEnergyStorage, "energyPassthrough")
    GameRegistry.registerTileEntity(classOf[TileEntityEnergyPassthrough], "alteng.energyPassthrough")

    blockEnergyConsumer = new BlockEnergyConsumer
    GameRegistry.registerBlock(blockEnergyConsumer, "energyConsumer")
    GameRegistry.registerTileEntity(classOf[TileEntityPowerConsumer], "alteng.powerConsumer")

    fluidLiquidEnergy = new Fluid("altEng.fluidEnergy")
    FluidRegistry.registerFluid(this.fluidLiquidEnergy)

    blockFluidEnergy = new BlockFluidEnergy(this.fluidLiquidEnergy)
    GameRegistry.registerBlock(blockFluidEnergy, "fluidEnergy")

    fluidLiquidEnergy.setBlock(blockFluidEnergy)

    bucketLiquidEnergy = new ItemFluidBucket(blockFluidEnergy)
    bucketLiquidEnergy.setUnlocalizedName("altEng.bucketFluidEnergy").setTextureName("alteng:bucketFluidEnergy")
    GameRegistry.registerItem(bucketLiquidEnergy, "bucketFluidEnergy")
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidLiquidEnergy, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketLiquidEnergy), new ItemStack(Items.bucket))

    blockTier1Machine = new BlockTier1Machine
    GameRegistry.registerBlock(blockTier1Machine, classOf[ItemBlockMulti], "machineTier1")
    GameRegistry.registerTileEntity(classOf[TileFluidEnergyProducer], "alteng.fluidEnergyProducer")
    GameRegistry.registerTileEntity(classOf[TileFluidEnergyConsumer], "alteng.fluidEnergyConsumer")

    GameRegistry.registerItem(ItemFluidEnergyBottle, "fluidEnergyBottle")
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidLiquidEnergy, FluidContainerRegistry.BUCKET_VOLUME / 3), new ItemStack(ItemFluidEnergyBottle), new ItemStack(Items.glass_bottle))
  }

  def postInit(){
    // Textures
    fluidLiquidEnergy.setIcons(blockFluidEnergy.getIcon(0, 0))
  }
}
