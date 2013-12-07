package jkmau5.alternativeenergy.world.blocks;

import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.util.Utils;
import jkmau5.alternativeenergy.util.config.ConfigTag;
import jkmau5.alternativeenergy.world.tileentity.AltEngTileEntity;
import lombok.Getter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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

    @Getter
    private final ConfigTag blockConfig;

    public BlockTileEntity(String unlocalizedName, Material material) {
        super(AlternativeEnergy.getInstance().getConfig().getTag("blocks").useBraces().getTag(unlocalizedName).useBraces().getTag("blockid").getIntValue(Utils.getFreeBlockID()), material);
        this.blockConfig = AlternativeEnergy.getInstance().getConfig().getTag("blocks").useBraces().getTag(unlocalizedName).useBraces();
        this.setUnlocalizedName("altEng." + unlocalizedName);
        this.setTextureName(Constants.TEXTURE_DOMAIN + ":" + unlocalizedName);
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
        if(world.isRemote) {
            return;
        }
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof AltEngTileEntity) {
            if(entity != null && entity instanceof EntityPlayer) {
                ((AltEngTileEntity) tile).setOwner(((EntityPlayer) entity).username);
            }
            ((AltEngTileEntity) tile).constructFromItemStack(itemStack, entity);
        }
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof AltEngTileEntity) {
            return ((AltEngTileEntity) tile).removeBlockByPlayer(player);
        }
        return world.setBlockToAir(x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        return tile instanceof AltEngTileEntity && ((AltEngTileEntity) tile).blockActivated(player, side);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
        return AlternativeEnergy.getInstance().getCreativeTab();
    }
}
