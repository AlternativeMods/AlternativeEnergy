package jkmau5.alternativeenergy.network;

import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Sent from client to the server when a player updates the frequency in the linkbox
 *
 * @author jk-5
 */
public class PacketLinkboxFrequencyUpdate extends AbstractPacket {

    private int linkID;
    private int x, y, z;

    public PacketLinkboxFrequencyUpdate() {} //We need the empty constructor here!
    public PacketLinkboxFrequencyUpdate(TileEntityLinkBox tile, int linkID) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.linkID = linkID;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        data.writeInt(this.linkID);
    }

    @Override
    public void readPacket(DataInput data) throws IOException {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();

        TileEntity tile = this.getSender().worldObj.getBlockTileEntity(this.x, this.y, this.z);
        if(tile == null || !(tile instanceof TileEntityLinkBox)) {
            return;
        }
        ((TileEntityLinkBox) tile).setLinkId(this.linkID);
    }
}
