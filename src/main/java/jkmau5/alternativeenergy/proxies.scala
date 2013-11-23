package jkmau5.alternativeenergy

import cpw.mods.fml.common.network.NetworkRegistry
import jkmau5.alternativeenergy.server.PacketHandler
import jkmau5.alternativeenergy.gui.GuiHandler
import net.minecraft.world.World
import jkmau5.alternativeenergy.client.render.{BlockPowerCableRender, Render}
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.registry.TickRegistry
import jkmau5.alternativeenergy.client.TickHandlerClient
import cpw.mods.fml.relauncher.{SideOnly, Side}
import cpw.mods.fml.common.{FMLCommonHandler, Loader}
import javax.swing.JOptionPane
import net.minecraft.item.ItemStack
import buildcraft.api.tools.IToolWrench

/**
 * No description given
 *
 * @author jk-5
 */
class AltEngProxy_serverImpl{
  def registerEventHandlers(){
    NetworkRegistry.instance().registerChannel(new PacketHandler(), "AltEng")
    NetworkRegistry.instance().registerGuiHandler(AlternativeEnergy, new GuiHandler())
  }
  def getTicks(world: World) = world.getTotalWorldTime
}

//Everything in the clientProxy should be @SideOnly(Side.CLIENT)
class AltEngProxy_clientImpl extends AltEngProxy_serverImpl {
  @SideOnly(Side.CLIENT)
  override def registerEventHandlers(){
    super.registerEventHandlers()

    Render.RENDER_BLOCKPOWERCABLE = RenderingRegistry.getNextAvailableRenderId
    RenderingRegistry.registerBlockHandler(new BlockPowerCableRender)

    TickRegistry.registerTickHandler(new TickHandlerClient, Side.CLIENT)
  }
  @SideOnly(Side.CLIENT)
  override def getTicks(world: World) = TickHandlerClient.getTicks
}

object AltEngProxy extends AltEngProxy_clientImpl {

  var hasBC = Loader.isModLoaded("BuildCraft|Transport")
  var hasIC2 = Loader.isModLoaded("IC2")
  var hasCC = Loader.isModLoaded("ComputerCraft")

  def checkCompat(){
    hasBC = Loader.isModLoaded("BuildCraft|Transport")
    hasIC2 = Loader.isModLoaded("IC2")
    hasCC = Loader.isModLoaded("ComputerCraft")
    if(!hasIC2 && !hasBC){
      if (FMLCommonHandler.instance.getSide.isClient) {
        val optionPane = new JOptionPane
        optionPane.setMessage("AlternativeEnergy is useless without one of the corresponding mods (BuildCraft or IndustrialCraft2).\n" + "Install atleast one of them, and the mod will have features!")
        optionPane.setMessageType(JOptionPane.WARNING_MESSAGE)
        val dialog = optionPane.createDialog("Compatability warning!")
        dialog.setAlwaysOnTop(true)
        dialog.setVisible(true)
      }
    }
  }

  def isWrench(stack: ItemStack): Boolean = stack.getItem match{
    case w: IToolWrench => true
    case w => w.equals(ic2.api.item.Items.getItem("wrench")) || w.equals(ic2.api.item.Items.getItem("electricWrench"))
  }
}
