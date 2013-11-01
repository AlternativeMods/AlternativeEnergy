package core;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import item.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tile.TileEntityPowerBox;

import java.io.*;

/**
 * Author: Lordmau5
 * Date: 23.08.13
 * Time: 13:48
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class PacketHandler implements IPacketHandler {

    public static int CAPACITY_UPGRADE = 0;
    public static int OUTPUTSPEED_UPGRADE = 1;

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if(!packet.channel.equals(Main.channelName))
            return;

        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        int ID;

        int x;
        int y;
        int z;

        float var;

        World world = ((EntityPlayer)player).worldObj;

        try {
            ID = inputStream.readInt();

            x = inputStream.readInt();
            y = inputStream.readInt();
            z = inputStream.readInt();

            var = inputStream.readFloat();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        TileEntity tmpTile = world.getBlockTileEntity(x, y, z);
        if(tmpTile != null && tmpTile instanceof TileEntityPowerBox) {
            TileEntityPowerBox pBox = (TileEntityPowerBox) tmpTile;

            if(ID == CAPACITY_UPGRADE) {
                if(var == 0)
                    pBox.capacitySlot.put(null);
                else
                    pBox.capacitySlot.put(new ItemStack(Items.upgrade_Item, (int) var, 0));
                pBox.forceMaxPowersUpdate();
            }

            else if(ID == OUTPUTSPEED_UPGRADE) {
                if(var == 0)
                    pBox.outputSpeedSlot.put(null);
                else
                    pBox.outputSpeedSlot.put(new ItemStack(Items.upgrade_Item, (int) var, 1));
                pBox.forceOutputSpeedUpdate();
            }
        }
    }

    public static void sendPacketToPlayer(Player player, int ID, int x, int y, int z, float var) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeInt(ID);

            outputStream.writeInt(x);
            outputStream.writeInt(y);
            outputStream.writeInt(z);

            outputStream.writeFloat(var);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Main.channelName;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        PacketDispatcher.sendPacketToPlayer(packet, player);
    }

    public static void sendPacketToPlayers(int ID, int x, int y, int z, float var) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeInt(ID);

            outputStream.writeInt(x);
            outputStream.writeInt(y);
            outputStream.writeInt(z);

            outputStream.writeFloat(var);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Main.channelName;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        PacketDispatcher.sendPacketToAllPlayers(packet);
    }

    public static void sendSpecialPacketToPlayers(int ID, int x, int y, int z, float var, float ... array) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeInt(ID);

            outputStream.writeInt(x);
            outputStream.writeInt(y);
            outputStream.writeInt(z);

            outputStream.writeFloat(var);

            outputStream.writeInt(array.length);

            for(int i=0; i<array.length; i++)
                outputStream.writeFloat(array[i]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Main.channelName;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        PacketDispatcher.sendPacketToAllPlayers(packet);
    }
}