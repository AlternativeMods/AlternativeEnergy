package alternativemods.alteng.tileentities

import net.minecraft.nbt.NBTTagCompound
import io.netty.buffer.ByteBuf
import alternativemods.alteng.network.SyncedTileEntity
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.util.ForgeDirection

/**
 * @author: Lordmau5
 * @date: 03.04.14 - 10:24
 */
class TileEntityConveyorInsertion(var facing: Int = 0) extends SyncedTileEntity with ISidedInventory {
  def this(){this(0)}

  val inventory = Array.ofDim[ItemStack](9)

  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    facing = tag.getInteger("facing")
  }

  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setInteger("facing", facing)
  }

  override def readData(buffer: ByteBuf){
    this.facing = buffer.readInt()
  }

  override def writeData(buffer: ByteBuf){
    buffer.writeInt(this.facing)
  }

  var tick = 0
  override def updateEntity() {
    if(worldObj.isRemote) return

    tick += 1
    if(tick > 5) {
      tick = 0
      if(inventory.isEmpty) return
      val dr = ForgeDirection.getOrientation(facing)
      val adjTile = worldObj.getTileEntity(xCoord + dr.offsetX, yCoord, zCoord + dr.offsetZ)
      if(adjTile == null) return
      object done extends Exception { }
      adjTile match {
        case invTile: ISidedInventory =>
          for (i <- 0 until inventory.length) {
            if (inventory(i) != null) {
              try {
                for (oI <- 0 until invTile.getSizeInventory) {
                  if(invTile.canInsertItem(oI, inventory(i), facing)) {
                    if (invTile.getStackInSlot(oI) == null) {
                      invTile.setInventorySlotContents(oI, inventory(i))
                      inventory(i) = null
                      throw done
                    }
                    if (invTile.getStackInSlot(oI).getItem == inventory(i).getItem) {
                      if (invTile.getStackInSlot(oI).stackSize + inventory(i).stackSize <= invTile.getInventoryStackLimit) {
                        invTile.getStackInSlot(oI).stackSize += inventory(i).stackSize
                        inventory(i) = null
                        throw done
                      }
                    }
                  }
                }
              }
              catch {
                case done: Throwable =>
              }
            }
          }
        case invTile: IInventory =>
          for (i <- 0 until inventory.length) {
            if (inventory(i) != null) {
              try {
                for (oI <- 0 until invTile.getSizeInventory) {
                  if (invTile.getStackInSlot(oI) == null) {
                    invTile.setInventorySlotContents(oI, inventory(i))
                    inventory(i) = null
                    throw done
                  }
                  if (invTile.getStackInSlot(oI).getItem == inventory(i).getItem) {
                    if (invTile.getStackInSlot(oI).stackSize + inventory(i).stackSize <= invTile.getInventoryStackLimit) {
                      invTile.getStackInSlot(oI).stackSize += inventory(i).stackSize
                      inventory(i) = null
                      throw done
                    }
                  }
                }
              }
              catch {
                case done: Throwable =>
              }
            }
          }
        case _ =>
      }
    }
  }

  //--- Inventory

  def getSizeInventory = inventory.length

  def closeInventory() = {}

  def openInventory() = {}

  def setInventorySlotContents(slot: Int, itemStack: ItemStack) = inventory(slot) = itemStack

  def getStackInSlot(slot: Int) = inventory(slot)

  def decrStackSize(slot: Int, size: Int) = {
    inventory(slot).stackSize -= size
    if(inventory(slot).stackSize <= 0)
      inventory(slot) = null
    inventory(slot)
  }

  def getStackInSlotOnClosing(slot: Int) = inventory(slot)

  def getInventoryName = "ConveyorInsertion"

  def hasCustomInventoryName = true

  def getInventoryStackLimit = 64

  def isUseableByPlayer(player: EntityPlayer) = false

  def isItemValidForSlot(slot: Int, itemStack: ItemStack) = {
    if(inventory(slot) == null)
      true
    if(inventory(slot).getItem== itemStack.getItem) {
      if(inventory(slot).stackSize + itemStack.stackSize <= getInventoryStackLimit)
        true
    }
    false
  }

  def getAccessibleSlotsFromSide(side: Int) = null

  def canInsertItem(slot: Int, itemStack: ItemStack, side: Int): Boolean = {
    if(side == facing) return false
    if(inventory(slot) == null) return true
    if(inventory(slot).getItem == itemStack.getItem) {
      if(inventory(slot).stackSize + itemStack.stackSize <= getInventoryStackLimit) {
        return true
      }
    }
    false
  }

  def canInsertAnywhere(itemStack: ItemStack, side: Int): Boolean = {
    if(side == facing) return false
    for(i <- 0 until inventory.length) {
      if(inventory(i) == null) {
        inventory(i) = itemStack
        return true
      }
      if(inventory(i).getItem == itemStack.getItem) {
        if(inventory(i).stackSize + itemStack.stackSize <= getInventoryStackLimit) {
          inventory(i).stackSize += itemStack.stackSize
          return true
        }
      }
    }
    false
  }

  def canExtractItem(slot: Int, itemStack: ItemStack, side: Int): Boolean = false
}
