package alternativemods.alteng.items.energy

import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.block.Block

/**
 * @author: Lordmau5
 * @date: 06.04.14 - 11:21
 */
class ItemBlockEnergyStorage(block: Block) extends ItemBlock(block) {

  setUnlocalizedName("energyStorage")
  setHasSubtypes(true)
  val subNames = Array("small", "medium", "high")

  override def getMetadata(metadata: Int) = metadata

  override def getUnlocalizedName(is: ItemStack) = getUnlocalizedName() + "." + subNames(is.getItemDamage())

}
