package jkmau5.alternativeenergy.network;

import jkmau5.alternativeenergy.network.synchronisation.ISynchronisationHandler;
import jkmau5.alternativeenergy.network.synchronisation.ISynchronized;
import jkmau5.alternativeenergy.network.synchronisation.SynchronsiationMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketSynchronisation extends AbstractPacket {

    private byte[] data;

    public PacketSynchronisation() {} //We need the empty constructor here!
    public PacketSynchronisation(byte[] data) {
        this.data = data;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.write(this.data);
    }

    @Override
    public void readPacket(DataInput data) throws IOException {
        boolean toServer = data.readBoolean();
        World world;
        if(toServer) {
            world = DimensionManager.getWorld(data.readInt());
        } else {
            world = Minecraft.getMinecraft().theWorld;
        }

        ISynchronisationHandler handler = SynchronsiationMap.findSyncMap(world, data);
        if (handler != null) {
            List<ISynchronized> changes = handler.getSyncMap().readFromStream(data);
            handler.onSynced(changes);
        }
    }
}
