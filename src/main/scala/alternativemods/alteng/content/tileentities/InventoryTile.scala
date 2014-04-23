package alternativemods.alteng.content.tileentities

import net.minecraft.inventory.IInventory
import net.minecraft.tileentity.TileEntity
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import alternativemods.alteng.util.InventoryObject

/**
 * No description given
 *
 * @author jk-5
 */
trait InventoryTile extends TileEntity with IInventory {
  val inventory: InventoryObject

  override def getSizeInventory = inventory.getSizeInventory
  override def getStackInSlot(var1: Int) = inventory.getStackInSlot(var1)
  override def decrStackSize(var1: Int, var2: Int) = inventory.decrStackSize(var1, var2)
  override def getStackInSlotOnClosing(var1: Int) = inventory.getStackInSlotOnClosing(var1)
  override def setInventorySlotContents(var1: Int, var2: ItemStack) = inventory.setInventorySlotContents(var1, var2)
  override def getInventoryName = inventory.getInventoryName
  override def hasCustomInventoryName = inventory.hasCustomInventoryName
  override def getInventoryStackLimit = inventory.getInventoryStackLimit
  override def markDirty() = inventory.markDirty()
  override def isUseableByPlayer(var1: EntityPlayer) = inventory.isUseableByPlayer(var1)
  override def openInventory() = inventory.openInventory()
  override def closeInventory() = inventory.closeInventory()
  override def isItemValidForSlot(var1: Int, var2: ItemStack) = inventory.isItemValidForSlot(var1, var2)

  abstract override def writeToNBT(nbt: NBTTagCompound){
    this.inventory.writeToNBT(nbt)
    super.writeToNBT(nbt)
  }

  abstract override def readFromNBT(nbt: NBTTagCompound){
    this.inventory.readFromNBT(nbt)
    super.readFromNBT(nbt)
  }
}
