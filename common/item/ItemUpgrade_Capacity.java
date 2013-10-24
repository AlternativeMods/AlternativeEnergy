package item;

import core.Main;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

/**
 * Author: Lordmau5
 * Date: 23.08.13
 * Time: 16:59
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class ItemUpgrade_Capacity extends Item {

    public ItemUpgrade_Capacity(int par1) {
        super(par1);
        this.setUnlocalizedName("upgradeCapacity");
        setCreativeTab(Main.tabPowerBox);
        setMaxStackSize(16);
    }

    @Override
    public void registerIcons(IconRegister iR)
    {
        itemIcon = iR.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5));
    }
}