package alternativemods.alteng.gui.containers

import net.minecraft.entity.player.InventoryPlayer
import alternativemods.alteng.content.blocks.tier1.tile.TileFluidEnergyProducer
import alternativemods.alteng.gui.element.ElementTank
import net.minecraft.inventory.{Slot, ICrafting}
import java.util
import cpw.mods.fml.relauncher.{SideOnly, Side}

/**
 * No description given
 *
 * @author jk-5
 */
class ContainerFluidEnergyProducer(inventoryPlayer: InventoryPlayer, final val te: TileFluidEnergyProducer) extends AltEngContainer(Some(te)) {
  this.addPlayerInventory(inventoryPlayer)
  this.addElement(new ElementTank(te.tanks.get(0), 110, 6, 176, 0, 16, 63))
  this.addSlot(new Slot(te, 0, 135, 11)) //TODO: jk-5: custom slot for this
  this.addSlot(new Slot(te, 1, 135, 49))

  override def detectAndSendChanges(){
    super.detectAndSendChanges()
    te.updateGuiTankData(this, this.crafters.asInstanceOf[util.List[ICrafting]])
  }

  override def addCraftingToCrafters(crafter: ICrafting){
    super.addCraftingToCrafters(crafter)
    te.initGuiTankData(this, crafter)
  }

  @SideOnly(Side.CLIENT)
  override def updateProgressBar(id: Int, data: Int){
    super.updateProgressBar(id, data)
    te.onGuiTankUpdate(id, data)
  }
}
