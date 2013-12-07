package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;

public class AltEngItems {

    public static ItemUpgrade itemUpgrade;
    public static ItemAlternativeWrench itemAlternativeWrench;
    public static ItemWrenchUpgrade itemWrenchUpgrade;

    public static void init(){
        itemUpgrade = new ItemUpgrade();
        GameRegistry.registerItem(itemUpgrade, "upgrade");

        itemAlternativeWrench = new ItemAlternativeWrench();
        GameRegistry.registerItem(itemAlternativeWrench, "alternativeWrench");

        itemWrenchUpgrade = new ItemWrenchUpgrade();
        GameRegistry.registerItem(itemWrenchUpgrade, "wrenchUpgrade");
    }
}