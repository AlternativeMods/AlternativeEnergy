package lordmau5.powerboxes.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import lordmau5.powerboxes.client.gui.GUI_LinkBox;
import lordmau5.powerboxes.client.gui.GUI_PowerBox;
import lordmau5.powerboxes.inventory.container.ContainerLinkBox;
import lordmau5.powerboxes.inventory.container.ContainerPowerBox;
import lordmau5.powerboxes.world.tileentity.TileEntityLinkBox;
import lordmau5.powerboxes.world.tileentity.TileEntityPowerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Author: Lordmau5
 * Date: 28.10.13
 * Time: 10:57
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class CommonProxy implements IGuiHandler {

    public static int ID_GUI_PowerBox = 0;
    public static int ID_GUI_LinkBox = 1;

    public void initRendering(){

    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(ID == ID_GUI_PowerBox)
            if(tileEntity instanceof TileEntityPowerBox)
                return new ContainerPowerBox(player.inventory, (TileEntityPowerBox) tileEntity);
        if(ID == ID_GUI_LinkBox)
            if(tileEntity instanceof TileEntityLinkBox)
                return new ContainerLinkBox(player.inventory);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}