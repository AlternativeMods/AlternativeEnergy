package alternativemods.alteng.gui.containers

import net.minecraft.inventory.{ICrafting, Slot, IInventory, Container}
import net.minecraft.entity.player.{InventoryPlayer, EntityPlayer}
import scala.collection.mutable
import scala.collection.JavaConversions._
import alternativemods.alteng.gui.element.Element
import io.netty.buffer.ByteBuf
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.item.ItemStack
import alternativemods.alteng.gui.containers.slot.AltEngSlot
import alternativemods.alteng.util.InventoryUtils

/**
 * No description given
 *
 * @author jk-5
 */
abstract class AltEngContainer(private final val inventory: Option[IInventory] = None) extends Container {

  val elements = mutable.ArrayBuffer[Element]()

  final def addSlot(slot: Slot) = this.addSlotToContainer(slot)
  final def addElement(element: Element){
    element.container = this
    this.elements += element
  }

  override def addCraftingToCrafters(crafter: ICrafting){
    super.addCraftingToCrafters(crafter)
    this.elements.foreach(_.initElement(crafter))
  }

  override def detectAndSendChanges(){
    super.detectAndSendChanges()
    this.elements.foreach(e => this.crafters.map(_.asInstanceOf[ICrafting]).foreach(c => e.updateElement(c)))
  }

  def sendElementDataToClient(element: Element, crafter: ICrafting, data: ByteBuf){
    //TODO
  }

  def handleElementData(id: Int, data: ByteBuf){
    this.elements(id).handleClientPacket(data)
  }

  @SideOnly(Side.CLIENT) def updateString(id: Byte, data: String){}

  override def canInteractWith(player: EntityPlayer) = if(this.inventory.isEmpty) true else this.inventory.get.isUseableByPlayer(player)

