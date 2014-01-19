package jkmau5.alternativeenergy.network;

import io.netty.buffer.ByteBuf;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import lombok.NoArgsConstructor;

/**
 * No description given
 *
 * @author jk-5
 */
@NoArgsConstructor
public class PacketCapacityUpgrade extends AbstractPacket {

    private TileEntityPowerBox tile;
    private int numUpgrades;

    private int x, y, z;

    public PacketCapacityUpgrade(TileEntityPowerBox tile, int numUpgrades) {
        this.x = tile.field_145851_c;
        this.y = tile.field_145848_d;
        this.z = tile.field_145849_e;
        this.tile = tile;
        this.numUpgrades = numUpgrades;
    }

    @Override
    public void encode(ByteBuf buffer){
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(this.numUpgrades);
    }

    @Override
    public void decode(ByteBuf buffer){
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.numUpgrades = buffer.readInt();

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
