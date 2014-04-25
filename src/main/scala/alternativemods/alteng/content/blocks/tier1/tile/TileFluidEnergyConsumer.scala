package alternativemods.alteng.content.blocks.tier1.tile

import net.minecraft.tileentity.TileEntity
import alternativemods.alteng.powertraits.tile.UniversalPowerEjector
import alternativemods.alteng.content.tileentities.{TankTile, NeighborAwareTile}
import alternativemods.alteng.fluid.tank.RestrictedTank
import alternativemods.alteng.content.AltEngContent
import alternativemods.alteng.util.Ratios
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.common.util.ForgeDirection

/**
 * No description given
 *
 * @author jk-5
 */
class TileFluidEnergyConsumer extends TileEntity with UniversalPowerEjector with TankTile with NeighborAwareTile {
  var energy = 0d
  val maxEnergy = 1000d
  val maxPerTick = 100d
  val bcRatio = Ratios.MJ
  val ic2Ratio = Ratios.EU

  this.addTank(new RestrictedTank(AltEngContent.fluidLiquidEnergy, "energyTank", 16000, this))

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = {
    val drain = super.fill(from, resource, doFill = false) // Don't insert into the tank *yet* :3
    if(doFill){
      this.energy += resource.amount / 10 //TODO: check max space
    }
    drain
  }
}
