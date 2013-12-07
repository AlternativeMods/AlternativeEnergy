package jkmau5.alternativeenergy;

import jkmau5.alternativeenergy.world.item.AltEngItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public class AltEngCreativeTab extends CreativeTabs {

    public AltEngCreativeTab() {
        super("altEng");
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(AltEngItems.itemUpgrade, 1, 0);
    }
}
