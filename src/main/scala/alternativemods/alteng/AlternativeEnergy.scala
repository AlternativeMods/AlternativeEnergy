package alternativemods.alteng

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.Mod.EventHandler
import org.apache.logging.log4j.LogManager
import alternativemods.alteng.network.NetworkHandler
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import alternativemods.alteng.content.AltEngContent

@Mod(modid = "AltEng", name = "AlternativeEnergy", modLanguage = "scala")
object AlternativeEnergy {

  val logger = LogManager.getLogger("AltEng")
  val creativeTab = new CreativeTabs("altEng") {
    def getTabIconItem = new ItemStack(AltEngContent.blockConveyor, 1, 0).getItem
  }

  @EventHandler def preInit(event: FMLPreInitializationEvent){
    this.logger.info("Loading AlternativeEnergy")

    AltEngContent.load()
    NetworkHandler.init(event.getSide)
  }
}
