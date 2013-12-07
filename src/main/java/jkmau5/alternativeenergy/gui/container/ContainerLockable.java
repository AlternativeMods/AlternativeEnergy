package jkmau5.alternativeenergy.gui.container;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.network.PacketGuiString;
import jkmau5.alternativeenergy.util.Utils;
import jkmau5.alternativeenergy.util.interfaces.ILockable;
import lombok.Getter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class ContainerLockable extends AltEngContainer {

    private final ILockable lockable;
    private final InventoryPlayer playerInv;
    @Getter private boolean canLock = false;
    private int lastLockState;
    @Getter private String owner = "[Unknown]";

    protected ContainerLockable(InventoryPlayer player, ILockable lockable) {
        super((IInventory) lockable);
        this.lockable = lockable;
        this.playerInv = player;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);

        crafter.sendProgressBarUpdate(this, 0, this.lockable.getLockController().getCurrentState());

        this.canLock = Utils.isOwnerOrOp(this.lockable, this.playerInv.player.username);
        crafter.sendProgressBarUpdate(this, 1, this.canLock ? 1 : 0);

        PacketDispatcher.sendPacketToPlayer(new PacketGuiString(this.windowId, 0, this.lockable.getOwner()).getPacket(), (Player) crafter);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.crafters.size(); i++) {
            ICrafting crafter = (ICrafting) this.crafters.get(i);

            int lockState = this.lockable.getLockController().getCurrentState();
            if(this.lastLockState != lockState) {
                crafter.sendProgressBarUpdate(this, 0, lockState);
            }
        }

        this.lastLockState = this.lockable.getLockController().getCurrentState();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        if(id == 0) {
            this.lockable.getLockController().setCurrentState(data);
        } else if(id == 1) {
            this.canLock = data == 1;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateString(byte id, String data) {
        if(id == 0) {
            this.owner = data;
        }
    }
}
