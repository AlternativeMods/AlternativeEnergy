package alternativemods.alteng.gui.guis

import alternativemods.alteng.gui.containers.ContainerFluidEnergyProducer
import net.minecraft.entity.player.InventoryPlayer
import alternativemods.alteng.content.blocks.tier1.tile.TileFluidEnergyProducer
import net.minecraft.util.{StatCollector, ResourceLocation}

/**
 * No description given
 *
 * @author jk-5
 */
object GuiFluidEnergyProducer {
  val background = new ResourceLocation("alternativeenergy", "textures/gui/guiFluidEnergyProducer.png")
}

class GuiFluidEnergyProducer(inventoryPlayer: InventoryPlayer, te: TileFluidEnergyProducer) extends AltEngGuiContainer(new ContainerFluidEnergyProducer(inventoryPlayer, te), GuiFluidEnergyProducer.background) {
  override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int){
    super.drawGuiContainerForegroundLayer(mouseX, mouseY)
    fontRendererObj.drawString("Fluid Energy Producer", 6, 5, 0x000000)
    fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 4, 0x000000)
  }
}