  protected def addPlayerInventory(inventory: InventoryPlayer){
    0 to 2 foreach(y => 0 to 8 foreach(x =>
      this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18))
    ))
    0 to 8 foreach(i => this.addSlot(new Slot(inventory, i, 8 + i * 18, 142)))
  }

  override def slotClick(slotId: Int, button: Int, modifier: Int, player: EntityPlayer): ItemStack = {
    val slot = if(slotId < 0) null else this.inventorySlots.get(slotId).asInstanceOf[Slot]
    slot match {
      case s: AltEngSlot => if(s.isFakeSlot) this.fakeSlotClick(s, button, modifier, player)
        else super.slotClick(slotId, button, modifier, player)
      case _ => super.slotClick(slotId, button, modifier, player)
    }
  }

  private def fakeSlotClick(slot: AltEngSlot, button: Int, modifier: Int, player: EntityPlayer): ItemStack = {
    var stack: ItemStack = null

    if(button == 2){
      if(slot.canAdjustFakeSlot) slot.putStack(null)
    }else if(button == 0 || button == 1){
      val playerInv = player.inventory
      slot.onSlotChanged()
      val stackSlot = slot.getStack
      val stackHeld = playerInv.getItemStack

      if(stackSlot != null) stack = stackSlot.copy()
      if(stackSlot == null){
        if(stackHeld != null && slot.isItemValid(stackHeld)){
          this.fillFakeSlot(slot, stackHeld, button, modifier)
        }
      }else if(stackHeld == null){
        adjustFakeSlot(slot, button, modifier)
        slot.onPickupFromSlot(player, playerInv.getItemStack)
      }else if(slot.isItemValid(stackHeld)){
        if(InventoryUtils.areItemsEqual(stackSlot, stackHeld)) {
          this.adjustFakeSlot(slot, button, modifier)
        }else{
          this.fillFakeSlot(slot, stackHeld, button, modifier)
        }
      }
    }
    stack
  }

  protected def adjustFakeSlot(slot: AltEngSlot, button: Int, modifier: Int){
    if(!slot.canAdjustFakeSlot) return
    val stackSlot = slot.getStack
    var stackSize = 0
    if(modifier == 1){
      stackSize = if(button == 0) (stackSlot.stackSize + 1) / 2 else stackSlot.stackSize * 2
    }else{
      stackSize = if(button == 0) stackSlot.stackSize - 1 else stackSlot.stackSize + 1
    }
    stackSize = Math.min(stackSize, slot.getSlotStackLimit)
    stackSlot.stackSize = stackSize
    if(stackSlot.stackSize <= 0){
      slot.putStack(null)
    }
  }

  protected def fillFakeSlot(slot: AltEngSlot, held: ItemStack, button: Int, modifier: Int){
    if(!slot.canAdjustFakeSlot) return
    var stackSize = if(button == 0) held.stackSize else 1
    stackSize = Math.min(stackSize, slot.getSlotStackLimit)
    val phantomStack = held.copy()
    phantomStack.stackSize = stackSize
    slot.putStack(phantomStack)
  }

  def shiftItemStack(stackToShift: ItemStack, start: Int, end: Int): Boolean = {
    var changed = false
    if(stackToShift.isStackable){
      var slotIndex = start
      while(stackToShift.stackSize > 0 && slotIndex < end){
        val slot = this.inventorySlots.get(slotIndex).asInstanceOf[Slot]
        val stackInSlot = slot.getStack
        if(stackInSlot != null && InventoryUtils.areItemsEqual(stackInSlot, stackToShift)){
          val resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize
          val max = Math.min(stackToShift.getMaxStackSize, slot.getSlotStackLimit)
          if(resultingStackSize <= max){
            stackToShift.stackSize = 0
            stackInSlot.stackSize = resultingStackSize
            slot.onSlotChanged()
            changed = true
          }else if(stackInSlot.stackSize < max){
            stackToShift.stackSize -= max - stackInSlot.stackSize
            stackInSlot.stackSize = max
            slot.onSlotChanged()
            changed = true
          }
        }
        slotIndex += 1
      }
    }
    if(stackToShift.stackSize > 0) {
      var slotIndex = start
      while(stackToShift.stackSize > 0 && slotIndex < end){
        val slot = this.inventorySlots.get(slotIndex).asInstanceOf[Slot]
        var stackInSlot = slot.getStack
        if(stackInSlot == null) {
          val max = Math.min(stackToShift.getMaxStackSize, slot.getSlotStackLimit)
          stackInSlot = stackToShift.copy()
          stackInSlot.stackSize = Math.min(stackToShift.stackSize, max)
          stackToShift.stackSize -= stackInSlot.stackSize
          slot.putStack(stackInSlot)
          slot.onSlotChanged()
          changed = true
        }
        slotIndex += 1
      }
    }
    changed
  }

  private def tryShiftItem(stackToShift: ItemStack, numSlots: Int): Boolean = {
    for(machineIndex <- 0 until numSlots - 36){
      val slot = this.inventorySlots.get(machineIndex).asInstanceOf[Slot]
      if(!slot.isInstanceOf[AltEngSlot] || slot.asInstanceOf[AltEngSlot].canShiftClick){
        if(!slot.isInstanceOf[AltEngSlot] || slot.asInstanceOf[AltEngSlot].isFakeSlot){
          if(slot.isItemValid(stackToShift)){
            if(shiftItemStack(stackToShift, machineIndex, machineIndex + 1)){
              return true
            }
          }
        }
      }
    }
    false
  }

  override def transferStackInSlot(player: EntityPlayer, index: Int): ItemStack = {
    var originalStack: ItemStack = null
    val slot = this.inventorySlots.get(index).asInstanceOf[Slot]
    val numSlots = this.inventorySlots.size()
    if(slot != null && slot.getHasStack){
      val stackInSlot = slot.getStack
      originalStack = stackInSlot.copy()
      if(index < numSlots - 36 || !tryShiftItem(stackInSlot, numSlots)){
        if(index >= numSlots - 36 && index < numSlots - 9){
          if(!shiftItemStack(stackInSlot, numSlots - 9, numSlots)){
            return null
          }
        }else if(index >= numSlots - 9 && index < numSlots){
          if(!shiftItemStack(stackInSlot, numSlots - 36, numSlots - 9)){
            return null
          }
        }else if(!shiftItemStack(stackInSlot, numSlots - 36, numSlots)){
          return null
        }
      }
      slot.onSlotChange(stackInSlot, originalStack)
      if(stackInSlot.stackSize <= 0){
        slot.putStack(null)
      }else{
        slot.onSlotChanged()
      }
      if(stackInSlot.stackSize == originalStack.stackSize){
        return null
      }
      slot.onPickupFromSlot(player, stackInSlot)
    }
    originalStack
  }
}
