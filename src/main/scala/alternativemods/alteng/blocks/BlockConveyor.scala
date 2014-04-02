package alternativemods.alteng.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.{IBlockAccess, World}
import alternativemods.alteng.tileentities.TileEntityConveyor
import net.minecraft.util.{MathHelper, IIcon}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.ItemStack
import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.{ISidedInventory, IInventory}

/**
 * Author: Lordmau5
 * Date: 02.04.14
 * Time: 16:47
 */
class BlockConveyor(material: Material) extends Block(material) {

  setCreativeTab(CreativeTabs.tabBlock)
  setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F)

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, item: ItemStack) = {
    val dir = MathHelper.floor_double(((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3
    world.setTileEntity(x, y, z, new TileEntityConveyor(dir))
  }


  override def createTileEntity(world: World, metadata: Int) = new TileEntityConveyor()

  override def hasTileEntity(metadata: Int) = true

  val facings = Array[ForgeDirection](ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST)
  val top = Array.ofDim[IIcon](4)
  val icons = Array.ofDim[IIcon](2)
  override def registerBlockIcons(iR: IIconRegister) = {
    for(meta <- 0 until top.length) {
      top(meta) = iR.registerIcon("alternativeenergy:conveyor/top_" + facings(meta).name())
    }
    icons(0) = iR.registerIcon("alternativeenergy:conveyor/bottom")
    icons(1) = iR.registerIcon("alternativeenergy:conveyor/sides")
  }

  override def getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): IIcon = {
    val worldTile = world.getTileEntity(x, y, z)
    if(!worldTile.isInstanceOf[TileEntityConveyor])
      return icons(1)
    val tile = worldTile.asInstanceOf[TileEntityConveyor]

    if(tile == null)
      return icons(1)

    val dr = ForgeDirection.getOrientation(side)
    val adjDr = (facings(tile.facing), facings((tile.facing + 2) % 4))
    dr match {
      case ForgeDirection.UP => return top(tile.facing)
      case adjDr._1 => return top(facings(3).ordinal() - 2)
      case adjDr._2 => return top(facings(0).ordinal() - 2)
      case _ =>
    }
    if(side == ForgeDirection.DOWN.ordinal())
      return icons(0)
    else
      return icons(1)
    top(0)
  }

  override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, entity: Entity) {
    super.onEntityCollidedWithBlock(world, x, y, z, entity)

    entity match {
      case item: EntityItem =>
        item.age = 0
      case _ => return
    }

    val tile = world.getTileEntity(x, y, z)
    if(tile == null || !tile.isInstanceOf[TileEntityConveyor])
      return
    val facing = tile.asInstanceOf[TileEntityConveyor].facing
    var dr = ForgeDirection.UNKNOWN
    if(facing == 0) {
      dr = ForgeDirection.SOUTH
    }
    if(facing == 1) {
      dr = ForgeDirection.WEST
    }
    if(facing == 2) {
      dr = ForgeDirection.NORTH
    }
    if(facing == 3) {
      dr = ForgeDirection.EAST
    }
    if(dr == ForgeDirection.UNKNOWN) {
      return
    }

    var vel = (dr.offsetX * 0.09D, dr.offsetY * 0.09D, dr.offsetZ * 0.09D)

    if(dr.offsetX != 0) {
      if(entity.posZ > z + 0.6D) {
        vel = (vel._1, vel._2, -0.1D)
      } else if(entity.posZ < z + 0.4D) {
        vel = (vel._1, vel._2, 0.1D)
      }
    } else if(dr.offsetZ != 0) {
      if(entity.posX > x + 0.6D) {
        vel = (-0.1D, vel._2, vel._3)
      } else if(entity.posX < x + 0.4D) {
        vel = (0.1D, vel._2, vel._3)
      }
    }
    entity.setVelocity(vel._1, vel._2, vel._3)
  }

  override def isOpaqueCube = false
  override def renderAsNormalBlock = false
}
