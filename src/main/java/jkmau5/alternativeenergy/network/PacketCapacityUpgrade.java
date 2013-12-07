package jkmau5.alternativeenergy.network;

import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketCapacityUpgrade extends AbstractPacket {

    private TileEntityPowerBox tile;
    private int numUpgrades;

    private int x, y, z;

    public PacketCapacityUpgrade(EntityPlayer sender) {} //We need the empty constructor here!
    public PacketCapacityUpgrade(TileEntityPowerBox tile, int numUpgrades) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.tile = tile;
        this.numUpgrades = numUpgrades;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        data.writeInt(this.numUpgrades);
    }

    @Override
    public void readPacket(DataInput data) throws IOException {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();

        /*TileEntity tile = this.getSender().worldObj.getBlockTileEntity(this.x, this.y, this.z);
        if(tile == null || !(tile instanceof TileEntityPowerBox)) return;
        TileEntityPowerBox powerBox = (TileEntityPowerBox) tile;
        if(this.numUpgrades == 0){
            powerBox.capacitySlot.put(null);
        }else{
            powerBox.capacitySlot.put(new ItemStack(AltEngItems.itemUpgrade, this.numUpgrades, 0));
        }
        powerBox.updateMaxStorage();*/
    }
}
