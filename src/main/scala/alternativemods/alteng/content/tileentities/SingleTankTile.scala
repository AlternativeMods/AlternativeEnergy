package alternativemods.alteng.content.tileentities

import net.minecraftforge.fluids.{FluidTankInfo, Fluid, FluidStack, IFluidHandler}
import net.minecraftforge.common.util.ForgeDirection
import alternativemods.alteng.fluid.tank.Tank
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

/**
 * No description given
 *
 * @author jk-5
 */
trait SingleTankTile extends TileEntity with IFluidHandler {
  val tank: Tank

  abstract override def readFromNBT(nbt: NBTTagCompound){
    if(nbt.hasKey("tanks")){
      val tankData = nbt.getCompoundTag("tanks")
      tank.readFromNBT(tankData)
    }
    super.readFromNBT(nbt)
  }

  abstract override def writeToNBT(nbt: NBTTagCompound){
    val tankData = new NBTTagCompound
    this.tank.writeToNBT(tankData)
    nbt.setTag("tanks", tankData)
    super.writeToNBT(nbt)
  }

  override def getTankInfo(from: ForgeDirection) = Array[FluidTankInfo](this.tank.getInfo)
  override def canDrain(from: ForgeDirection, fluid: Fluid) = true
  override def canFill(from: ForgeDirection, fluid: Fluid) = true
  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = tank.drain(maxDrain, doDrain)
  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = tank.drain(resource.amount, doDrain)
  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = tank.fill(resource, doFill)
}
