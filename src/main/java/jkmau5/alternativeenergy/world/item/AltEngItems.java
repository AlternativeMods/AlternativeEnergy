package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;

public class AltEngItems {

    public static ItemUpgrade itemUpgrade;

    public static void init(){
        itemUpgrade = new ItemUpgrade();

        GameRegistry.registerItem(itemUpgrade, "powerUpgrade");
    }
}