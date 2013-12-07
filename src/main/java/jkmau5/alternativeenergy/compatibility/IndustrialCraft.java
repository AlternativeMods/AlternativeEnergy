package jkmau5.alternativeenergy.compatibility;

import ic2.api.item.IElectricItemManager;
import jkmau5.alternativeenergy.AltEngSupport;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.world.item.AltEngItems;
import jkmau5.alternativeenergy.world.item.ItemAlternativeWrench;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Author: Lordmau5
 * Date: 04.12.13
 * Time: 16:46
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class IndustrialCraft {

    public static class AlternativeElectricItemManager implements IElectricItemManager {

        @Override
        public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
            if(itemStack.getItem() != AltEngItems.itemAlternativeWrench) {
                return 0;
            }
            if(amount >= 512)
                amount = 512;

            int storedPower = AltEngSupport.initiateOrGetNBTInteger(itemStack, "storedPower");
            if(storedPower >= ItemAlternativeWrench.maxStoredPower)
                return 0;

            storedPower += (int) Math.floor(amount / Ratios.EU.conversion);
            if(storedPower > ItemAlternativeWrench.maxStoredPower) {
                amount -= Math.ceil((storedPower - ItemAlternativeWrench.maxStoredPower) * Ratios.EU.conversion);
                storedPower = ItemAlternativeWrench.maxStoredPower;
            }

            AltEngSupport.setNBTInteger(itemStack, "storedPower", storedPower);

            return amount;
        }

        @Override
        public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
            System.out.println("Discharge, seriously?");
            return 0;
        }

        @Override
        public int getCharge(ItemStack itemStack) {
            return 0;
        }

        @Override
        public boolean canUse(ItemStack itemStack, int amount) {
           return false;
           //return AltEngSupport.canWrench(itemStack);
        }

        @Override
        public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity) {
            System.out.println(amount);
            if(amount == 0)
                return false;
            AltEngSupport.drainWrenchPower(itemStack, true);
            return true;
        }

        @Override
        public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {

        }

        @Override
        public String getToolTip(ItemStack itemStack) {
            return null;
        }
    }
}