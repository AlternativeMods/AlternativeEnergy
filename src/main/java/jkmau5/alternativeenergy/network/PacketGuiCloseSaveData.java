package jkmau5.alternativeenergy.network;

import jkmau5.alternativeenergy.util.interfaces.IGuiCloseSaveDataHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketGuiCloseSaveData extends AbstractPacket {

    private EntityPlayer sender;
    private IGuiCloseSaveDataHandler handler;
    private byte[] data;

    public PacketGuiCloseSaveData(EntityPlayer sender){
        this.sender = sender;
    }
    public PacketGuiCloseSaveData(IGuiCloseSaveDataHandler handler){
        this.handler = handler;
    }
    public PacketGuiCloseSaveData(IGuiCloseSaveDataHandler handler, byte[] data){
        this.handler = handler;
        this.data = data;
    }

    @Override
    public void writePacket(DataOutput data) throws IOException {
        data.writeInt(this.handler.getWorld().provider.dimensionId);
        if(this.handler instanceof TileEntity){
            TileEntity tileEntity = (TileEntity) this.handler;
            data.writeBoolean(true);
            data.writeInt(tileEntity.xCoord);
            data.writeInt(tileEntity.yCoord);
            data.writeInt(tileEntity.zCoord);
        }else if(this.handler instanceof Entity){
            Entity entity = (Entity) this.handler;
            data.writeBoolean(false);
            data.writeInt(entity.entityId);
        }else return;
        this.handler.writeGuiCloseData(data);
        if(this.data != null) data.write(this.data);
    }

    @Override
    public void readPacket(DataInput data) throws IOException {
        World world = DimensionManager.getWorld(data.readInt());
        boolean isTileEntity = data.readBoolean();
        if(isTileEntity){
            int x = data.readInt();
            int y = data.readInt();
            int z = data.readInt();
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof IGuiCloseSaveDataHandler){
                ((IGuiCloseSaveDataHandler) tileEntity).readGuiCloseData(data, this.sender);
            }
        }else{
            Entity entity = world.getEntityByID(data.readInt());
            if(entity instanceof IGuiCloseSaveDataHandler){
                ((IGuiCloseSaveDataHandler) entity).readGuiCloseData(data, this.sender);
            }
        }
    }
}
