package jkmau5.alternativeenergy.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import jkmau5.alternativeenergy.AlternativeEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public class GuiHandler implements IGuiHandler {

    public static void openGui(EnumGui gui, EntityPlayer player, World world, int x, int y, int z){
        if(!world.isRemote) {
            if(gui.hasContainer()){
                player.openGui(AlternativeEnergy.getInstance(), gui.getId(), world, x, y, z);
            }
        }else if (!gui.hasContainer()) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            FMLClientHandler.instance().displayGuiScreen(player, GuiFactory.createGui(gui, player.inventory, tile, world, x, y, z));
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        return GuiFactory.createContainer(EnumGui.fromId(ID), player.inventory, tileEntity, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        return GuiFactory.createGui(EnumGui.fromId(ID), player.inventory, tileEntity, world, x, y, z);
    }
}
