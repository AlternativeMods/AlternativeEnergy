package alternativemods.alteng.util

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import scala.collection.mutable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import net.minecraftforge.common.util.Constants
import alternativemods.alteng.AlternativeEnergy

/**
 * No description given
 *
 * @author jk-5
 */
class InventoryObject(size: Int, final val name: String, final val stackLimit: Int) extends IInventory {

  private final val content = new Array[ItemStack](size)
  final val listeners = mutable.ArrayBuffer[InventoryChangeListener]()

  override def getSizeInventory = this.content.length
  override def getStackInSlot(slot: Int) = this.content(slot)
  override def decrStackSize(slot: Int, count: Int): ItemStack = {
    if(content(slot) == null) return null
    if(content(slot).stackSize > count) {
      val ret = content(slot).copy()
      ret.stackSize = count
      content(slot).stackSize = content(slot).stackSize - count
      return ret
    }
    val ret = content(slot).copy()
    content(slot) = null
    ret
  }

  override def getStackInSlotOnClosing(slot: Int): ItemStack = {
    if(content(slot) == null) return null
    val ret = content(slot).copy()
    content(slot) = null
    ret
  }

  override def setInventorySlotContents(slot: Int, stack: ItemStack) = content(slot) = stack
  override def getInventoryName = name
  override def hasCustomInventoryName = true //TODO ?
  override def getInventoryStackLimit = this.stackLimit
  override def markDirty() = listeners.foreach(_.onInventoryChanged(this))
  override def isUseableByPlayer(player: EntityPlayer) = true
  override def closeInventory(){}
  override def openInventory(){}
  override def isItemValidForSlot(slot: Int, stack: ItemStack) = true

  def readFromNBT(nbt: NBTTagCompound, prefix: String = ""){
    val taglist = nbt.getTagList(prefix + "items", Constants.NBT.TAG_COMPOUND)
    for(i <- 0 until taglist.tagCount()){
      val tag = taglist.getCompoundTagAt(i)
      val index = tag.getInteger("index")
      if(index < this.content.length){
        this.content(index) = ItemStack.loadItemStackFromNBT(tag)
      }else{
        AlternativeEnergy.logger.error(s"InventoryObject: java.lang.ArrayIndexOutOfBoundsException: $index of ${this.content.length}")
      }
    }
  }

  def writeToNBT(nbt: NBTTagCompound, prefix: String = ""){
    val taglist = new NBTTagList
    for(i <- 0 until this.content.length){
      if(this.content(i) != null && this.content(i).stackSize > 0){
        val tag = new NBTTagCompound
        taglist.appendTag(tag)
        tag.setInteger("index", i)
        this.content(i).writeToNBT(tag)
      }
    }
    nbt.setTag(prefix + "items", taglist)
    nbt.setInteger(prefix + "itemsCount", this.content.length)
  }
}

trait InventoryChangeListener {
  def onInventoryChanged(inventory: IInventory)
}
