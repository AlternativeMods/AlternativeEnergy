package alternativemods.alteng.content.blocks.energy

import net.minecraft.block.material.Material
import net.minecraft.block.Block
import alternativemods.alteng.AlternativeEnergy
import net.minecraft.world.{World, IBlockAccess}
import net.minecraft.item.Item
import net.minecraft.util.ChatComponentText
import alternativemods.alteng.content.tileentities._
import net.minecraft.entity.player.EntityPlayer
import alternativemods.alteng.util.Side

class BlockEnergyPassthrough(material: Material) extends Block(material) {
  setBlockName("energyPassthrough")
  setCreativeTab(AlternativeEnergy.creativeTab)

  //TODO: Move this into the actual Storage Block :P
  /*object StorageType extends Enumeration {
    type StorageType = Value
    val Small, Medium, High = Value
  }

  override def damageDropped(metadata: Int) = metadata

  override def getSubBlocks(item: Item, creativeTab: CreativeTabs, list: util.List[_]) = {
    for(i <- 0 until StorageType.values.size) {
      list.asInstanceOf[util.List[ItemStack]].add(new ItemStack(this, 1, i))
    }
  }*/

  override def createTileEntity(world: World, metadata: Int) = {
    new TileEntityEnergyPassthrough
  }

  override def hasTileEntity(metadata: Int) = true

  override def onNeighborChange(world: IBlockAccess, x: Int, y: Int, z: Int, tileX: Int, tileY: Int, tileZ: Int): Unit = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityEnergyPassthrough]
    if(tile == null)
      return

    tile.updateConnections()
  }

  override def onBlockActivated(world: World, p_149727_2_ : Int, p_149727_3_ : Int, p_149727_4_ : Int, p_149727_5_ : EntityPlayer, p_149727_6_ : Int, p_149727_7_ : Float, p_149727_8_ : Float, p_149727_9_ : Float): Boolean = {
    if(Side(world).isClient)
      return false

    val mainTile = world.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_)
    if(mainTile == null || !mainTile.isInstanceOf[TileEntityEnergyPassthrough])
      return false

    val tile = mainTile.asInstanceOf[TileEntityEnergyPassthrough]
    p_149727_5_.addChatComponentMessage(new ChatComponentText("Valid? - " + tile.validConnections() + " | " + tile.energy))
    true
  }
}
