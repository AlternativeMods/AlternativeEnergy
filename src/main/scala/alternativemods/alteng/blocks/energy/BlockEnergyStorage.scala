package alternativemods.alteng.blocks.energy

import net.minecraft.block.material.Material
import net.minecraft.block.Block
import alternativemods.alteng.AlternativeEnergy
import net.minecraft.world.{World, IBlockAccess}
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.creativetab.CreativeTabs
import java.util
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.{IIcon, MathHelper}
import alternativemods.alteng.tileentities.{TileEntityConveyorInsertion, TileEntityConveyor}
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.tileentity.TileEntity

class BlockEnergyStorage(material: Material) extends Block(material) {
  setCreativeTab(AlternativeEnergy.creativeTab)

  object StorageType extends Enumeration {
    type StorageType = Value
    val Small, Medium, High = Value

    val tile = Array.ofDim[TileEntity](this.values.size)
  }

  override def damageDropped(metadata: Int) = metadata

  override def getSubBlocks(item: Item, creativeTab: CreativeTabs, list: util.List[_]) = {
    for(i <- 0 until StorageType.values.size) {
      list.asInstanceOf[util.List[ItemStack]].add(new ItemStack(this, 1, i))
    }
  }

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, item: ItemStack) = {
    if(entity.rotationPitch > 62.5) { // Facing down
        var dir = ForgeDirection.DOWN
        if(entity.isSneaking)
          dir = ForgeDirection.UP
        world.setTileEntity(x, y, z, new TileEntityConveyorInsertion(dir.ordinal()))
    }
    else if(entity.rotationPitch < -62.5) { // Facing up
      var dir = ForgeDirection.UP
      if(entity.isSneaking)
        dir = ForgeDirection.DOWN
      world.setTileEntity(x, y, z, new TileEntityConveyorInsertion(dir.ordinal()))
    }
    else {
      var dir = 2 + MathHelper.floor_double(((entity.rotationYaw * 4F) / 360F) + 0.5D)
      if(entity.isSneaking)
        dir -= 2
      world.setTileEntity(x, y, z, new TileEntityConveyorInsertion(facings(dir & 3).ordinal()))
    }
  }


  override def createTileEntity(world: World, metadata: Int) = {
    metadata match {
      case 0 => new TileEntityConveyor() // Conveyor
      case 1 => new TileEntityConveyorInsertion() // Conveyor Insertion Block
    }
  }

  override def hasTileEntity(metadata: Int) = true

  val facings = Array[ForgeDirection](ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST)
  val top = Array.ofDim[IIcon](4)
  val icons = Array.ofDim[IIcon](4)
  override def registerBlockIcons(iR: IIconRegister) = {
    for(meta <- 0 until top.length) top(meta) = iR.registerIcon("alternativeenergy:conveyor/top_" + facings(meta).name())

    icons(0) = iR.registerIcon("alternativeenergy:conveyor/bottom")
    icons(1) = iR.registerIcon("alternativeenergy:conveyor/sides")

    icons(2) = iR.registerIcon("alternativeenergy:conveyor/insertionBlock")
    icons(3) = iR.registerIcon("alternativeenergy:conveyor/insertionBlockFacing")
  }

  override def getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): IIcon = {
    val worldTile = world.getTileEntity(x, y, z)
    if(worldTile == null)
      return icons(1)
    val metadata = world.getBlockMetadata(x, y, z)
    metadata match {
      case 0 => { // Conveyor
      val tile = worldTile.asInstanceOf[TileEntityConveyor]

        if(tile == null) return icons(1)

        val dr = ForgeDirection.getOrientation(side)
        val adjDr = (facings(tile.facing), facings((tile.facing + 2) % 4))
        dr match {
          case ForgeDirection.UP => return top(tile.facing)
          case adjDr._1 => return top(facings(3).ordinal() - 2)
          case adjDr._2 => return top(facings(0).ordinal() - 2)
          case _ =>
        }
        if(side == ForgeDirection.DOWN.ordinal()) return icons(0)
        else return icons(1)
      }
      case 1 => { // Conveyor Insertion Block
      val tile = worldTile.asInstanceOf[TileEntityConveyorInsertion]

        if(tile == null) return icons(2)

        val dr = ForgeDirection.getOrientation(side)
        if(dr.ordinal() == tile.facing) return icons(3)
        return icons(2)
      }
      case _ =>
    }
    top(0)
  }
}
