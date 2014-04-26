package alternativemods.alteng.content.items

import net.minecraft.item.{EnumAction, ItemStack, Item}
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import alternativemods.alteng.util.{DamageSourceFluidEnergy, Side}
import net.minecraft.init.Items
import net.minecraft.potion.{PotionEffect, Potion}
import java.util
import net.minecraft.client.resources.I18n
import alternativemods.alteng.AlternativeEnergy

/**
 * No description given
 *
 * @author jk-5
 */
object ItemFluidEnergyBottle extends Item {
  this.setUnlocalizedName("AltEng.fluidEnergyBottle")
  this.setCreativeTab(AlternativeEnergy.creativeTab)
  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    player.setItemInUse(stack, this.getMaxItemUseDuration(stack))
    stack
  }
  override def getMaxItemUseDuration(stack: ItemStack) = 28
  override def getItemUseAction(stack: ItemStack) = EnumAction.drink
  override def onEaten(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if(!player.capabilities.isCreativeMode) stack.stackSize -= 1
    if(Side(world).isServer){
      player.attackEntityFrom(DamageSourceFluidEnergy, 4)
      player.getFoodStats.addStats(10, 0.9f)
      player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, (10 + world.rand.nextInt(8)) * 20, 1, true))
    }
    if(stack.stackSize <= 0) new ItemStack(Items.glass_bottle) else stack
  }
  override def addInformation(stack: ItemStack, player: EntityPlayer, list: util.List[_], debug: Boolean){
    list.asInstanceOf[util.List[String]].add(I18n.format(this.getUnlocalizedName(stack) + ".tooltip"))
    super.addInformation(stack, player, list, debug)
  }
}
