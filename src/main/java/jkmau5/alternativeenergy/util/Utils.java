package jkmau5.alternativeenergy.util;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.WorldServer;

import java.util.Set;

/**
 * No description given
 *
 * @author jk-5
 */
public class Utils {

    public static Set<EntityPlayer> getPlayersWatchingChunk(WorldServer world, int chunkX, int chunkZ) {
        PlayerManager manager = world.getPlayerManager();

        Set<EntityPlayer> playerList = Sets.newHashSet();
        for (Object o : world.playerEntities) {
            EntityPlayerMP player = (EntityPlayerMP)o;
            if (manager.isPlayerWatchingChunk(player, chunkX, chunkZ)) playerList.add(player);
        }
        return playerList;
    }

    public static Set<EntityPlayer> getPlayersWatchingBlock(WorldServer world, int blockX, int blockZ) {
        return getPlayersWatchingChunk(world, blockX >> 4, blockZ >> 4);
    }
}
