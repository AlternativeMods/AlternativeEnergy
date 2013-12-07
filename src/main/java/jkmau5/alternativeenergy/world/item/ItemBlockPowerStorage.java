package jkmau5.alternativeenergy.world.item;

import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.world.blocks.AltEngBlocks;
import jkmau5.alternativeenergy.world.blocks.BlockPowerStorage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class ItemBlockPowerStorage extends ItemBlock {

    public ItemBlockPowerStorage(int id) {
        super(id);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setNoRepair();
    }

    @Override
    public Icon getIconFromDamage(int par1) {
        return AltEngBlocks.blockPowerStorage.getIcon(2, par1);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public void onCreated(ItemStack is, World world, EntityPlayer player) {
        is.setTagCompound(new NBTTagCompound());
        is.getTagCompound().setInteger("storedPower", 0);
    }

    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean par4) {
        if(GuiScreen.isShiftKeyDown()) {
            if(!is.hasTagCompound()) {
                is.setTagCompound(new NBTTagCompound());
                is.getTagCompound().setInteger("storedPower", 0);
            }

            int pBoxCapacity = Config.powerBox_capacity;
            int capacityAmount = 0;
            int storedPower = 0;
            int outputSpeedUpgrades = 0;
            boolean gotUpgrade = false;

            if(is.hasTagCompound()) {
                if(is.getTagCompound().hasKey("storedPower")) {
                    storedPower = is.getTagCompound().getInteger("storedPower");
                }
                if(is.getTagCompound().hasKey("capacityUpgrade")) {
                    gotUpgrade = true;
                    int capacityUpgrades = is.getTagCompound().getInteger("capacityUpgrade");
                    for(int i = 1; i <= capacityUpgrades; i++) {
                        pBoxCapacity += i * Config.powerBox_capacity_multiplier;
                    }
                    capacityAmount = capacityUpgrades;
                }
                if(is.getTagCompound().hasKey("outputSpeedUpgrade")) {
                    gotUpgrade = true;
                    outputSpeedUpgrades = is.getTagCompound().getInteger("outputSpeedUpgrade");
                }
            }

            list.add("Stored Power: " + Config.convertNumber(storedPower) + "/" + Config.convertNumber(pBoxCapacity));
            if(gotUpgrade) {
                list.add(" ");
                if(capacityAmount > 0) {
                    list.add("Capacity increased by " + capacityAmount);
                }
                if(outputSpeedUpgrades > 0) {
                    list.add("Output Speed increased by " + outputSpeedUpgrades);
                }
            }
        } else {
            list.add("Hold shift to get more information");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.altEng." + BlockPowerStorage.iconNames[stack.getItemDamage()];
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
}
