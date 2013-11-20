package jkmau5.alternativeenergy

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.network.NetworkMod
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.Mod.EventHandler
import jkmau5.alternativeenergy.util.config.ConfigFile
import jkmau5.alternativeenergy.world.item.AltEngItems
import jkmau5.alternativeenergy.world.blocks.AltEngBlocks
import jkmau5.alternativeenergy.world.tileentity.AltEngTileEntities
import jkmau5.alternativeenergy.world.AltEngRecipes

@Mod(modid = Constants.MODID, dependencies = "required-after:Forge@[9.11.1.942,);after:IC2;after:BuildCraft|Core;after:ComputerCraft", modLanguage = "scala")
@NetworkMod
object AlternativeEnergy {

  var config: ConfigFile = _

  @EventHandler def preInit(event: FMLPreInitializationEvent){
    this.config = new ConfigFile(event.getSuggestedConfigurationFile).setComment("AlternativeEnergy main config file")
    AltEngCreativeTab.init()
    AltEngProxy.registerEventHandlers()

    AltEngBlocks.init() //In 1.7, block registration should be in preInit. So, here we are!
    AltEngItems.init() //In 1.7, item registration should be in preInit. So, here we are!
    AltEngTileEntities.init() //In 1.7, tileEntity registration should be in preInit. So, here we are!
    AltEngRecipes.init()

    //Config stuff
    val powerBoxTag = this.config.getTag("powerBox").useBraces().setComment("PowerBox settings")
    Config.powerBox_blockId = powerBoxTag.getTag("blockID").getIntValue(Config.powerBox_blockId)
    Config.powerBox_capacity = powerBoxTag.getTag("capacity").setComment("The max capacity for the powerbox").getIntValue(Config.powerBox_capacity)
    Config.powerBoxExplosionResistant = powerBoxTag.getTag("explosionResistant").setComment("Should the powerbox be explosion resistant?").getBooleanValue(Config.powerBoxExplosionResistant)
    Config.powerBox_capacity_multiplier = powerBoxTag.getTag("upgradeCapacityMultiplier").setComment("The capacity that will be added to the powerbox for every capacity upgrade inserted").getIntValue(Config.powerBox_capacity_multiplier)
    Config.powerCable_blockId = this.config.getTag("powerCable").setComment("PowerCable settings").useBraces().getTag("blockID").getIntValue(Config.powerCable_blockId)
    Config.linkBox_blockId = this.config.getTag("powerCable").setComment("PowerCable settings").useBraces().getTag("blockID").getIntValue(Config.powerCable_blockId)
    Config.upgrade_ItemId = this.config.getTag("upgrades").getTag("blockID").getIntValue(Config.upgrade_ItemId)
  }

  @EventHandler def postInit(event: FMLPostInitializationEvent){
    AltEngProxy.checkCompat()
  }
}
