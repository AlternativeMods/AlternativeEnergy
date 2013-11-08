package ic2.alternativeenergy.world.blocks;

import ic2.alternativeenergy.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Author: Lordmau5
 * Date: 22.08.13
 * Time: 00:10
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ItemBlockPowerBox extends ItemBlock {

    public ItemBlockPowerBox(int par1) {
        super(par1);
    }

    @Override
    public void onCreated(ItemStack is, World world, EntityPlayer player) {
        is.setTagCompound(new NBTTagCompound());
        is.getTagCompound().setInteger("storedPower", 0);
    }

    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean par4) {
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if(is.hasTagCompound() == false) {
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
                    for(int i=1; i<=capacityUpgrades; i++) {
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
                if(capacityAmount > 0)
                    list.add("Capacity increased by " + capacityAmount);
                if(outputSpeedUpgrades > 0)
                    list.add("Output Speed increased by " + outputSpeedUpgrades);
            }

        }
        else {
            list.add("Hold shift to get more information");
        }
    }
}