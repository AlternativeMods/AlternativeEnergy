package block;

import core.Config;
import core.GUIHandler;
import core.Main;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import item.Items;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import tile.TileEntityPowerBox;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:12
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class BlockPowerBox extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public BlockPowerBox(int par1, Material par2Material) {
        super(par1, par2Material);
        setUnlocalizedName("PowerBox");
        setHardness(5.0F);
        if(Config.unbreakable)
            setResistance(25000.0F);
        else
            setResistance(10.0F);
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
        return new TileEntityPowerBox();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack is) {
        if(is.getTagCompound() != null) {
            if(is.getTagCompound().hasKey("storedPower")) {
                TileEntityPowerBox pBox = new TileEntityPowerBox();

                if(is.getTagCompound().hasKey("capacityUpgrade")) {
                    pBox.capacitySlot.put(new ItemStack(Items.upgrade_Item, is.getTagCompound().getInteger("capacityUpgrade"), 0));

                    for(int i=1; i<=pBox.capacitySlot.get().stackSize; i++) {
                        pBox.maxPowers += i * Config.powerBox_capacity_multiplier;
                    }
                }
                if(is.getTagCompound().hasKey("outputSpeedUpgrade")) {
                    pBox.outputSpeedSlot.put(new ItemStack(Items.upgrade_Item, is.getTagCompound().getInteger("outputSpeedUpgrade"), 1));

                    int tmpOutput = 32 * (4 ^ pBox.outputSpeedSlot.get().stackSize);
                    if(tmpOutput > 512) tmpOutput = 512;

                    pBox.maxOutput = tmpOutput;
                }

                pBox.setPowerStored(is.getTagCompound().getInteger("storedPower"));

                world.setBlockTileEntity(x, y, z, pBox);
            }
        }
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if(world.isRemote)
            return world.setBlockToAir(x, y, z);

        if(world.getBlockTileEntity(x, y, z) != null && world.getBlockTileEntity(x, y, z) instanceof TileEntityPowerBox) {
            TileEntityPowerBox pBox = (TileEntityPowerBox) world.getBlockTileEntity(x, y, z);

            ItemStack is = new ItemStack(this);

            if(is.getTagCompound() == null)
                is.setTagCompound(new NBTTagCompound());

            is.getTagCompound().setInteger("storedPower", pBox.getPowerStored());

            if(pBox.capacitySlot.get() != null)
                is.getTagCompound().setInteger("capacityUpgrade", pBox.capacitySlot.get().stackSize);
            if(pBox.outputSpeedSlot.get() != null)
                is.getTagCompound().setInteger("outputSpeedUpgrade", pBox.outputSpeedSlot.get().stackSize);

            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, is);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);

            dropItemStackInWorld(world, x, y, z, pBox.chargeSlot.get());
            dropItemStackInWorld(world, x, y, z, pBox.dischargeSlot.get());
        }

        return world.setBlockToAir(x, y, z);
    }

    public void dropItemStackInWorld(World world, int x, int y, int z, ItemStack is) {
        if(is == null)
            return;

        float f = 0.7F;
        double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, is);
        entityitem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityitem);
    }

    @Override
    public void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack)
    {
        return;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
            return false;

        TileEntity tempTile = world.getBlockTileEntity(x, y, z);
        if(tempTile == null)
            return false;

        if(tempTile instanceof TileEntityPowerBox) {
            TileEntityPowerBox pBox = (TileEntityPowerBox) tempTile;

            if(player.getHeldItem() == null) {
                if(player.isSneaking()) {
                    player.addChatMessage(pBox.storedPower + " _ " + pBox.getPowerStored());
                }
                else {
                    player.openGui(Main.instance, GUIHandler.ID_GUI_PowerBox, world, x, y, z);
                }
            }
            else if(player.getHeldItem().itemID == Main.bcWrenchId) {
                if(player.isSneaking()) {
                    pBox.setMode(side, pBox.getNextMode(pBox.getMode(side)));
                    player.addChatMessage("Side \"" + ForgeDirection.getOrientation(side).toString() + "\" is now set to " + pBox.getMode(side));
                    return true;
                }
                else {
                    player.addChatMessage("Side \"" + ForgeDirection.getOrientation(side).toString() + "\" is set to " + pBox.getMode(side));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess bAccess, int x, int y, int z, int side) {
        TileEntity tmpTile = bAccess.getBlockTileEntity(x, y, z);
        if(tmpTile != null && tmpTile instanceof TileEntityPowerBox) {
            TileEntityPowerBox pBox = (TileEntityPowerBox) tmpTile;

            if(pBox.getMode(side).equalsIgnoreCase("input")) return icons[0];
            else if(pBox.getMode(side).equalsIgnoreCase("output")) return icons[1];
            else return icons[2];
        }

        return icons[2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        icons = new Icon[3];

        icons[0] = par1IconRegister.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5) + "_Input");
        icons[1] = par1IconRegister.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5) + "_Output");
        icons[2] = par1IconRegister.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5) + "_Disabled");
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return icons[2];
    }
}