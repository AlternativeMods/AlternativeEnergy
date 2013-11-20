package jkmau5.alternativeenergy

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import jkmau5.alternativeenergy.world.blocks.AltEngBlocks

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngCreativeTab extends CreativeTabs("altEng") {
  def init() = {}
  override def getIconItemStack = new ItemStack(AltEngBlocks.blockPowerCable, 1, 0)
}
