package jkmau5.alternativeenergy.gui.container;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.gui.container.slot.AltEngSlot;
import jkmau5.alternativeenergy.gui.element.Element;
import jkmau5.alternativeenergy.inventory.InventoryUtils;
import jkmau5.alternativeenergy.network.PacketElementUpdate;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngContainer extends Container {

    private final IInventory inventory;
    @Getter private List<Element> elements = Lists.newArrayList();

    public AltEngContainer(IInventory inventory) {
        this.inventory = inventory;
    }

    public AltEngContainer() {
        this.inventory = null;
    }

    public void addSlot(Slot slot) {
        this.addSlotToContainer(slot);
    }

    public void addElement(Element element) {
        element.setContainer(this);
        this.elements.add(element);
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        for(Element element : this.elements) {
            element.initElement(crafter);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for(Element element : this.elements) {
            for(ICrafting crafter : (List<ICrafting>) this.crafters) {
                element.updateElement(crafter);
            }
        }
    }

    public void sendElementDataToClient(Element element, ICrafting crafter, byte[] data) {
        PacketDispatcher.sendPacketToPlayer(new PacketElementUpdate(this.windowId, this.elements.indexOf(element), data).getPacket(), (Player) crafter);
    }

    public void handleElementDataClient(int widgetId, DataInput data) throws IOException {
        this.elements.get(widgetId).handleClientPacketData(data);
    }

    @SideOnly(Side.CLIENT)
    public void updateString(byte id, String data) {
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(this.inventory == null) {
            return true;
        }
        return this.inventory.isUseableByPlayer(entityplayer);
    }

    protected void addPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
        Slot slot = slotNum < 0 ? null : (Slot)this.inventorySlots.get(slotNum);
        if(slot instanceof AltEngSlot && ((AltEngSlot) slot).isFakeSlot()) {
            return this.fakeSlotClick((AltEngSlot) slot, mouseButton, modifier, player);
        }
        return super.slotClick(slotNum, mouseButton, modifier, player);
    }

    private ItemStack fakeSlotClick(AltEngSlot slot, int mouseButton, int modifier, EntityPlayer player) {
        ItemStack stack = null;

        if(mouseButton == 2) {
            if(slot.canAdjustFakeSlot()) {
                slot.putStack(null);
            }
        } else if((mouseButton == 0) || (mouseButton == 1)) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();

            if (stackSlot != null) {
                stack = stackSlot.copy();
            }
            if (stackSlot == null) {
                if (stackHeld != null && slot.isItemValid(stackHeld)) {
                    this.fillFakeSlot(slot, stackHeld, mouseButton, modifier);
                }
            } else if(stackHeld == null) {
                adjustFakeSlot(slot, mouseButton, modifier);
                slot.onPickupFromSlot(player, playerInv.getItemStack());
            } else if(slot.isItemValid(stackHeld)) {
                if(InventoryUtils.areItemsEqual(stackSlot, stackHeld)) {
                    this.adjustFakeSlot(slot, mouseButton, modifier);
                } else {
                    this.fillFakeSlot(slot, stackHeld, mouseButton, modifier);
                }
            }
        }
        return stack;
    }

    protected void adjustFakeSlot(AltEngSlot slot, int mouseButton, int modifier) {
        if(!slot.canAdjustFakeSlot()) {
            return;
        }
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (modifier == 1) {
            stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
        }
        if(stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        stackSlot.stackSize = stackSize;
        if (stackSlot.stackSize <= 0) {
            slot.putStack(null);
        }
    }

    protected void fillFakeSlot(AltEngSlot slot, ItemStack stackHeld, int mouseButton, int modifier) {
        if(!slot.canAdjustFakeSlot()) {
            return;
        }
        int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.stackSize = stackSize;
        slot.putStack(phantomStack);
    }

    protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
        boolean changed = false;
        if (stackToShift.isStackable()) {
            for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
                Slot slot = (Slot) this.inventorySlots.get(slotIndex);
                ItemStack stackInSlot = slot.getStack();
                if (stackInSlot != null && InventoryUtils.areItemsEqual(stackInSlot, stackToShift)) {
                    int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                    if (resultingStackSize <= max) {
                        stackToShift.stackSize = 0;
                        stackInSlot.stackSize = resultingStackSize;
                        slot.onSlotChanged();
                        changed = true;
                    } else if(stackInSlot.stackSize < max) {
                        stackToShift.stackSize -= max - stackInSlot.stackSize;
                        stackInSlot.stackSize = max;
                        slot.onSlotChanged();
                        changed = true;
                    }
                }
            }
        }
        if(stackToShift.stackSize > 0) {
            for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
                Slot slot = (Slot) this.inventorySlots.get(slotIndex);
                ItemStack stackInSlot = slot.getStack();
                if(stackInSlot == null) {
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                    stackInSlot = stackToShift.copy();
                    stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
                    stackToShift.stackSize -= stackInSlot.stackSize;
                    slot.putStack(stackInSlot);
                    slot.onSlotChanged();
                    changed = true;
                }
            }
        }
        return changed;
    }

    private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
        for (int machineIndex = 0; machineIndex < numSlots - 36; machineIndex++) {
            Slot slot = (Slot) this.inventorySlots.get(machineIndex);
            if (!(slot instanceof AltEngSlot) || ((AltEngSlot) slot).canShiftClick()) {
                if(!(slot instanceof AltEngSlot) || ((AltEngSlot) slot).isFakeSlot()) {
                    if (slot.isItemValid(stackToShift)) {
                        if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack originalStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);
        int numSlots = this.inventorySlots.size();
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            originalStack = stackInSlot.copy();
            if (slotIndex < numSlots - 36 || !tryShiftItem(stackInSlot, numSlots)) {
                if(slotIndex >= numSlots - 36 && slotIndex < numSlots - 9) {
                    if(!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
                        return null;
                    }
                } else if(slotIndex >= numSlots - 9 && slotIndex < numSlots) {
                    if(!shiftItemStack(stackInSlot, numSlots - 36, numSlots - 9)) {
                        return null;
                    }
                } else if(!shiftItemStack(stackInSlot, numSlots - 36, numSlots)) {
                    return null;
                }
            }
            slot.onSlotChange(stackInSlot, originalStack);
            if (stackInSlot.stackSize <= 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (stackInSlot.stackSize == originalStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, stackInSlot);
        }
        return originalStack;
    }
}
