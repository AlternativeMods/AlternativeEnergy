package alternativemods.alteng.content.items

import net.minecraft.item.{ItemStack, ItemBlock}
import alternativemods.alteng.content.blocks.BlockMulti
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.block.Block

/**
 * No description given
 *
 * @author jk-5
 */
final class ItemBlockMulti(block: Block) extends ItemBlock(block) {
  this.setMaxDamage(0)
  this.setNoRepair()
  this.setHasSubtypes(true)

  override def getUnlocalizedName(stack: ItemStack): String = this.field_150939_a match {
    case b: BlockMulti => super.getUnlocalizedName(stack) + "." + b.getUnlocalizedName(stack)
    case _ => super.getUnlocalizedName(stack)
  }

  @SideOnly(Side.CLIENT) override def getIconFromDamage(meta: Int) = this.field_150939_a.getIcon(2, meta)
  override def getMetadata(meta: Int) = meta
}
