package jkmau5.alternativeenergy.client;

import jkmau5.alternativeenergy.client.gui.GUI_LinkBox;
import jkmau5.alternativeenergy.client.gui.GUI_PowerBox;
import jkmau5.alternativeenergy.server.GuiHandlerServer;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public class GuiHandlerClient extends GuiHandlerServer {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(ID == ID_GUI_PowerBox)
            if(tileEntity instanceof TileEntityPowerBox)
                return new GUI_PowerBox(player.inventory, (TileEntityPowerBox) tileEntity);
        if(ID == ID_GUI_LinkBox)
            if(tileEntity instanceof TileEntityLinkBox)
                return new GUI_LinkBox(player.inventory, (TileEntityLinkBox) tileEntity);
        return null;
    }
}
