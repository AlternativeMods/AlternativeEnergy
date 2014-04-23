package alternativemods.alteng.fluid.tank

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fluids.{Fluid, FluidStack}

/**
 * No description given
 *
 * @author jk-5
 */
class RestrictedTank(val acceptedFluid: Fluid, _name: String, _capacity: Int, _owner: TileEntity) extends Tank(_name, _capacity, _owner) {
  override def fill(resource: FluidStack, doFill: Boolean): Int =
    if(!acceptsFluid(resource.getFluid)) 0 else super.fill(resource, doFill)

  def acceptsFluid(fluid: Fluid): Boolean = fluid == this.acceptedFluid
}
