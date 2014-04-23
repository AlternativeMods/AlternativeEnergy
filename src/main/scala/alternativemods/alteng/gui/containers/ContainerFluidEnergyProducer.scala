package alternativemods.alteng.gui.containers

import net.minecraft.entity.player.InventoryPlayer
import alternativemods.alteng.content.blocks.tier1.tile.TileFluidEnergyProducer

/**
 * No description given
 *
 * @author jk-5
 */
class ContainerFluidEnergyProducer(inventoryPlayer: InventoryPlayer, final val te: TileFluidEnergyProducer) extends AltEngContainer(Some(te)) {
  this.addPlayerInventory(inventoryPlayer)
}
