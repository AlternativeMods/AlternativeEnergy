package block;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import core.EnergyNetwork;
import core.Main;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tile.TileEntityPowerCable;

import java.util.List;

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

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        setCableBoundingBox(world, i, j, k);
    }

    public void setCableBoundingBox(IBlockAccess bAccess, int x, int y, int z)
    {
        TileEntity xTile = null;
        TileEntityPowerCable thisTile = (TileEntityPowerCable) bAccess.getBlockTileEntity(x, y, z);

        float minX = 0.335F;
        float minY = 0.335F;
        float minZ = 0.335F;

        float maxX = 0.665F;
        float maxY = 0.665F;
        float maxZ = 0.665F;

        xTile = bAccess.getBlockTileEntity(x - 1, y, z);

        xTile = bAccess.getBlockTileEntity(x - 1, y, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter)
                minX = 0.0F;
        }

        xTile = bAccess.getBlockTileEntity(x + 1, y, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter)
                maxX = 1.0F;
        }

        xTile = bAccess.getBlockTileEntity(x, y, z - 1);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter)
                minZ = 0.0F;
        }

        xTile = bAccess.getBlockTileEntity(x, y, z + 1);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter)
                maxZ = 1.0F;
        }

        xTile = bAccess.getBlockTileEntity(x, y - 1, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter)
                minY = 0.0F;
        }

        xTile = bAccess.getBlockTileEntity(x, y + 1, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter)
                maxY = 1.0F;
        }

        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        setCableBoundingBox(world, x, y, z);
        AxisAlignedBB aabb = super.getCollisionBoundingBoxFromPool(world, x, y, z);
        if ((aabb != null) && (axisAlignedBB.intersectsWith(aabb)))
        {
            list.add(aabb);
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
}