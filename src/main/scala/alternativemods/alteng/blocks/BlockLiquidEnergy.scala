package alternativemods.alteng.blocks

import net.minecraft.block.material.Material
import alternativemods.alteng.AlternativeEnergy
import net.minecraftforge.fluids.{Fluid, BlockFluidFinite}

/**
 * No description given
 *
 * @author jk-5
 */
class BlockLiquidEnergy(fluid: Fluid) extends BlockFluidFinite(fluid, Material.water) {
  this.setCreativeTab(AlternativeEnergy.creativeTab)
  this.setBlockName("altEng.liquidEnergy")
}
