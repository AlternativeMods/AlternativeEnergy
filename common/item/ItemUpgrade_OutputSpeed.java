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
public class ItemUpgrade_OutputSpeed extends Item {

    public ItemUpgrade_OutputSpeed(int par1) {
        super(par1);
        this.setUnlocalizedName("upgradeOutputSpeed");
        setCreativeTab(Main.tabPowerBox);
        setMaxStackSize(2);
    }

    @Override
    public void registerIcons(IconRegister iR)
    {
        itemIcon = iR.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5));
    }
}