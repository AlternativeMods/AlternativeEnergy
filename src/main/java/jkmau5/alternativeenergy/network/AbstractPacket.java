package jkmau5.alternativeenergy.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.network.PacketDispatcher;
import jkmau5.alternativeenergy.AltEngLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
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

    private static void registerPacket(int id, Class<? extends AbstractPacket> cl){
        packets.put(id, cl);
    }

    static {
        registerPacket(0, PacketCapacityUpgrade.class);
        registerPacket(1, PacketOutputspeedUpgrade.class);
        registerPacket(2, PacketLinkboxFrequencyUpdate.class);
    }

    public final Packet250CustomPayload getPacket(){
        Packet250CustomPayload ret = null;
        ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(arrayStream);
        try {
            stream.write(this.getID());
            this.writePacket(stream);
            ret = PacketDispatcher.getPacket("AltEng", arrayStream.toByteArray());
        }catch(IOException e) {
            AltEngLog.severe(e, "Error while writing packet data for packet " + this.getID());
        }finally {
            IOUtils.closeQuietly(arrayStream);
            IOUtils.closeQuietly(stream);
        }
        return ret;
    }

    public static AbstractPacket readPacket(Packet250CustomPayload packet){
        AbstractPacket ret = null;
        ByteArrayInputStream arrayStream = null;
        DataInputStream stream = null;
        try{
            arrayStream = new ByteArrayInputStream(packet.data);
            stream = new DataInputStream(arrayStream);

            int packetID = stream.read();
            AbstractPacket nPacket = packets.get(packetID).newInstance();
            if(nPacket != null){
                nPacket.readPacket(stream);
            }
            ret = nPacket;
        }catch (Exception e){
            AltEngLog.severe(e, "Error while reading packet");
        }finally {
            IOUtils.closeQuietly(arrayStream);
            IOUtils.closeQuietly(stream);
        }
        return ret;
    }

    public final int getID(){
        return packets.inverse().get(this.getClass());
    }

    public abstract void writePacket(DataOutput data) throws IOException;
    public abstract void readPacket(DataInput data) throws  IOException;
    public abstract void processPacket(INetworkManager manager, EntityPlayer player);
}
