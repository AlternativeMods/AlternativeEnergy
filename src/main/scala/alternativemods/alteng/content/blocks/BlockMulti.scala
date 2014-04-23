package alternativemods.alteng.content.blocks

import net.minecraft.block.Block
import net.minecraft.item.ItemStack

/**
 * No description given
 *
 * @author jk-5
 */
trait BlockMulti extends Block {
  def getUnlocalizedName(stack: ItemStack): String
}
