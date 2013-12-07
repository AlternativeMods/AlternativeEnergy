package jkmau5.alternativeenergy.network;

import jkmau5.alternativeenergy.gui.container.AltEngContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketElementUpdate extends AbstractPacket {

    private int windowID;
    private int widgetID;
    private byte[] data;

    public PacketElementUpdate() {} //We need the empty constructor here!
    public PacketElementUpdate(int windowID, int widgetID, byte[] data) {
        this.windowID = windowID;
        this.widgetID = widgetID;
        this.data = data;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.writeInt(this.windowID);
        data.writeInt(this.widgetID);
        data.write(this.data);
    }

    @Override
    public void readPacket(DataInput data) throws IOException {
        this.windowID = data.readInt();
        this.widgetID = data.readInt();
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if(player.openContainer instanceof AltEngContainer && player.openContainer.windowId == this.windowID) {
            ((AltEngContainer) player.openContainer).handleElementDataClient(this.widgetID, data);
        }
    }
}
