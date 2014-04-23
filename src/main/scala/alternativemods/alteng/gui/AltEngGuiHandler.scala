package alternativemods.alteng.gui

import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngGuiHandler extends IGuiHandler {
  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    val tile = world.getTileEntity(x, y, z)
    tile match {
      case t: GuiTile => t.clientGui(ID, player)
      case _ => null
    }
  }

  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    val tile = world.getTileEntity(x, y, z)
    tile match {
      case t: GuiTile => t.serverGui(ID, player)
      case _ => null
    }
  }
}
