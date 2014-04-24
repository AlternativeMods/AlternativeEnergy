package alternativemods.alteng.gui.element

import alternativemods.alteng.fluid.tank.Tank
import cpw.mods.fml.relauncher.{SideOnly, Side}
import alternativemods.alteng.gui.guis.AltEngGuiContainer
import alternativemods.alteng.util.RenderUtils
import RenderUtils.bind
import net.minecraft.client.Minecraft.{getMinecraft => mc}
import net.minecraft.client.renderer.texture.TextureMap

/**
 * No description given
 *
 * @author jk-5
 */
class ElementTank(final val tank: Tank, x: Int, y: Int, u: Int, v: Int, w: Int, h: Int) extends Element(x, y, u, v, w, h) {

  @SideOnly(Side.CLIENT) override def getToolTip = this.tank.getTooltip

  @SideOnly(Side.CLIENT)
  override def draw(gui: AltEngGuiContainer, guiX: Int, guiY: Int, mouseX: Int, mouseY: Int){
    if(this.tank == null) return
    val fluidStack = this.tank.getFluid
    if(fluidStack == null || fluidStack.amount <= 0 || fluidStack.getFluid == null) return
    val icon = RenderUtils.getFluidTexture(fluidStack, flowing = false)
    val scale = Math.min(fluidStack.amount, this.tank.getCapacity) / this.tank.getCapacity
    bind(TextureMap.locationBlocksTexture)
    mc.getTextureManager.bindTexture(TextureMap.locationBlocksTexture)
    for(col <- 0 until this.w / 16) for(row <- 0 until this.h / 16){
      gui.drawTexturedModelRectFromIcon(guiX + this.x + col * 16, guiY + this.y + row * 16 - 1, icon, 16, 16)
    }
    bind(gui.background)
    gui.drawTexturedModalRect(guiX + this.x, guiY + this.y - 1, this.x, this.y - 1, this.w, this.h - Math.floor(this.h * scale).toInt + 1)
    gui.drawTexturedModalRect(guiX + this.x, guiY + this.y, this.u, this.v, this.w, this.h)
  }
}
