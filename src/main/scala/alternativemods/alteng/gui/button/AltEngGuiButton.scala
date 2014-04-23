package alternativemods.alteng.gui.button

import net.minecraft.client.gui.GuiButton
import net.minecraft.util.ResourceLocation
import alternativemods.alteng.util.ToolTip
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngGuiButton {
  private final val texture = new ResourceLocation("alteng:textures/gui/guiBasic.png")
}

abstract class AltEngGuiButton(id: Int, x: Int, y: Int, label: String, width: Int = 200, texture: ButtonTextureSet = ButtonTextureSets.largeButton) extends GuiButton(id, x, y, width, texture.getHeight, label) {

  var tooltip: ToolTip = null

  @inline def getWidth = this.width
  @inline def getHeight = this.texture.getHeight

  def getTextColor(hover: Boolean): Int = {
    if(!this.enabled) 0xA0A0A0
    else if(hover) 0xFFFFA0
    else 0xE0E0E0
  }

  def isMouseOver(mouseX: Int, mouseY: Int): Boolean =
    (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + getHeight)

  override def drawButton(mc: Minecraft, mouseX: Int, mouseY: Int){
    if(!this.visible) return
    val fr = mc.fontRenderer
    mc.renderEngine.bindTexture(AltEngGuiButton.texture)
    GL11.glColor4f(1, 1, 1, 1)
    val xOffset = this.texture.getX
    val yOffset = this.texture.getY
    val h = this.texture.getHeight
    val w = this.texture.getWidth
    val mouseOver = this.isMouseOver(mouseX, mouseY)
    val hoverState = getHoverState(mouseOver)

    this.drawTexturedModalRect(this.xPosition, this.yPosition, xOffset, yOffset + hoverState * h, this.width / 2, h)
    this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, xOffset + w - this.width / 2, yOffset + hoverState * h, this.width / 2, h)
    this.mouseDragged(mc, mouseX, mouseY)
    this.drawCenteredString(fr, this.displayString, this.xPosition + this.width / 2, this.yPosition + (h - 8) / 2, getTextColor(mouseOver))
  }
}
