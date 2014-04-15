package alternativemods.alteng.items.energy

import net.minecraft.item.{ItemStack, Item}
import ic2.api.item.ISpecialElectricItem
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.relauncher.{SideOnly, Side}
import java.util.List
import alternativemods.alteng.AlternativeEnergy

/**
 * @author: Lordmau5
 * @date: 14.04.14 - 16:17
 */
abstract class AbstractEnergyItem(maxCharge: Int, tier: Int, transferLimit: Int) extends Item
with IEnergyItem
with ISpecialElectricItem {

  setCreativeTab(AlternativeEnergy.creativeTab)
  setMaxStackSize(1)

  val buffer = new EnergyBuffer

  def setupEnergyItem(is: ItemStack) {
    createNBT(is)
    energy.setMaxStoredEnergy(is, maxCharge)
    energy.setStoredEnergy(is, 0)
  }

  override def onCreated(is: ItemStack, world: World, player: EntityPlayer) = {
    setupEnergyItem(is)
  }

  @SideOnly(Side.CLIENT)
  override def addInformation(is: ItemStack, player: EntityPlayer, list: List[_], par4: Boolean) = {
    if(is.getTagCompound == null || !is.getTagCompound.hasKey("stored"))
      setupEnergyItem(is)

    list.asInstanceOf[List[String]].add("Energy: " + energy.getStoredEnergy(is) + " / " + energy.getMaxStoredEnergy(is))
  }

  override def canProvideEnergy(is: ItemStack) = false

  override def getChargedItem(is: ItemStack) = this

  override def getEmptyItem(is: ItemStack) = this

  override def getTier(is: ItemStack) = tier

  override def getMaxCharge(is: ItemStack) = maxCharge

  override def getTransferLimit(is: ItemStack) = transferLimit

  override def getManager(is: ItemStack) = AlternativeEnergy.alternativeElectricItemManager

  //---------- IEnergyItem ----------

  override def getTransferLimit: Int = transferLimit

  override def addEU(is: ItemStack, amount: Int): Int = {
    var inserted = amount
    var addToEnergy = 0

    buffer.addBuffer("EU", is, amount)
    if(buffer.getBuffer("EU", is) > AlternativeEnergy.alternativeElectricItemManager.conversionRatio) {
      val i = Math.floor(buffer.getBuffer("EU", is) / AlternativeEnergy.alternativeElectricItemManager.conversionRatio).toInt
      addToEnergy += i
      buffer.removeBuffer("EU", is, i * AlternativeEnergy.alternativeElectricItemManager.conversionRatio)
    }

    val newStored = energy.getStoredEnergy(is) + addToEnergy

    if(newStored > energy.getMaxStoredEnergy(is)) {
      inserted -= Math.floor((energy.getStoredEnergy(is) - energy.getMaxStoredEnergy(is)) / AlternativeEnergy.alternativeElectricItemManager.conversionRatio).toInt
    }
    energy.addEnergy(is, addToEnergy)

    inserted
  }

}
