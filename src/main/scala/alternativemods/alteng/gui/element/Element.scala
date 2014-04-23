package alternativemods.alteng.gui.element

import cpw.mods.fml.relauncher.{SideOnly, Side}
import alternativemods.alteng.gui.guis.AltEngGuiContainer
import alternativemods.alteng.util.ToolTip
import net.minecraft.inventory.ICrafting
import io.netty.buffer.ByteBuf
import alternativemods.alteng.gui.containers.AltEngContainer

/**
 * No description given
 *
 * @author jk-5
 */
case class Element(x: Int, y: Int, u: Int, v: Int, w: Int, h: Int) {
  var hidden = false
  var container: AltEngContainer = null

  @SideOnly(Side.CLIENT) def isMouseOver(mouseX: Int, mouseY: Int): Boolean =
    (mouseX >= this.x - 1) && (mouseX < this.x + this.w + 1) && (mouseY >= this.y - 1) && (mouseY < this.y + this.h + 1)
  @SideOnly(Side.CLIENT) def mouseClicked(mouseX: Int, mouseY: Int, button: Int) = false
  @SideOnly(Side.CLIENT) def draw(gui: AltEngGuiContainer, guiX: Int, guiY: Int, mouseX: Int, mouseY: Int){
    gui.drawTexturedModalRect(guiX + this.x, guiY + this.y, this.u, this.v, this.w, this.h)
  }
  @SideOnly(Side.CLIENT) def getToolTip: ToolTip = null
  def initElement(player: ICrafting){}
  def updateElement(player: ICrafting){}
  @SideOnly(Side.CLIENT) def handleClientPacket(buffer: ByteBuf){}
}
