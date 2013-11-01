package Lordmau5.PowerBoxes.core;

import cpw.mods.fml.common.network.IGuiHandler;
import Lordmau5.PowerBoxes.gui.ContainerPowerBox;
import Lordmau5.PowerBoxes.gui.GUI_PowerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Lordmau5.PowerBoxes.tile.TileEntityPowerBox;

/**
 * Author: Lordmau5
 * Date: 23.08.13
 * Time: 13:26
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class GUIHandler implements IGuiHandler {

    public static int ID_GUI_PowerBox = 0;
    public static int ID_GUI_EnderBox = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(ID == ID_GUI_PowerBox)
            if(tileEntity instanceof TileEntityPowerBox)
                return new ContainerPowerBox(player.inventory, (TileEntityPowerBox) tileEntity);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(ID == ID_GUI_PowerBox)
            if(tileEntity instanceof TileEntityPowerBox)
                return new GUI_PowerBox(player.inventory, (TileEntityPowerBox) tileEntity);
        return null;
    }
}