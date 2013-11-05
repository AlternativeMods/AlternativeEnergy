package Lordmau5.PowerBoxes.item;

import Lordmau5.PowerBoxes.core.Main;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

/**
 * Author: Lordmau5
 * Date: 26.10.13
 * Time: 12:32
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ItemUpgrade extends Item {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public ItemUpgrade(int par1) {
        super(par1);
        setUnlocalizedName("upgrade");
        setCreativeTab(Main.tabPowerBox);
        setMaxStackSize(16);
        setHasSubtypes(true);
    }

    private String[] upgradeNames = {"capacity", "outputSpeed"};
    private String[] upgrades_Names = {"Capacity", "Output Speed"};

    public void addNames() {
        for(int i=0; i<upgrades_Names.length; i++)
            LanguageRegistry.addName(new ItemStack(this, 1, i), upgrades_Names[i] + " Upgrade");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iR)
    {
        icons = new Icon[2];
        for(int i = 0; i < icons.length; i++)
        {
            icons[i] = iR.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5) + "_" + upgradeNames[i]);
        }
    }

    public Icon getIconFromDamage(int par1)
    {
        return icons[par1];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int x = 0; x < icons.length; x++)
        {
            par3List.add(new ItemStack(this, 1, x));
        }
    }

    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = par1ItemStack.getItemDamage();
        return super.getUnlocalizedName() + "." + upgradeNames[i];
    }

}