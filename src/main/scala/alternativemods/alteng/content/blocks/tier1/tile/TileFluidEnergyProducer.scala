package alternativemods.alteng.content.blocks.tier1.tile

import net.minecraft.tileentity.TileEntity
import alternativemods.alteng.content.tileentities.{TimeTrackingTile, TankTile, InventoryTile}
import alternativemods.alteng.fluid.tank.RestrictedTank
import alternativemods.alteng.content.AltEngContent
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{FluidStack, Fluid}
import alternativemods.alteng.powertraits.tile.UniversalPowerConsumer
import alternativemods.alteng.util.{FluidUtils, InventoryObject, Ratios}
import alternativemods.alteng.gui.GuiTile
import cpw.mods.fml.relauncher.{SideOnly, Side}
import alternativemods.alteng.gui.guis.GuiFluidEnergyProducer
import alternativemods.alteng.gui.containers.ContainerFluidEnergyProducer
import net.minecraft.entity.player.EntityPlayer

/**
 * No description given
 *
 * @author jk-5
 */
class TileFluidEnergyProducer extends TileEntity with TankTile with UniversalPowerConsumer with GuiTile with InventoryTile with TimeTrackingTile {

  var energy = 0d
  val maxEnergy = 1000d
  val ic2Ratio = Ratios.EU
  val bcRatio = Ratios.MJ
  val inventory = new InventoryObject(2, "fluidEnergyProducer", 64)

  this.addTank(new RestrictedTank(AltEngContent.fluidLiquidEnergy, "energyTank", 16000, this))

  override def canFill(from: ForgeDirection, fluid: Fluid) = false
  @SideOnly(Side.CLIENT) override def clientGui(id: Int, player: EntityPlayer): AnyRef = new GuiFluidEnergyProducer(player.inventory, this)
  override def serverGui(id: Int, player: EntityPlayer): AnyRef = new ContainerFluidEnergyProducer(player.inventory, this)

  override def updateEntity(){
    if(this.energy > 10){
      this.energy -= this.tanks.get(0).fill(new FluidStack(AltEngContent.fluidLiquidEnergy, 1), true) * 10
    }
    if(this.ellapsed % 10 == 0){
      FluidUtils.fillContainer(this.tanks.get(0), this.inventory, 0, 1, AltEngContent.fluidLiquidEnergy)
    }

    super.updateEntity()
  }
}
