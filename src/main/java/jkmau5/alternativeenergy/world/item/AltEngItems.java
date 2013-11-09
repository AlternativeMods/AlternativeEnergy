package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;
import jkmau5.alternativeenergy.Config;

/**
 * Author: Lordmau5
 * Date: 23.08.13
 * Time: 16:57
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class AltEngItems {

    public static ItemUpgrade itemUpgrade;

    public static void init(){
        itemUpgrade = new ItemUpgrade(Config.upgrade_ItemId);

        GameRegistry.registerItem(itemUpgrade, "upgrade");
    }
}