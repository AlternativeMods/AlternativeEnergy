package alternativemods.alteng

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.Mod.EventHandler
import org.apache.logging.log4j.LogManager
import jk_5.commons.config.ConfigFile
import alternativemods.alteng.blocks.AltEngBlocks
import alternativemods.alteng.tileentities.AltEngTileEntities

/**
 * No description given
 *
 * @author jk-5
 */
@Mod(modid = "AltEng", name = "AlternativeEnergy", modLanguage = "scala")
object AlternativeEnergy {

  val logger = LogManager.getLogger("AltEng")

  @EventHandler def preInit(event: FMLPreInitializationEvent){
    this.logger.info("Loading AlternativeEnergy")

    val config = ConfigFile.fromFile(event.getSuggestedConfigurationFile).setComment("AlternativeEnergy main config file")
    AltEngBlocks.load()
    AltEngTileEntities.load()
  }
}
