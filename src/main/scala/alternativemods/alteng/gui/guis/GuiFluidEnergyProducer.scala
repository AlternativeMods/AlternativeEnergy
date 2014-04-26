package alternativemods.alteng.gui.guis

import alternativemods.alteng.gui.containers.ContainerFluidEnergyProducer
import net.minecraft.entity.player.InventoryPlayer
import alternativemods.alteng.content.blocks.tier1.tile.TileFluidEnergyProducer
import net.minecraft.util.ResourceLocation

/**
 * No description given
 *
 * @author jk-5
 */
object GuiFluidEnergyProducer {
  val background = new ResourceLocation("alteng", "textures/gui/guiFluidEnergyProducer.png")
}

class GuiFluidEnergyProducer(inventoryPlayer: InventoryPlayer, te: TileFluidEnergyProducer) extends AltEngGuiContainer(new ContainerFluidEnergyProducer(inventoryPlayer, te), GuiFluidEnergyProducer.background)
