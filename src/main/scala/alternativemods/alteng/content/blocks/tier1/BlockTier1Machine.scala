package alternativemods.alteng.content.blocks.tier1

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.World
import net.minecraft.tileentity.TileEntity
import alternativemods.alteng.content.blocks.tier1.tile.{TileFluidEnergyConsumer, TileFluidEnergyProducer}
import alternativemods.alteng.content.blocks.{NeighborAwareBlock, BlockMulti}
import net.minecraft.item.ItemStack
import alternativemods.alteng.AlternativeEnergy
import alternativemods.alteng.gui.GuiBlock

/**
 * No description given
 *
 * @author jk-5
 */
class BlockTier1Machine extends Block(Material.iron) with BlockMulti with GuiBlock with NeighborAwareBlock {
  this.setCreativeTab(AlternativeEnergy.creativeTab)
  override def hasTileEntity(metadata: Int) = true
  override def createTileEntity(world: World, meta: Int): TileEntity = meta match {
    case 0 => new TileFluidEnergyProducer
    case 1 => new TileFluidEnergyConsumer
    case _ => null
  }
  override def getUnlocalizedName(stack: ItemStack): String = stack.getItemDamage match {
    case 0 => "fluidEnergyProducer"
    case 1 => "fluidEnergyConsumer"
    case _ => "undefined"
  }
}
