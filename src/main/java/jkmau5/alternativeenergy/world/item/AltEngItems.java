package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;
import jkmau5.alternativeenergy.Config;

public class AltEngItems {

    public static ItemUpgrade itemUpgrade;

    public static void init(){
        itemUpgrade = new ItemUpgrade(Config.upgrade_ItemId);

        GameRegistry.registerItem(itemUpgrade, "powerUpgrade");
    }
}