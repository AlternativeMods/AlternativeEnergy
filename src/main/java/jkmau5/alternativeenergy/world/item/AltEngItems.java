package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;

public class AltEngItems {

    public static ItemUpgrade itemUpgrade;
    public static ItemAlternativeWrench itemAlternativeWrench;
    public static ItemWrenchUpgrade itemWrenchUpgrade;

    public static void init() {
        itemUpgrade = new ItemUpgrade();
        itemAlternativeWrench = new ItemAlternativeWrench();
        itemWrenchUpgrade = new ItemWrenchUpgrade();
        //GregTech_API.registerWrench(new ItemStack(itemAlternativeWrench));
        //GregTech_API.sWrenchList.add(Integer.valueOf(GT_Utility.stackToInt(new ItemStack(itemAlternativeWrench))));

        //The names in here are the new itemIDs. Do not change them!
        GameRegistry.registerItem(itemUpgrade, "upgrade");
        GameRegistry.registerItem(itemAlternativeWrench, "alternativeWrench");
        GameRegistry.registerItem(itemWrenchUpgrade, "wrenchUpgrade");
    }
}