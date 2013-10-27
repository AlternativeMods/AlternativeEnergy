package block;

import core.EnergyNetwork;
import core.Main;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tile.TileEntityPowerCable;

/**
 * Author: Lordmau5
 * Date: 27.10.13
 * Time: 20:56
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class BlockPowerCable extends BlockContainer {

    public BlockPowerCable(int par1, Material par2Material) {
        super(par1, par2Material);
        setUnlocalizedName("PowerBox");
        setHardness(5.0F);
        setStepSound(soundMetalFootstep);
        setCreativeTab(Main.tabPowerBox);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return new TileEntityPowerCable();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack is) {
        if(world.isRemote)
            return;

        TileEntityPowerCable pCable = (TileEntityPowerCable) world.getBlockTileEntity(x, y, z);

        if(pCable != null)
            pCable.onNeighborChange();
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if(world.isRemote)
            return world.setBlockToAir(x, y, z);

        TileEntityPowerCable tile = (TileEntityPowerCable) world.getBlockTileEntity(x, y, z);
        if(tile != null)
            tile.getEnergyNetwork().recalculateNetworks(tile);

        return world.setBlockToAir(x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote)
            return false;

        TileEntity tempTile = world.getBlockTileEntity(x, y, z);
        if(tempTile == null)
            return false;

        if(tempTile instanceof TileEntityPowerCable) {
            TileEntityPowerCable pCable = (TileEntityPowerCable) tempTile;

            EnergyNetwork network = pCable.getEnergyNetwork();

            if(player.isSneaking())
                player.addChatMessage(network.toString());
            else
                player.addChatMessage("Energy: " + network.networkPower + " / " + network.maxNetworkPower);
        }
        return false;
    }
}