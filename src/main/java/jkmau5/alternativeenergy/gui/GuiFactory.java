package jkmau5.alternativeenergy.gui;

import jkmau5.alternativeenergy.client.gui.GuiLinkBox;
import jkmau5.alternativeenergy.client.gui.GuiPowerBox;
import jkmau5.alternativeenergy.gui.container.ContainerLinkBox;
import jkmau5.alternativeenergy.gui.container.ContainerPowerBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public class GuiFactory {

    public static GuiScreen createGui(EnumGui gui, InventoryPlayer inventory, Object owner, World world, int x, int y, int z) {
        if(owner == null) {
            return null;
        }
        switch (gui.ordinal()) {
            case 0:
                return new GuiPowerBox(inventory, (TileEntityPowerBox) owner);
            case 1:
                return new GuiLinkBox(inventory, (TileEntityLinkBox) owner);
        }
        return null;
    }

    public static Container createContainer(EnumGui gui, InventoryPlayer inventory, Object owner, World world, int x, int y, int z) {
        if(owner == null) {
            return null;
        }
        switch (gui.ordinal()) {
            case 0:
                return new ContainerPowerBox(inventory, (TileEntityPowerBox) owner);
            case 1:
                return new ContainerLinkBox(inventory, (TileEntityLinkBox) owner);
        }
        return null;
    }
}
