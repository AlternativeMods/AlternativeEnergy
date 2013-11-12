package jkmau5.alternativeenergy.gui.container;

import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * No description given
 *
 * @author jk-5
 */
public class ContainerLinkBox extends AltEngContainer {

    public ContainerLinkBox(InventoryPlayer inventoryPlayer, TileEntityLinkBox te) {
        super(te);
        this.addPlayerInventory(inventoryPlayer);
    }
}
