package alternativemods.alteng.gui.element

import alternativemods.alteng.powertraits.tile.BasePowerTrait
import cpw.mods.fml.relauncher.{Side, SideOnly}
import alternativemods.alteng.gui.guis.AltEngGuiContainer

/**
 * No description given
 *
 * @author jk-5
 */
class ElementPowerIndicator(final val powerHandler: BasePowerTrait, x: Int, y: Int, u: Int, v: Int, w: Int, h: Int) extends Element(x, y, u, v, w, h) {

  @SideOnly(Side.CLIENT) override def getToolTip = this.powerHandler.getToolTip

  @SideOnly(Side.CLIENT)
  override def draw(gui: AltEngGuiContainer, guiX: Int, guiY: Int, mouseX: Int, mouseY: Int){
    if(this.powerHandler == null || powerHandler.clientEnergy == 0 || powerHandler.clientMaxEnergy == 0) return
    val scale = (Math.min(powerHandler.clientEnergy, powerHandler.clientMaxEnergy) * h) / powerHandler.clientMaxEnergy
    gui.drawTexturedModalRect(guiX + this.x, guiY + this.y + this.h - scale, this.u, this.v + this.h - scale, this.w, scale)
  }
}
