package jkmau5.alternativeenergy.network;

import cpw.mods.fml.client.FMLClientHandler;
import jkmau5.alternativeenergy.gui.container.AltEngContainer;
import net.minecraft.client.entity.EntityClientPlayerMP;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketGuiString extends AbstractPacket {

    private byte windowId;
    private byte dataId;
    private String str;

    public PacketGuiString() {}
    public PacketGuiString(int windowId, int dataId, String str) {
        this.windowId = ((byte)windowId);
        this.dataId = ((byte)dataId);
        this.str = str;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.writeByte(this.windowId);
        data.writeByte(this.dataId);
        data.writeUTF(this.str);
    }

    @Override
    public void readPacket(DataInput data) throws IOException {
        this.windowId = data.readByte();
        this.dataId = data.readByte();
        this.str = data.readUTF();

        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
        if(player.openContainer instanceof AltEngContainer && player.openContainer.windowId == this.windowId) {
            ((AltEngContainer) player.openContainer).updateString(this.dataId, this.str);
        }
    }
}
