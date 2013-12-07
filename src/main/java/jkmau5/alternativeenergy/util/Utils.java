package jkmau5.alternativeenergy.util;

import com.google.common.collect.Sets;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import jkmau5.alternativeenergy.util.interfaces.IOwnable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.WorldServer;

import java.util.Set;

/**
 * No description given
 *
 * @author jk-5
 */
public class Utils {

    private static final int BLOCKID_START = 2000;

    public static Set<EntityPlayer> getPlayersWatchingChunk(WorldServer world, int chunkX, int chunkZ) {
        PlayerManager manager = world.getPlayerManager();

        Set<EntityPlayer> playerList = Sets.newHashSet();
        for (Object o : world.playerEntities) {
            EntityPlayerMP player = (EntityPlayerMP)o;
            if (manager.isPlayerWatchingChunk(player, chunkX, chunkZ)) {
                playerList.add(player);
            }
        }
        return playerList;
    }

    public static Set<EntityPlayer> getPlayersWatchingBlock(WorldServer world, int blockX, int blockZ) {
        return getPlayersWatchingChunk(world, blockX >> 4, blockZ >> 4);
    }

    public static boolean isOwnerOrOp(IOwnable owner, String accessor) {
        if(owner == null || owner.getOwner() == null || accessor == null) {
            return false;
        }
        if(owner.getOwner().equals(accessor)) {
            return true;
        }
        return isPlayerOp(accessor);
    }

    public static boolean isPlayerOp(String username) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            throw new RuntimeException("Don\'t call me on the client!");
        }
        return FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(username);
    }

    public static int getFreeBlockID() {
        for(int id = BLOCKID_START; id < 4096; id++)
            if(Block.blocksList[id] == null) {
                return id;
            }
        for(int id = 256; id < BLOCKID_START; id++)
            if(Block.blocksList[id] == null) {
                return id;
            }
        throw new RuntimeException("No blockIDs are available!");
    }

    public static int getFreeItemID() {
        for(int id = 4097; id < Item.itemsList.length; id++)
            if(Item.itemsList[id + 256] == null) {
                return id;
            }
        throw new RuntimeException("No itemIDs are available!");
    }
}
