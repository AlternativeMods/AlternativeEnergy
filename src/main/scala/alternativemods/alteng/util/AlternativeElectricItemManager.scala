package alternativemods.alteng.util

import net.minecraft.item.ItemStack
import net.minecraft.entity.EntityLivingBase
import alternativemods.alteng.items.energy.IEnergyItem
import ic2.api.item.IElectricItemManager

class AlternativeElectricItemManager extends IElectricItemManager {

  def chargeFromArmor(is: ItemStack, entity: EntityLivingBase) = {}

  def charge(is: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    if(!is.getItem.isInstanceOf[IEnergyItem])
      return 0

    if(simulate)
      return 0

    val eItem = is.getItem.asInstanceOf[IEnergyItem]
    if(eItem.energy.getStoredEnergy(is) >= eItem.energy.getMaxStoredEnergy(is))
      return 0

    var realAmount = amount
    if(realAmount > eItem.getTransferLimit && !ignoreTransferLimit)
      realAmount = eItem.getTransferLimit
    eItem.addEU(is, realAmount)
  }

  def discharge(is: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    0
  }

  def getCharge(is: ItemStack) = {
    if(!is.getItem.isInstanceOf[IEnergyItem])
     0

    Math.floor(is.getItem.asInstanceOf[IEnergyItem].energy.getMaxStoredEnergy(is) / Ratios.EU).toInt
  }

  def canUse(is: ItemStack, amount: Int): Boolean = {
    false
  }

  def use(is: ItemStack, amount: Int, entity: EntityLivingBase): Boolean = {
    false
  }

  def getToolTip(is: ItemStack) = ""
}
