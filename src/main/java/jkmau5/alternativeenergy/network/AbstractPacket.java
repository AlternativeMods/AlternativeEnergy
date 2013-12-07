package jkmau5.alternativeenergy.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.PacketDispatcher;
import jkmau5.alternativeenergy.AltEngLog;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AbstractPacket {

    private static BiMap<Integer, Class<? extends AbstractPacket>> packets = HashBiMap.create();
    @Getter(AccessLevel.PROTECTED) private EntityPlayer sender;

    private static void registerPacket(int id, Class<? extends AbstractPacket> cl) {
        packets.put(id, cl);
    }

    static {
        registerPacket(0, PacketCapacityUpgrade.class);
        registerPacket(1, PacketOutputspeedUpgrade.class);
        registerPacket(2, PacketLinkboxFrequencyUpdate.class);
        registerPacket(3, PacketLinkboxFrequencyServerUpdate.class);
        registerPacket(4, PacketLinkboxPrivateUpdate.class);
        registerPacket(5, PacketElementUpdate.class);
        registerPacket(6, PacketSynchronisation.class);
        registerPacket(7, PacketGuiString.class);
        registerPacket(8, PacketGuiCloseSaveData.class);
    }

    public final Packet250CustomPayload getPacket() {
        Packet250CustomPayload ret = null;
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        try {
            output.write(this.getID());
            this.writePacket(output);
            ret = PacketDispatcher.getPacket("AltEng", output.toByteArray());
        } catch(IOException e) {
            AltEngLog.severe(e, "Error while writing packet data for packet " + this.getID());
        }
        return ret;
    }

    public static AbstractPacket readPacket(Packet250CustomPayload packet, EntityPlayer player) {
        AbstractPacket ret = null;
        ByteArrayInputStream arrayStream = null;
        DataInputStream stream = null;
        try {
            arrayStream = new ByteArrayInputStream(packet.data);
            stream = new DataInputStream(arrayStream);

            int packetID = stream.read();
            AbstractPacket nPacket = packets.get(packetID).newInstance();
            nPacket.sender = player;
            if(nPacket != null) {
                nPacket.readPacket(stream);
            }
            ret = nPacket;
        } catch (Exception e) {
            AltEngLog.severe(e, "Error while reading packet");
        } finally {
            IOUtils.closeQuietly(arrayStream);
            IOUtils.closeQuietly(stream);
        }
        return ret;
    }

    public final int getID() {
        return packets.inverse().get(this.getClass());
    }

    public abstract void writePacket(DataOutput data) throws IOException;
    public abstract void readPacket(DataInput data) throws  IOException;
}
