package alternativemods.alteng.powertraits.tile

import alternativemods.alteng.util.{ToolTipLine, ToolTip}
import alternativemods.alteng.gui.containers.ContainerFluidEnergyProducer
import java.util
import net.minecraft.inventory.ICrafting
import net.minecraft.entity.player.EntityPlayer
import alternativemods.alteng.network.{PacketGuiIntUpdate, NetworkHandler}
import scala.collection.JavaConversions._
import cpw.mods.fml.relauncher.{Side, SideOnly}

/**
 * No description given
 *
 * @author jk-5
 */
trait BasePowerTrait {
  def energy: Double
  protected[this] def energy_=(en: Double)
  def maxEnergy: Double

  private var prevEnergy = -1d
  private var prevMaxEnergy = -1d

  @SideOnly(Side.CLIENT) var clientEnergy = -1
  @SideOnly(Side.CLIENT) var clientMaxEnergy = -1

  private val line = new ToolTipLine()
  private val toolTip = new ToolTip(){
    override def refresh(){
      line.text = s"$clientEnergy / $clientMaxEnergy"
    }
  }

  toolTip.add(line)

  def getToolTip = this.toolTip

  def updateGuiEnergyData(container: ContainerFluidEnergyProducer, crafters: util.List[ICrafting], index: Int = 0){
    crafters.foreach(c => {
      val player = c.asInstanceOf[EntityPlayer]
      if(this.energy != this.prevEnergy){
        NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index, this.energy.toInt), player)
      }
      if(this.maxEnergy != this.prevMaxEnergy){
        NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index + 1, this.maxEnergy.toInt), player)
      }
    })
    this.prevEnergy = this.energy
    this.prevMaxEnergy = this.maxEnergy
  }

  def initGuiEnergyData(container: ContainerFluidEnergyProducer, crafter: ICrafting, index: Int = 0){
    NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index, this.energy.toInt), crafter.asInstanceOf[EntityPlayer])
    NetworkHandler.sendPacketToPlayer(new PacketGuiIntUpdate(container.windowId, index + 1, this.maxEnergy.toInt), crafter.asInstanceOf[EntityPlayer])
  }

  @SideOnly(Side.CLIENT)
  def onGuiEnergyUpdate(id: Int, data: Int, index: Int = 0){
    if(id < index || id > index + 1) return
    if(id % 2 == 0) this.clientEnergy = data
    else this.clientMaxEnergy = data
  }
}
