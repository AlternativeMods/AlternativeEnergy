package alternativemods.alteng.items

import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 * @author: Lordmau5
 * @date: 03.04.14 - 10:11
 */
class ItemBlockConveyor(block: Block) extends ItemBlock(block) {
  setHasSubtypes(true)
  val subNames = Array("conveyor", "conveyorInsertion")

  override def getMetadata(metadata: Int) = metadata

  override def getUnlocalizedName(is: ItemStack) = getUnlocalizedName() + "." + subNames(is.getItemDamage())
}
