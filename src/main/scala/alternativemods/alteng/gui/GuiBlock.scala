package alternativemods.alteng.gui

import net.minecraft.block.Block
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.network.internal.FMLNetworkHandler
import alternativemods.alteng.AlternativeEnergy

/**
 * No description given
 *
 * @author jk-5
 */
trait GuiBlock extends Block {
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if(world.isRemote){
      //Client
      true
    }else{
      //Server
      val id = this.getGuiId(world, x, y, z)
      if(id == -1){
        false
      }else{
        FMLNetworkHandler.openGui(player, AlternativeEnergy, id, world, x, y, z)
        true
      }
    }
  }

  def getGuiId(blockAccess: IBlockAccess, x: Int, y: Int, z: Int): Int = 0
}
