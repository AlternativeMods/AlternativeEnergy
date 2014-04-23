package alternativemods.alteng.gui

import net.minecraft.tileentity.TileEntity

/**
 * No description given
 *
 * @author jk-5
 */
trait GuiTile extends TileEntity {
  def serverGui(id: Int): AnyRef
  def clientGui(id: Int): AnyRef
}
