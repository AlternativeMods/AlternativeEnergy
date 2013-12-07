package jkmau5.alternativeenergy.compat.waila;

import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerStorage;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lordmau5
 * Date: 07.12.13
 * Time: 22:52
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class WailaProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        currenttip = new ArrayList<String>();
        if(accessor.getTileEntity() instanceof TileEntityPowerStorage) {
            TileEntityPowerStorage storage = (TileEntityPowerStorage) accessor.getTileEntity();
            currenttip.add("Storage: " + storage.getPowerStored() + "/" + storage.getMaxStoredPower() + " PBu");
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public static void callbackRegister(IWailaRegistrar register){
        register.registerBodyProvider(new WailaProvider(), TileEntityPowerStorage.class);
    }

}