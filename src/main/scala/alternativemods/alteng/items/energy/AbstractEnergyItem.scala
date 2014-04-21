package alternativemods.alteng.items.energy

import net.minecraft.item.{ItemStack, Item}
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.relauncher.{SideOnly, Side}
import alternativemods.alteng.AlternativeEnergy
import alternativemods.alteng.util.{AlternativeElectricItemManager, Ratios}
import ic2.api.item.ISpecialElectricItem
import java.util

abstract class AbstractEnergyItem(maxCharge: Double, tier: Int, transferLimit: Int) extends Item
with IEnergyItem
with ISpecialElectricItem {

  setCreativeTab(AlternativeEnergy.creativeTab)
  setMaxStackSize(1)

  def setupEnergyItem(is: ItemStack) {
    createNBT(is)
    energy.setMaxStoredEnergy(is, maxCharge)
    energy.setStoredEnergy(is, 0)
  }

  override def onCreated(is: ItemStack, world: World, player: EntityPlayer) = {
    setupEnergyItem(is)
  }

  @SideOnly(Side.CLIENT)
  override def addInformation(is: ItemStack, player: EntityPlayer, list: util.List[_], par4: Boolean) = {
    if(is.getTagCompound == null || !is.getTagCompound.hasKey("stored"))
      setupEnergyItem(is)

    list.asInstanceOf[util.List[String]].add("Energy: " + Math.floor(energy.getStoredEnergy(is)).toInt + " / " + energy.getMaxStoredEnergy(is).toInt)
  }

  override def canProvideEnergy(is: ItemStack) = true

  override def getChargedItem(is: ItemStack) = this

  override def getEmptyItem(is: ItemStack) = this

  override def getTier(is: ItemStack) = tier

  override def getMaxCharge(is: ItemStack) = maxCharge.toInt

  override def getTransferLimit(is: ItemStack) = transferLimit

  override def getManager(is: ItemStack) = AlternativeElectricItemManager

  //---------- IEnergyItem ----------

  override def getTransferLimit: Int = transferLimit

  override def addEU(is: ItemStack, amount: Int): Int = {
    var inserted = amount
    val newStored = energy.getStoredEnergy(is) + (amount / Ratios.EU)

    if(newStored > energy.getMaxStoredEnergy(is)) {
      inserted -= Math.floor((energy.getStoredEnergy(is) - energy.getMaxStoredEnergy(is)) * Ratios.EU).toInt
    }
    energy.addEnergy(is, amount / Ratios.EU)

    inserted
  }

  override def removeEU(is: ItemStack, amount: Int): Int = {
    var removed = amount
    val newStored = energy.getStoredEnergy(is) - (amount / Ratios.EU)

    if(newStored < 0) {
      removed -= Math.round(-energy.getStoredEnergy(is) * Ratios.EU).toInt
    }
    energy.removeEnergy(is, amount / Ratios.EU)
    if(energy.getStoredEnergy(is) < 0.0)
      energy.setStoredEnergy(is, 0.0)

    removed
  }

}
