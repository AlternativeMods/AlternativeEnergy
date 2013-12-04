package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import ic2.api.tile.IWrenchable;
import jkmau5.alternativeenergy.AltEngCompat;
import jkmau5.alternativeenergy.AltEngSupport;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.power.Ratios;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerStorage;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;
import java.util.Random;

/**
 * Author: Lordmau5
 * Date: 04.12.13
 * Time: 16:29
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
@Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "IC2")
public class ItemAlternativeWrench extends AltEngItem implements ISpecialElectricItem {

    public static int maxStoredPower = 15000;

    protected ItemAlternativeWrench() {
        super("alternativeWrench");
        setCreativeTab(AlternativeEnergy.getInstance().getCreativeTab());
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iR)
    {
        itemIcon = iR.registerIcon(Constants.TEXTURE_DOMAIN + ":" + this.getUnlocalizedName().substring(12));
    }

    public Icon getIconFromDamage(int par1)
    {
        return itemIcon;
    }

    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean par4) {
        AltEngSupport.initiateNBTTag(is);
        int storedPower = AltEngSupport.initiateOrGetNBTInteger(is, "storedPower");

        list.add("Stored Power: " + storedPower + " PBu / " + maxStoredPower + " PBu");

        if(GuiScreen.isShiftKeyDown()){

        }
        else {
            list.add("Hold shift to get more information");
        }
    }

    protected boolean canWrench(ItemStack itemStack) {
        return AltEngSupport.initiateOrGetNBTInteger(itemStack, "storedPower") >= 250;
    }

    protected void drainWrenchPower(ItemStack itemStack) {
        int storedPower = AltEngSupport.initiateOrGetNBTInteger(itemStack, "storedPower");
        AltEngSupport.setNBTInteger(itemStack, "storedPower", storedPower - 250);
    }

    //------------------------------------------------------------------------------

    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(!canWrench(itemStack))
            return false;

        int blockId = world.getBlockId(x, y, z);
        Block block = Block.blocksList[blockId];

        if (block == null) return false;

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if(AltEngCompat.hasIC2) {
            if(tileEntity instanceof IWrenchable) {
                return handleIC2Wrenching(world, player, itemStack, (IWrenchable) tileEntity, x, y, z, side, block);
            }
        }
        if(tileEntity instanceof TileEntityPowerStorage) {
            if(!world.isRemote)
                if(((TileEntityPowerStorage)tileEntity).setNextMode(player, ForgeDirection.getOrientation(side)))
                    drainWrenchPower(itemStack);
            return !world.isRemote;
        }

        if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
            if(!world.isRemote)
                drainWrenchPower(itemStack);
            return !world.isRemote;
        }

        return false;
    }

    public static void dropItemStackAsEntity(World world, int x, int y, int z, ItemStack itemStack)
    {
        if (itemStack == null) return;

        Random random = new Random();

        double f = 0.7D;
        double dx = random.nextFloat() * f + (1.0D - f) * 0.5D;
        double dy = random.nextFloat() * f + (1.0D - f) * 0.5D;
        double dz = random.nextFloat() * f + (1.0D - f) * 0.5D;

        EntityItem entityItem = new EntityItem(world, x + dx, y + dy, z + dz, itemStack.copy());
        entityItem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityItem);
    }

    @Optional.Method(modid = "IC2")
    protected boolean handleIC2Wrenching(World world, EntityPlayer player, ItemStack itemStack, IWrenchable iWrenchable, int x, int y, int z, int side, Block block) {
        if(player.isSneaking()) {
            side += side % 2 * -2 + 1;
        }

        if (iWrenchable.wrenchCanSetFacing(player, side)) {
            if(!world.isRemote) {
                iWrenchable.setFacing((short) side);
                drainWrenchPower(itemStack);
            }

            return !world.isRemote;
        }

        int metaData = world.getBlockMetadata(x, y, z);

        if(iWrenchable.wrenchCanRemove(player)) {
            if(!world.isRemote) {
                List<ItemStack> drops = block.getBlockDropped(world, x, y, z, metaData, 0);

                ItemStack wrenchDrop = iWrenchable.getWrenchDrop(player);
                if (wrenchDrop != null) {
                    if (drops.isEmpty())
                        drops.add(wrenchDrop);
                    else {
                        drops.set(0, wrenchDrop);
                    }
                }

                for (ItemStack itemStackDrops : drops) {
                    dropItemStackAsEntity(world, x, y, z, itemStackDrops);
                }

                world.setBlock(x, y, z, 0, 0, 3);
                drainWrenchPower(itemStack);
            }

            return !world.isRemote;
        }

        return false;
    }

    public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
    {
        return true;
    }

    //---------------------------------------------------------------------------------------------------------

    @Optional.Method(modid = "IC2")
    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return AltEngCompat.alternativeElectricItemManager;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getChargedItemId(ItemStack itemStack) {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getEmptyItemId(ItemStack itemStack) {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getMaxCharge(ItemStack itemStack) {
        return (int) Math.ceil(maxStoredPower / Ratios.EU.conversion);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getTier(ItemStack itemStack) {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getTransferLimit(ItemStack itemStack) {
        return 0;
    }
}