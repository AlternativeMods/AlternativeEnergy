package alternativemods.alteng.gui

import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer

/**
 * No description given
 *
 * @author jk-5
 */
trait GuiTile extends TileEntity {
  def serverGui(id: Int, player: EntityPlayer): AnyRef
  def clientGui(id: Int, player: EntityPlayer): AnyRef
}
