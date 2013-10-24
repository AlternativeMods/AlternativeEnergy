package core;


import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.Ic2Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tile.TileEntityPowerBox;

/**
 * Author: Lordmau5
 * Date: 24.08.13
 * Time: 17:06
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class InvSlotDisCharge extends InvSlot {

    public InvSlotDisCharge(TileEntityPowerBox base, int oldStartIndex) {
        super(base, "discharge", oldStartIndex, InvSlot.Access.IO, 1, null);
    }

    public boolean accepts(ItemStack itemStack)
    {
        Item item = itemStack.getItem();

        if ((item instanceof IElectricItem))
            return (((IElectricItem)item).canProvideEnergy(itemStack)) && (((IElectricItem)item).getTier(itemStack) <= 3);
        if ((item.itemID == Ic2Items.suBattery.itemID) || (item.itemID == Item.redstone.itemID))
        {
            return true;
        }

        return (Info.itemEnergy.getEnergyValue(itemStack) > 0) && ((!(item instanceof IElectricItem)) || (((IElectricItem)item).getTier(itemStack) <= 3));
    }

    public IElectricItem getItem()
    {
        ItemStack itemStack = get(0);
        if (itemStack == null) return null;

        return (IElectricItem)itemStack.getItem();
    }

    public int discharge(int amount, boolean ignoreLimit) {
        ItemStack itemStack = get(0);
        if (itemStack == null) return 0;

        int energyValue = Info.itemEnergy.getEnergyValue(itemStack);
        if (energyValue == 0) return 0;

        Item item = itemStack.getItem();

        if ((item instanceof IElectricItem)) {
            IElectricItem elItem = (IElectricItem)item;

            if ((!elItem.canProvideEnergy(itemStack)) || (elItem.getTier(itemStack) > 3))
            {
                return 0;
            }

            return ElectricItem.manager.discharge(itemStack, amount, 3, ignoreLimit, false);
        }
        itemStack.stackSize -= 1;
        if (itemStack.stackSize <= 0) put(0, null);

        return energyValue;
    }
}