package ic2.alternativeenergy.proxy;

import ic2.alternativeenergy.inventory.container.ContainerPowerBox;
import cpw.mods.fml.common.network.IGuiHandler;
import ic2.alternativeenergy.inventory.container.ContainerLinkBox;
import ic2.alternativeenergy.world.tileentity.TileEntityLinkBox;
import ic2.alternativeenergy.world.tileentity.TileEntityPowerBox;
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