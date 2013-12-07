package jkmau5.alternativeenergy.gui.container.slot;

import jkmau5.alternativeenergy.client.render.ToolTip;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngSlot extends Slot {

    @Getter
    @Setter
    private ToolTip toolTip;

    public AltEngSlot(IInventory inventory, int slotIndex, int posX, int posY) {
        super(inventory, slotIndex, posX, posY);
    }

    public boolean isFakeSlot() {
        return false;
    }

    public boolean canAdjustFakeSlot() {
        return true;
    }

    public boolean canShiftClick() {
        return true;
    }
}
