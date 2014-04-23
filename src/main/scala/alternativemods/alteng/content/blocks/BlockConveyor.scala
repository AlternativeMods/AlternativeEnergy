package alternativemods.alteng.content.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.{IBlockAccess, World}
import alternativemods.alteng.content.tileentities.{TileEntityConveyorInsertion, TileEntityConveyor}
import net.minecraft.util.{MathHelper, IIcon}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.entity.item.EntityItem
import java.util
import alternativemods.alteng.AlternativeEnergy

class BlockConveyor(material: Material) extends Block(material) {

  setCreativeTab(AlternativeEnergy.creativeTab)

  override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
    val meta = world.getBlockMetadata(x, y, z)
    meta match {
      case 0 => setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F) // Conveyor
      case 1 => setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F) // Conveyor Insertion Block

      case _ => setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
    }
  }

  override def damageDropped(metadata: Int) = metadata

  override def getSubBlocks(item: Item, creativeTab: CreativeTabs, list: util.List[_]) = {
    for(i <- 0 until 2) {
      list.asInstanceOf[util.List[ItemStack]].add(new ItemStack(this, 1, i))
    }
  }

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, item: ItemStack) = {
    item.getItemDamage match {
      case 0 => { // Conveyor
        val dir = MathHelper.floor_double(((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3
        world.setTileEntity(x, y, z, new TileEntityConveyor(dir))
      }
      case 1 => { // Conveyor Insertion
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
      case _ =>
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

  override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, wEntity: Entity) {
    super.onEntityCollidedWithBlock(world, x, y, z, wEntity)

    wEntity match {
      case item: EntityItem =>
        item.age = 0
      case _ => return
    }
    val entity = wEntity.asInstanceOf[EntityItem]

    val tile = world.getTileEntity(x, y, z)
    if(tile == null) return
    tile match {
      case conveyor: TileEntityConveyor =>
        val facing = conveyor.facing

        var dr = ForgeDirection.UNKNOWN
        facing match {
          case 0 => dr = ForgeDirection.SOUTH
          case 1 => dr = ForgeDirection.EAST
          case 2 => dr = ForgeDirection.NORTH
          case 3 => dr = ForgeDirection.EAST
          case _ =>
        }

        if (dr == ForgeDirection.UNKNOWN) return

        var vel = (dr.offsetX * 0.09D, dr.offsetY * 0.09D, dr.offsetZ * 0.09D)

        if (dr.offsetX != 0) {
          if (entity.posZ > z + 0.6D) {
            vel = (vel._1, vel._2, -0.1D)
          } else if (entity.posZ < z + 0.4D) {
            vel = (vel._1, vel._2, 0.1D)
          }
        } else if (dr.offsetZ != 0) {
          if (entity.posX > x + 0.6D) {
            vel = (-0.1D, vel._2, vel._3)
          } else if (entity.posX < x + 0.4D) {
            vel = (0.1D, vel._2, vel._3)
          }
        }
        entity.setVelocity(vel._1, vel._2, vel._3)
      case insertion: TileEntityConveyorInsertion => {
        val facing = insertion.facing
        val dr = ForgeDirection.getOrientation(facing)
        if(insertion.canInsertAnywhere(entity.getEntityItem, dr.ordinal()))
          entity.setDead()
      }
      case _ =>
    }
  }

  override def isOpaqueCube = false
  override def renderAsNormalBlock = false
}
