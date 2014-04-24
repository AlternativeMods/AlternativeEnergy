package alternativemods.alteng.util

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.fluids.FluidStack
import net.minecraft.util.{ResourceLocation, IIcon}
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.Minecraft.{getMinecraft => mc}

object Util {
  def diff(a: Double, b: Double) = if(a > b) a - b else b - a
}

object InventoryUtils {

  def isWildcardItem(stack: ItemStack) = stack.getItemDamage == OreDictionary.WILDCARD_VALUE
  def areItemsEqual(item1: ItemStack, item2: ItemStack, matchDamage: Boolean = true, matchNBT: Boolean = true): Boolean = {
    if(item1 == null || item2 == null) false
    if(item1.getItem != item2.getItem) false
    if(matchNBT && !ItemStack.areItemStackTagsEqual(item1, item2)) false
    if(matchDamage && !item1.getHasSubtypes){
      if(isWildcardItem(item1) || isWildcardItem(item2)) return true
      if(item1.getItemDamage != item2.getItemDamage) return false
    }
    true
  }
}

object RenderUtils {
  def getFluidTexture(fluidStack: FluidStack, flowing: Boolean): IIcon = {
    if(fluidStack == null) return getIconSafe(null)
    val fluid = fluidStack.getFluid
    if(fluid == null) return getIconSafe(null)
    val icon = if(flowing) fluid.getFlowingIcon else fluid.getStillIcon
    getIconSafe(icon)
  }

  def getIconSafe(icon: IIcon): IIcon = {
    if(icon == null) mc.getTextureManager.getTexture(TextureMap.locationBlocksTexture).asInstanceOf[TextureMap].getAtlasSprite("missingno")
    else icon
  }

  implicit def bind(resourceLocation: ResourceLocation){
    mc.getTextureManager.bindTexture(resourceLocation)
  }
}
