package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

public class AltEngItems {

    public static ItemUpgrade itemUpgrade;
    public static ItemAlternativeWrench itemAlternativeWrench;
    public static ItemWrenchUpgrade itemWrenchUpgrade;

    public static void init() {
        itemUpgrade = new ItemUpgrade();
        GameRegistry.registerItem(itemUpgrade, "upgrade");

        itemAlternativeWrench = new ItemAlternativeWrench();
        GameRegistry.registerItem(itemAlternativeWrench, "alternativeWrench");
        GregTech_API.sWrenchList.add(Integer.valueOf(GT_Utility.stackToInt(new ItemStack(itemAlternativeWrench))));

        itemWrenchUpgrade = new ItemWrenchUpgrade();
        GameRegistry.registerItem(itemWrenchUpgrade, "wrenchUpgrade");
    }
}