package jkmau5.alternativeenergy.world.item;

import jkmau5.alternativeenergy.AlternativeEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Author: Lordmau5
 * Date: 07.12.13
 * Time: 10:20
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ItemWrenchUpgrade extends AltEngItem {

    protected ItemWrenchUpgrade() {
        super("wrenchUpgrade");
        setCreativeTab(AlternativeEnergy.getInstance().getCreativeTab());
        setHasSubtypes(true);
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {

    }
}