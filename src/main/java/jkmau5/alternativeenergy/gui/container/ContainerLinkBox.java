package jkmau5.alternativeenergy.gui.container;

import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * No description given
 *
 * @author jk-5
 */
public class ContainerLinkBox extends ContainerLockable {

    public boolean canLock = false;

    public ContainerLinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox te) {
        super(inventoryPlayer, te);
        this.addPlayerInventory(inventoryPlayer);
    }
}
