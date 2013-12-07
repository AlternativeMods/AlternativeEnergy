package jkmau5.alternativeenergy.network;

import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Author: Lordmau5
 * Date: 08.11.13
 * Time: 22:38
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class PacketLinkboxPrivateUpdate extends AbstractPacket {

    private int x, y, z;

    public PacketLinkboxPrivateUpdate() {} //We need the empty constructor here!
    public PacketLinkboxPrivateUpdate(TileEntityLinkBox tile) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
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
        TileEntityLinkBox linkBox = (TileEntityLinkBox) tile;
        linkBox.togglePrivate();
    }
}
