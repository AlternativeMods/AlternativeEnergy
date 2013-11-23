package jkmau5.alternativeenergy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import jkmau5.alternativeenergy.server.ProxyCommon;
import jkmau5.alternativeenergy.util.config.ConfigFile;
import jkmau5.alternativeenergy.util.config.ConfigTag;
import jkmau5.alternativeenergy.world.AltEngRecipes;
import jkmau5.alternativeenergy.world.blocks.AltEngBlocks;
import jkmau5.alternativeenergy.world.item.AltEngItems;
import jkmau5.alternativeenergy.world.tileentity.AltEngTileEntities;
import lombok.Getter;

@Mod(modid = "AlternativeEnergy", dependencies = "required-after:Forge@[9.11.1.942,);after:IC2;after:BuildCraft|Core;after:ComputerCraft")
@NetworkMod
public class AlternativeEnergy {

    @Getter
    @SuppressWarnings("unused")
    @SidedProxy(modId = Constants.MODID, serverSide = "jkmau5.alternativeenergy.server.ProxyCommon", clientSide = "jkmau5.alternativeenergy.client.ProxyClient")
    private static ProxyCommon proxy;

    @Getter
    private ConfigFile config;

    @Getter
    private static AlternativeEnergy instance;

    private AltEngCreativeTab creativeTab;

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        this.config = new ConfigFile(event.getSuggestedConfigurationFile()).setComment("AlternativeEnergy main config file");

        this.creativeTab = new AltEngCreativeTab();
        proxy.registerEventHandlers();

        AltEngBlocks.init(); //In 1.7, block registration should be in preInit. So, here we are!
        AltEngItems.init(); //In 1.7, item registration should be in preInit. So, here we are!
        AltEngTileEntities.init(); //In 1.7, tileEntity registration should be in preInit. So, here we are!
        AltEngRecipes.init();

        //Config stuff
        ConfigTag powerBoxTag = this.config.getTag("powerBox").useBraces().setComment("PowerBox settings");
        Config.powerBox_blockId = powerBoxTag.getTag("blockID").getIntValue(Config.powerBox_blockId);
        Config.powerBox_capacity = powerBoxTag.getTag("capacity").setComment("The max capacity for the powerbox").getIntValue(Config.powerBox_capacity);
        Config.powerBoxExplosionResistant = powerBoxTag.getTag("explosionResistant").setComment("Should the powerbox be explosion resistant?").getBooleanValue(Config.powerBoxExplosionResistant);
        Config.powerBox_capacity_multiplier = powerBoxTag.getTag("upgradeCapacityMultiplier").setComment("The capacity that will be added to the powerbox for every capacity upgrade inserted").getIntValue(Config.powerBox_capacity_multiplier);
        Config.powerCable_blockId = this.config.getTag("powerCable").setComment("PowerCable settings").useBraces().getTag("blockID").getIntValue(Config.powerCable_blockId);
        Config.linkBox_blockId = this.config.getTag("powerCable").setComment("PowerCable settings").useBraces().getTag("blockID").getIntValue(Config.powerCable_blockId);
        Config.upgrade_ItemId = this.config.getTag("upgrades").getTag("blockID").getIntValue(Config.upgrade_ItemId);
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        AltEngCompat.checkCompat();
    }
}
