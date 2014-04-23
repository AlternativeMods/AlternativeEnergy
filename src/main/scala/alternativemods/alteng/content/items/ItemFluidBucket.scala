package alternativemods.alteng.content.items

import net.minecraft.item.{Item, ItemBucket}
import net.minecraft.block.Block
import alternativemods.alteng.AlternativeEnergy
import net.minecraft.init.Items
import cpw.mods.fml.relauncher.Side

/**
 * No description given
 *
 * @author jk-5
 */
class ItemFluidBucket(block: Block) extends ItemBucket(block) {
  setCreativeTab(AlternativeEnergy.creativeTab)
  setContainerItem(Items.bucket)
}
