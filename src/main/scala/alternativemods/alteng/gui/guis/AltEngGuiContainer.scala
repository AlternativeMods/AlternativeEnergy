package alternativemods.alteng.gui.guis

import net.minecraft.client.gui.inventory.GuiContainer
import cpw.mods.fml.relauncher.{Side, SideOnly}
import alternativemods.alteng.gui.containers.AltEngContainer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import net.minecraft.client.renderer.RenderHelper
import alternativemods.alteng.util.ToolTip
import scala.collection.JavaConversions._
import net.minecraft.client.gui.{GuiButton, GuiScreen}
import net.minecraft.client.renderer.Tessellator.{instance => tes}
import alternativemods.alteng.gui.button.AltEngGuiButton
import alternativemods.alteng.gui.containers.slot.AltEngSlot
import net.minecraft.inventory.Slot
import java.util

/**
 * No description given
 *
 * @author jk-5
 */
@SideOnly(Side.CLIENT)
class AltEngGuiContainer(final val container: AltEngContainer, final val background: ResourceLocation) extends GuiContainer(container) {

  override def drawScreen(mouseX: Int, mouseY: Int, delta: Float){
    super.drawScreen(mouseX, mouseY, delta)
    import GL11._

    val left = this.guiLeft
    val top = this.guiTop

    glDisable(GL_LIGHTING)
    glDisable(GL_DEPTH_TEST)
    glPushMatrix()
    glTranslatef(left, top, 0)
    glColor4f(1, 1, 1, 1)
    RenderHelper.disableStandardItemLighting()

    val inventoryPlayer = mc.thePlayer.inventory
    if(inventoryPlayer.getItemStack == null){
      val mX = mouseX - left
      val mY = mouseY - top
      this.container.elements.filter(!_.hidden).foreach(e => {
        val tooltip = e.getToolTip
        if(tooltip != null){
          val hover = e.isMouseOver(mX, mY)
          tooltip.onTick(hover)
          if(hover && tooltip.display()){
            tooltip.refresh()
            this.drawToolTip(tooltip, mouseX, mouseY)
          }
        }
      })
      this.buttonList.asInstanceOf[util.List[GuiButton]].filter(b => b.visible && b.isInstanceOf[AltEngGuiButton]).map(_.asInstanceOf[AltEngGuiButton]).foreach(b => {
        val tooltip = b.tooltip
        if(tooltip != null){
          val hover = b.isMouseOver(mouseX, mouseY)
          tooltip.onTick(hover)
          if(hover && tooltip.display()){
            tooltip.refresh()
            this.drawToolTip(tooltip, mouseX, mouseY)
          }
        }
      })
      this.inventorySlots.inventorySlots.asInstanceOf[util.List[Slot]].filter(_.isInstanceOf[AltEngSlot]).map(_.asInstanceOf[AltEngSlot]).foreach(s => {
        val tooltip = s.tooltip
        if(tooltip != null){
          val hover = this.isMouseOverSlot(s, mouseX, mouseY)
          tooltip.onTick(hover)
          if(hover && tooltip.display()){
            tooltip.refresh()
            this.drawToolTip(tooltip, mouseX, mouseY)
          }
        }
      })
    }

    glPopMatrix()
    glEnable(GL_LIGHTING)
    glEnable(GL_DEPTH_TEST)
  }

  override def drawGuiContainerBackgroundLayer(delta: Float, mouseX: Int, mouseY: Int){
    import GL11._

    glColor4f(1, 1, 1, 1)
    mc.renderEngine.bindTexture(this.background)

    val x = (this.width - this.xSize) / 2
    val y = (this.height - this.ySize) / 2

    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize)

    val mX = mouseX - this.guiLeft
    val mY = mouseY - this.guiTop

