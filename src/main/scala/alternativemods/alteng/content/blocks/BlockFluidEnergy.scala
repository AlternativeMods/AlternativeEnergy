package alternativemods.alteng.content.blocks

import net.minecraft.block.material.Material
import alternativemods.alteng.AlternativeEnergy
import net.minecraftforge.fluids.{BlockFluidClassic, Fluid, BlockFluidFinite}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import javax.swing.Icon
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon

/**
 * No description given
 *
 * @author jk-5
 */
class BlockFluidEnergy(fluid: Fluid) extends BlockFluidClassic(fluid, Material.water) {
  this.setCreativeTab(AlternativeEnergy.creativeTab)
  this.setBlockName("altEng.liquidEnergy")

  @SideOnly(Side.CLIENT)
  var icons = new Array[IIcon](2)

  override def registerBlockIcons(register: IIconRegister): Unit = {
    icons(0) = register.registerIcon("alternativeenergy:fluidEnergy")
    icons(1) = register.registerIcon("alternativeenergy:fluidEnergy_Flowing")
  }

  override def getIcon(side: Int, metadata: Int): IIcon = {
    side match {
      case 0 | 1 => icons(0)
      case _ => icons(1)
    }
  }
}
