package alternativemods.alteng.gui.containers

import net.minecraft.entity.player.InventoryPlayer
import alternativemods.alteng.content.blocks.tier1.tile.TileFluidEnergyProducer
import alternativemods.alteng.gui.element.{ElementPowerIndicator, ElementTank}
import net.minecraft.inventory.ICrafting
import java.util
import cpw.mods.fml.relauncher.{SideOnly, Side}
import alternativemods.alteng.gui.containers.slot.{SlotEmptyFluidContainer, SlotOutput}

/**
 * No description given
 *
 * @author jk-5
 */
class ContainerFluidEnergyProducer(inventoryPlayer: InventoryPlayer, final val te: TileFluidEnergyProducer) extends AltEngContainer(Some(te)) {
  this.addElement(new ElementTank(te.tanks.get(0), 110, 6, 176, 0, 16, 63))
  this.addElement(new ElementPowerIndicator(te, 50, 6, 192, 0, 16, 63))
  this.addSlot(new SlotEmptyFluidContainer(te, 0, 135, 11))
  this.addSlot(new SlotOutput(te, 1, 135, 49))
  this.addPlayerInventory(inventoryPlayer)

  override def detectAndSendChanges(){
    super.detectAndSendChanges()
    te.updateGuiTankData(this, this.crafters.asInstanceOf[util.List[ICrafting]])
    te.updateGuiEnergyData(this, this.crafters.asInstanceOf[util.List[ICrafting]], 10)
  }

  override def addCraftingToCrafters(crafter: ICrafting){
    super.addCraftingToCrafters(crafter)
    te.initGuiTankData(this, crafter)
    te.initGuiEnergyData(this, crafter, 10)
  }

  @SideOnly(Side.CLIENT)
  override def updateProgressBar(id: Int, data: Int){
    super.updateProgressBar(id, data)
    te.onGuiTankUpdate(id, data)
    te.onGuiEnergyUpdate(id, data, 10)
  }
}
