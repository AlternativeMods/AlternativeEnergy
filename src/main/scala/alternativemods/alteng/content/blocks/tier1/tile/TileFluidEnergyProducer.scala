package alternativemods.alteng.content.blocks.tier1.tile

import net.minecraft.tileentity.TileEntity
import alternativemods.alteng.content.tileentities.SingleTankTile
import alternativemods.alteng.fluid.tank.RestrictedTank
import alternativemods.alteng.content.AltEngContent

/**
 * No description given
 *
 * @author jk-5
 */
class TileFluidEnergyProducer extends TileEntity with SingleTankTile {
  override val tank = new RestrictedTank(AltEngContent.fluidLiquidEnergy, "energyTank", 16000, this)
}
