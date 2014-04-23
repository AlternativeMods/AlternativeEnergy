package alternativemods.alteng.content.tileentities

import net.minecraftforge.fluids.{FluidTankInfo, Fluid, FluidStack, IFluidHandler}
import net.minecraftforge.common.util.ForgeDirection

/**
 * No description given
 *
 * @author jk-5
 */
trait SingleTankTile extends IFluidHandler {
  
  
  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = ???
  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = ???
  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = ???
  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = ???
  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = ???
  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = ???
}
