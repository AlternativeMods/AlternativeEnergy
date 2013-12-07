package jkmau5.alternativeenergy.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * No description given
 *
 * @author jk-5
 */
public class InventoryUtils {

    public static boolean areItemsEqual(ItemStack item1, ItemStack item2) {
        return areItemsEqual(item1, item2, true, true);
    }

    public static boolean areItemsEqual(ItemStack item1, ItemStack item2, boolean matchDamage, boolean matchNBT) {
        if(item1 == null || item2 == null) {
            return false;
        }
        if(item1.itemID != item2.itemID) {
            return false;
        }
        if(matchNBT && !ItemStack.areItemStackTagsEqual(item1, item2)) {
            return false;
        }
        if(matchDamage && item1.getHasSubtypes()) {
            if(isWildcardItem(item1) || isWildcardItem(item2)) {
                return true;
            }
            if(item1.getItemDamage() != item2.getItemDamage()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWildcardItem(ItemStack itemStack) {
        return itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }
}
