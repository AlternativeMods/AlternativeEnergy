package alternativemods.alteng.fluid.tank

import net.minecraftforge.fluids.{Fluid, FluidTank}
import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import alternativemods.alteng.util.{ToolTipLine, ToolTip}
import java.util.Locale

/**
 * No description given
 *
 * @author jk-5
 */
class Tank(final val name: String, _capacity: Int, _owner: TileEntity) extends FluidTank(_capacity) {

  this.tile = _owner

  def isEmpty: Boolean = this.getFluid == null || this.getFluid.amount <= 0
  def isFull: Boolean = this.getFluid != null && this.getFluid.amount >= this.getCapacity
  def getFluidType: Fluid = if(this.getFluid != null) this.getFluid.getFluid else null

  final override def writeToNBT(nbt: NBTTagCompound): NBTTagCompound = {
    val tankData = new NBTTagCompound
    super.writeToNBT(tankData)
    this.writeTankToNBT(tankData)
    nbt.setTag(this.name, tankData)
    nbt
  }

  final override def readFromNBT(nbt: NBTTagCompound): FluidTank = {
    if(nbt.hasKey(this.name)){
      val tankData = nbt.getCompoundTag(this.name)
      super.readFromNBT(nbt)
      this.readTankFromNBT(tankData)
    }
    this
  }

  protected def writeTankToNBT(nbt: NBTTagCompound){}
  protected def readTankFromNBT(nbt: NBTTagCompound){}

  def getTooltip = this.tooltip

  def refreshTooltip(){
    this.tooltip.clear()
    var amount = 0
    if(this.getFluid != null && this.getFluid.amount > 0){
      val fluidName = new ToolTipLine(getFluidType.getLocalizedName)
      fluidName.spacing = 2
      tooltip.add(fluidName)
      amount = getFluidAmount
    }
    tooltip.add(new ToolTipLine(String.format(Locale.ENGLISH, "%,d / %,d", amount, this.getCapacity)))
  }

  protected final val tooltip = new ToolTip(){
    override def refresh() = refreshTooltip()
  }
}
