package alternativemods.alteng.content.blocks

import net.minecraft.block.material.Material
import alternativemods.alteng.AlternativeEnergy
import net.minecraftforge.fluids.{BlockFluidClassic, Fluid}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraft.entity.Entity
import alternativemods.alteng.util.DamageSourceFluidEnergy

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
    icons(0) = register.registerIcon("alteng:fluidEnergy")
    icons(1) = register.registerIcon("alteng:fluidEnergy_Flowing")
  }

  override def getIcon(side: Int, metadata: Int): IIcon = {
    side match {
      case 0 | 1 => icons(0)
      case _ => icons(1)
    }
  }

  override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, entity: Entity){
    if(entity.ticksExisted % 15 == 0){
      entity.attackEntityFrom(DamageSourceFluidEnergy, 3)
    }
  }
}
