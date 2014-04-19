package alternativemods.alteng.items

import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.block.Block

class ItemBlockConveyor(block: Block) extends ItemBlock(block) {
  setHasSubtypes(true)
  val subNames = Array("conveyor", "conveyorInsertion")

  override def getMetadata(metadata: Int) = metadata

  override def getUnlocalizedName(is: ItemStack) = getUnlocalizedName() + "." + subNames(is.getItemDamage())
}
