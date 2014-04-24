package alternativemods.alteng.fluid.tank

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fluids.{Fluid, FluidStack}
import alternativemods.alteng.util.ToolTipLine
import java.util.Locale

/**
 * No description given
 *
 * @author jk-5
 */
class RestrictedTank(val acceptedFluid: Fluid, _name: String, _capacity: Int, _owner: TileEntity) extends Tank(_name, _capacity, _owner) {
  override def fill(resource: FluidStack, doFill: Boolean): Int =
    if(!acceptsFluid(resource.getFluid)) 0 else super.fill(resource, doFill)

  def acceptsFluid(fluid: Fluid): Boolean = fluid == this.acceptedFluid

  override def refreshTooltip(){
    this.tooltip.clear()
    val fluidName = new ToolTipLine(acceptedFluid.getLocalizedName, acceptedFluid.getRarity.rarityColor)
    fluidName.spacing = 2
    tooltip.add(fluidName)
    tooltip.add(new ToolTipLine(String.format(Locale.ENGLISH, "%,d / %,d", getFluidAmount: Integer, this.getCapacity: Integer)))
  }
}
