package alternativemods.alteng.gui.containers

import net.minecraft.entity.player.InventoryPlayer
import alternativemods.alteng.content.blocks.tier1.tile.TileFluidEnergyProducer
import alternativemods.alteng.gui.element.ElementTank
import net.minecraft.inventory.ICrafting
import java.util
import cpw.mods.fml.relauncher.{SideOnly, Side}

/**
 * No description given
 *
 * @author jk-5
 */
class ContainerFluidEnergyProducer(inventoryPlayer: InventoryPlayer, final val te: TileFluidEnergyProducer) extends AltEngContainer(Some(te)) {
  this.addPlayerInventory(inventoryPlayer)
  this.addElement(new ElementTank(te.tanks.get(0), 35, 23, 176, 0, 48, 47))

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
