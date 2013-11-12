package jkmau5.alternativeenergy.world.blocks;

import jkmau5.alternativeenergy.world.tileentity.AltEngTileEntity;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerStorage;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class BlockTileEntity extends BlockContainer {

    public BlockTileEntity(int id, Material material){
        super(id, material);
    }

    /**
     * This method is final now. Use the metadata-sensitive one.
     *
     * @param world the world the TileEntity is in
     * @return null
     */
    @Override
    public final TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    public abstract TileEntity createTileEntity(World world, int metadata);

    @Override
    public abstract boolean hasTileEntity(int metadata);

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        if(world.isRemote) return;
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof AltEngTileEntity){
            ((AltEngTileEntity) tile).constructFromItemStack(itemStack, entity);
            if(entity != null && entity instanceof EntityPlayer){
                ((AltEngTileEntity) tile).setOwner(((EntityPlayer) entity).username);
            }
        }
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof TileEntityPowerStorage){
            return ((TileEntityPowerStorage) tile).removeBlockByPlayer(player);
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile instanceof AltEngTileEntity){
            return ((AltEngTileEntity) tile).blockActivated(player, side);
        }else return false;
    }
}