    this.container.elements.filter(!_.hidden).foreach(_.draw(this, x, y, mX, mY))
  }

  override def mouseClicked(mouseX: Int, mouseY: Int, button: Int){
    val mX = mouseX - this.guiLeft
    val mY = mouseY - this.guiTop

    this.container.elements.filter(e => !e.hidden && e.isMouseOver(mX, mY)).foreach(e => if(e.mouseClicked(mX, mY, button)) return)
    super.mouseClicked(mouseX, mouseY, button)
  }

  override def mouseClickMove(x: Int, y: Int, button: Int, time: Long){
    val slot = this.getSlotAtPosition(x, y).getOrElse(null)
    if(button == 1 && slot.isInstanceOf[AltEngSlot] && slot.asInstanceOf[AltEngSlot].isFakeSlot) return
    super.mouseClickMove(x, y, button, time)
  }

  private def getSlotAtPosition(x: Int, y: Int): Option[Slot] = {
    this.inventorySlots.inventorySlots.asInstanceOf[util.List[Slot]].find(s => this.isMouseOverSlot(s, x, y))
  }

  private def isMouseOverSlot(slot: Slot, x: Int, y: Int): Boolean = {
    val left = this.guiLeft
    val top = this.guiTop
    val mx = x - left
    val my = y - top
    mx >= slot.xDisplayPosition - 1 && mx < slot.xDisplayPosition + 16 + 1 && my >= slot.yDisplayPosition - 1 && my < slot.yDisplayPosition + 16 + 1
  }

  private def drawToolTip(toolTip: ToolTip, mouseX: Int, mouseY: Int){
    if(toolTip.size() > 0){
      val left = this.guiLeft
      val top = this.guiTop
      var length = 0
      toolTip.foreach(line => {
        val y = this.fontRendererObj.getStringWidth(line.text)
        if(y > length) length = y
      })
      val x = mouseX - left + 12
      var y = mouseY - top - 12

      var var14 = 8
      if(toolTip.size() > 1){
        var14 += 2 + (toolTip.size() - 1) * 10
      }
      this.zLevel = 300.0F
      GuiScreen.itemRender.zLevel = 300.0F
      val var15 = -267386864
      this.drawGradientRect(x - 3, y - 4, x + length + 3, y - 3, var15, var15)
      this.drawGradientRect(x - 3, y + var14 + 3, x + length + 3, y + var14 + 4, var15, var15)
      this.drawGradientRect(x - 3, y - 3, x + length + 3, y + var14 + 3, var15, var15)
      this.drawGradientRect(x - 4, y - 3, x - 3, y + var14 + 3, var15, var15)
      this.drawGradientRect(x + length + 3, y - 3, x + length + 4, y + var14 + 3, var15, var15)
      val var16 = 1347420415
      val var17 = (var16 & 0xFEFEFE) >> 1 | var16 & 0xFF000000
      this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + var14 + 3 - 1, var16, var17)
      this.drawGradientRect(x + length + 2, y - 3 + 1, x + length + 3, y + var14 + 3 - 1, var16, var17)
      this.drawGradientRect(x - 3, y - 3, x + length + 3, y - 3 + 1, var16, var16)
      this.drawGradientRect(x - 3, y + var14 + 2, x + length + 3, y + var14 + 3, var17, var17)

      toolTip.foreach(tip => {
        this.fontRendererObj.drawStringWithShadow(tip.color.toString + tip.text, x, y, -1)
        y += 10 + tip.spacing
      })
      this.zLevel = 0.0F
      GuiScreen.itemRender.zLevel = 0.0F
    }
  }

  def drawTexture(x: Int, y: Int, w: Int, h: Int, uMin: Float, vMin: Float, uMax: Float, vMax: Float){
    tes.startDrawingQuads()
    tes.addVertexWithUV(x, y + h, this.zLevel, uMin, vMax)
    tes.addVertexWithUV(x + w, y + h, this.zLevel, uMax, vMax)
    tes.addVertexWithUV(x + w, y, this.zLevel, uMax, vMin)
    tes.addVertexWithUV(x, y, this.zLevel, uMin, vMin)
    tes.draw()
  }
}
