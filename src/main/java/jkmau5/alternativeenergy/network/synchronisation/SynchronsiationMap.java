package jkmau5.alternativeenergy.network.synchronisation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import jkmau5.alternativeenergy.AltEngLog;
import jkmau5.alternativeenergy.network.PacketSynchronisation;
import jkmau5.alternativeenergy.util.ByteUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class SynchronsiationMap<T extends ISynchronisationHandler> {

    protected final T handler;

    private Set<Integer> knownUsers = Sets.newHashSet();
    protected ISynchronized[] objects = new ISynchronized[16];
    protected Map<String, Integer> nameMap = Maps.newHashMap();

    private int index = 0;

    protected SynchronsiationMap(T handler) {
        this.handler = handler;
    }

    public void put(String name, ISynchronized value) {
        nameMap.put(name, index);
        objects[index++] = value;
    }

    public ISynchronized get(String name) {
        if (nameMap.containsKey(name)) {
            return objects[nameMap.get(name)];
        }
        return null;
    }

    public int size() {
        return index;
    }

    public List<ISynchronized> readFromStream(DataInput dis) throws IOException {
        short mask = dis.readShort();
        List<ISynchronized> changes = Lists.newArrayList();
        for (int i = 0; i < 16; i++) {
            if (objects[i] != null) {
                if (ByteUtils.get(mask, i)) {
                    objects[i].readFromStream(dis);
                    changes.add(objects[i]);
                    objects[i].resetChangeTimer(getWorld());
                }
            }
        }
        return changes;
    }

    public void writeToStream(DataOutput dos, boolean regardless) throws IOException {
        short mask = 0;
        for (int i = 0; i < 16; i++) {
            mask = ByteUtils.set(mask, i, objects[i] != null && (regardless || objects[i].isDirty()));
        }
        dos.writeShort(mask);
        for (int i = 0; i < 16; i++) {
            if (objects[i] != null && (regardless || objects[i].isDirty())) {
                objects[i].writeToStream(dos, regardless);
                objects[i].resetChangeTimer(getWorld());
            }
        }
    }

    public void markAllAsClean() {
        for (int i = 0; i < 16; i++) {
            if (objects[i] != null) {
                objects[i].markClean();
            }
        }
    }

    protected abstract SynchronisationHandlerType getHandlerType();

    protected abstract Set<EntityPlayer> getPlayersWatching();

    protected abstract World getWorld();

    public boolean sync() {
        Set<EntityPlayer> players = getPlayersWatching();
        boolean sent = false;
        if (!getWorld().isRemote) {
            Packet changePacket = null;
            Packet fullPacket = null;

            boolean hasChanges = hasChanges();
            try {
                Set<Integer> newUsersInRange = Sets.newHashSet();
                for (EntityPlayer player : players) {
                    newUsersInRange.add(player.entityId);
                    if (knownUsers.contains(player.entityId)) {
                        if(hasChanges) {
                            if (changePacket == null) {
                                changePacket = createPacket(false, false);
                            }
                            PacketDispatcher.sendPacketToPlayer(changePacket, (Player) player);
                            sent = true;
                        }
                    } else {
                        if (fullPacket == null) {
                            fullPacket = createPacket(true, false);
                        }
                        PacketDispatcher.sendPacketToPlayer(fullPacket, (Player) player);
                        sent = true;
                    }
                }
                knownUsers = newUsersInRange;
            } catch (IOException e) {
                AltEngLog.warning(e, "IOException during synchronisation");
            }
        } else {
            try {
                PacketDispatcher.sendPacketToServer(createPacket(false, true));
                sent = true;
            } catch(IOException e) {
                e.printStackTrace();
            }
            knownUsers.clear();
        }
        markAllAsClean();
        return sent;
    }

    private boolean hasChanges() {
        for (ISynchronized obj : objects) {
            if (obj != null && obj.isDirty()) {
                return true;
            }
        }

        return false;
    }

    public Packet createPacket(boolean fullPacket, boolean toServer) throws IOException {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeBoolean(toServer);
        if (toServer) {
            int dimension = getWorld().provider.dimensionId;
            output.writeInt(dimension);
        }
        SynchronisationHandlerType type = getHandlerType();
        ByteUtils.writeVarint(output, type.ordinal());
        type.writeHandlerInfo(handler, output);
        writeToStream(output, fullPacket);
        PacketSynchronisation packet = new PacketSynchronisation(output.toByteArray());
        return packet.getPacket();
    }

    public static ISynchronisationHandler findSyncMap(World world, DataInput input) throws IOException {
        int handlerTypeId = ByteUtils.readVarint(input);
        Preconditions.checkPositionIndex(handlerTypeId, SynchronisationHandlerType.TYPES.length, "handler type");
        SynchronisationHandlerType handlerType = SynchronisationHandlerType.TYPES[handlerTypeId];
        return handlerType.findHandler(world, input);
    }
}
