package alternativemods.alteng.content.blocks.tier1.tile

import net.minecraft.tileentity.TileEntity
import alternativemods.alteng.content.tileentities.SingleTankTile
import alternativemods.alteng.fluid.tank.RestrictedTank
import alternativemods.alteng.content.AltEngContent
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import alternativemods.alteng.powertraits.tile.UniversalPowerTile
import alternativemods.alteng.util.Ratios

/**
 * No description given
 *
 * @author jk-5
 */
class TileFluidEnergyProducer extends TileEntity with SingleTankTile with UniversalPowerTile {
  var energy = 0d
  val maxEnergy = 1000d
  val ic2Ratio = Ratios.MJ
  val bcRatio = Ratios.EU
  val tank = new RestrictedTank(AltEngContent.fluidLiquidEnergy, "energyTank", 16000, this)

  override def canFill(from: ForgeDirection, fluid: Fluid) = false
}
