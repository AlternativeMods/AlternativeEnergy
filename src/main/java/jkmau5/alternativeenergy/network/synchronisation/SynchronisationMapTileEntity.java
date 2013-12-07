package jkmau5.alternativeenergy.network.synchronisation;

import com.google.common.collect.ImmutableSet;
import jkmau5.alternativeenergy.util.Utils;
import jkmau5.alternativeenergy.util.interfaces.ISaveNBT;
import jkmau5.alternativeenergy.world.tileentity.AltEngTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Map;
import java.util.Set;

/**
 * No description given
 *
 * @author jk-5
 */
public class SynchronisationMapTileEntity<T extends AltEngTileEntity & ISynchronisationHandler> extends SynchronsiationMap<T> implements ISaveNBT {

    public SynchronisationMapTileEntity(T handler) {
        super(handler);
    }

    @Override
    protected SynchronisationHandlerType getHandlerType() {
        return SynchronisationHandlerType.TILEENTITY;
    }

    @Override
    protected Set<EntityPlayer> getPlayersWatching() {
        if(handler.worldObj instanceof WorldServer) {
            return Utils.getPlayersWatchingBlock((WorldServer) handler.worldObj, handler.xCoord, handler.zCoord);
        }
        return ImmutableSet.of();
    }

    @Override
    protected World getWorld() {
        return this.handler.worldObj;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
            int index = entry.getValue();
            String name = entry.getKey();
            if (objects[index] != null) {
                objects[index].readFromNBT(tag, name);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
            int index = entry.getValue();
            String name = entry.getKey();
            if (objects[index] != null) {
                objects[index].writeToNBT(tag, name);
            }
        }
    }
}
