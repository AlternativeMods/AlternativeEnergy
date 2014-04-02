package alternativemods.alteng.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.{IBlockAccess, World}
import alternativemods.alteng.tileentities.TileEntityConveyor
import net.minecraft.util.IIcon
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.creativetab.CreativeTabs

/**
 * Author: Lordmau5
 * Date: 02.04.14
 * Time: 16:47
 */
class BlockConveyor(material: Material) extends Block(material) {

  setCreativeTab(CreativeTabs.tabBlock)
  setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F)
  override def createTileEntity(world: World, metadata: Int) = new TileEntityConveyor()

  override def hasTileEntity(metadata: Int) = true

  val top = Array.ofDim[IIcon](4)
  val icons = Array.ofDim[IIcon](2)
  override def registerBlockIcons(iR : IIconRegister) = {
    for(meta <- 0 until top.length) {
      top(meta) = iR.registerIcon("alternativeenergy:conveyor/top_" + ForgeDirection.getOrientation(meta + 2).name())
    }
    icons(0) = iR.registerIcon("alternativeenergy:conveyor/bottom")
    icons(1) = iR.registerIcon("alternativeenergy:conveyor/sides")
  }

  override def getIcon(world : IBlockAccess, x : Int, y : Int, z : Int, side : Int): IIcon = {
    val worldTile = world.getTileEntity(x, y, z)
    if(!worldTile.isInstanceOf[TileEntityConveyor])
      return icons(1)
    val tile = worldTile.asInstanceOf[TileEntityConveyor]

    if(side == ForgeDirection.UP.ordinal()) {
      if(top(tile.facing) != null)
        return top(tile.facing)
    }
    else
      if(side == ForgeDirection.DOWN.ordinal())
        return icons(0)
      else
        return icons(1)
    top(0)
  }
}
