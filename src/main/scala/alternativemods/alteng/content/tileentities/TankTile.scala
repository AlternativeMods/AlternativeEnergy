package alternativemods.alteng.content.tileentities

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fluids.{IFluidTank, Fluid, FluidStack, IFluidHandler}
import alternativemods.alteng.fluid.tank.Tank
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import net.minecraftforge.common.util.{Constants, ForgeDirection}
import alternativemods.alteng.gui.containers.ContainerFluidEnergyProducer
import net.minecraft.inventory.ICrafting
import net.minecraft.entity.player.EntityPlayer
import alternativemods.alteng.network.{PacketGuiIntUpdate, NetworkHandler}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.util
import scala.collection.JavaConversions._

/**
 * No description given
 *
 * @author jk-5
 */
trait TankTile extends TileEntity with IFluidHandler {
  val tanks = new util.ArrayList[Tank]()
  private val prevFluidStacks = new util.ArrayList[FluidStack]()

  def addTank(tank: Tank): Boolean = {
    val added = this.tanks.add(tank)
    val index = this.tanks.indexOf(tank)
    tank.index = index
    prevFluidStacks.add(if(tank.getFluid == null) null else tank.getFluid.copy)
    added
  }

  abstract override def readFromNBT(nbt: NBTTagCompound){
    if(nbt.hasKey("tanks")){
      val data = nbt.getCompoundTag("tanks")
      val taglist = data.getTagList("tanks", Constants.NBT.TAG_COMPOUND)
      for(i <- 0 until taglist.tagCount()){
        val tag = taglist.getCompoundTagAt(i)
        val slot = tag.getByte("tank")
        if(slot >= 0 && slot < tanks.size()){
          tanks.get(slot).readFromNBT(tag)
        }
      }
    }
    super.readFromNBT(nbt)
  }

  abstract override def writeToNBT(nbt: NBTTagCompound){
    val taglist = new NBTTagList
    for(slot <- 0 until tanks.size()){
      val tank = tanks.get(slot)
      if(tank.getFluid != null){
        val tag = new NBTTagCompound
        tag.setByte("tank", slot.toByte)
        tank.writeToNBT(tag)
        taglist.appendTag(tag)
      }
    }
    nbt.setTag("tanks", taglist)
    super.writeToNBT(nbt)
  }

  override def getTankInfo(from: ForgeDirection) = this.tanks.map(_.getInfo).toArray
  override def canDrain(from: ForgeDirection, fluid: Fluid) = true
  override def canFill(from: ForgeDirection, fluid: Fluid) = true
  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = this.drain(0, maxDrain, doDrain)
  def drain(tankIndex: Int, maxDrain: Int, doDrain: Boolean): FluidStack = {
    if(tankIndex < 0 || tankIndex >= this.tanks.length || this.tanks(tankIndex) == null) null
    else this.tanks(tankIndex).drain(maxDrain, doDrain)
  }

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    val tank = this.tanks.find(t => this.tankCanDrain(t, resource))
    if(tank.isDefined) tank.get.drain(resource.amount, doDrain) else null
  }

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = this.fill(0, resource, doFill)
  def fill(tankIndex: Int, resource: FluidStack, doFill: Boolean): Int = {
    if(tankIndex < 0 || tankIndex >= this.tanks.length || this.tanks(tankIndex) == null) 0
    else this.tanks(tankIndex).fill(resource, doFill)
  }

  private def tankCanDrain(tank: IFluidTank, fluidStack: FluidStack): Boolean = {
    if(fluidStack == null) return false
    val drained = tank.drain(1, false)
    if(drained != null && drained.amount > 0) true else false
  }

  def updateGuiTankData(container: ContainerFluidEnergyProducer, crafters: util.List[ICrafting], index: Int = 0){
    crafters.foreach(c => {
      val player = c.asInstanceOf[EntityPlayer]
      val fluidStack = this.tanks(index).getFluid
      val prev = this.prevFluidStacks.get(index)
      if(((if(fluidStack == null) 1 else 0) ^ (if(prev == null) 1 else 0)) != 0){
        val fluidId = if(fluidStack == null) -1 else fluidStack.fluidID
        val fluidAmount = if(fluidStack == null) 0 else fluidStack.amount
        c.sendProgressBarUpdate(container, index * 2, fluidId)
        NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index * 2 + 1, fluidAmount), player)
      }else if(fluidStack != null && prev != null){
        if(fluidStack.getFluid != prev.getFluid){
          c.sendProgressBarUpdate(container, index * 2, fluidStack.fluidID)
        }
        if(fluidStack.amount != prev.amount){
          NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index * 2 + 1, fluidStack.amount), player)
        }
      }
    })
    this.prevFluidStacks.set(0, if(this.tanks(index).getFluid == null) null else this.tanks(index).getFluid.copy())
  }

  def initGuiTankData(container: ContainerFluidEnergyProducer, crafter: ICrafting, index: Int = 0){
    if(index >= this.tanks.size) return
    val fluidStack = this.tanks(0).getFluid
    var fluidId = -1
    var fluidAmount = 0
    if(fluidStack != null && fluidStack.amount > 0){
      fluidId = fluidStack.getFluid.getID
      fluidAmount = fluidStack.amount
    }
    crafter.sendProgressBarUpdate(container, index * 2 + 0, fluidId)
    NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index * 2 + 1, fluidAmount), crafter.asInstanceOf[EntityPlayer])
  }

  @SideOnly(Side.CLIENT)
  def onGuiTankUpdate(id: Int, data: Int){
    val index = id / 2
    if(index >= this.tanks.size) return

    var fluidStack = this.tanks(index).getFluid
    if(fluidStack == null){
      fluidStack = new FluidStack(-1, 0)
      this.tanks(index).setFluid(fluidStack)
    }
    var fluidId = fluidStack.fluidID
    var amount = fluidStack.amount
    var newLiquid = false
    if(id % 2 == 0){
      fluidId = data
      newLiquid = true
    }else{
      amount = data
    }
    if(newLiquid){
      fluidStack = new FluidStack(fluidId, 0)
      this.tanks(index).setFluid(fluidStack)
    }
    fluidStack.amount = amount
  }
}
