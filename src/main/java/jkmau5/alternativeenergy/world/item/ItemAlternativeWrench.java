package jkmau5.alternativeenergy.world.item;

import carpentersblocks.api.ICarpentersHammer;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import ic2.api.tile.IWrenchable;
import jds.bibliocraft.tileentities.*;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
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
@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "IC2"),
        @Optional.Interface(iface = "carpentersblocks.api.ICarpentersHammer", modid = "CarpentersBlocks")
})
public class ItemAlternativeWrench extends AltEngItem implements ISpecialElectricItem, ICarpentersHammer {

    public static int maxStoredPower = 30000;

    protected ItemAlternativeWrench() {
        super("alternativeWrench", new Object[] {"I I", " D ", " I ", 'I', Item.ingotIron, 'D', Item.diamond});
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

        if(AltEngCompat.hasBiblioCraft && !world.isRemote)
            if(tryAnythingWithBiblioCraft(player, world, x, y, z, side, tileEntity))
                drainWrenchPower(itemStack);

        if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
            if(!world.isRemote)
                drainWrenchPower(itemStack);
            return !world.isRemote;
        }

        return false;
    }

    @Optional.Method(modid = "BiblioCraft")
    public static boolean tryAnythingWithBiblioCraft(EntityPlayer player, World world, int x, int y, int z, int side, TileEntity tile) {
        boolean returnValue = false;

        if (player.isSneaking())
        {
            if ((tile instanceof TileEntitySeat))
            {
                if (side == 1)
                {
                    returnValue = BiblioCraft_Compatibility.connectChairs(player, world, x, y, z);
                }
                else
                {
                    returnValue = BiblioCraft_Compatibility.rotateSeat(tile);
                }
            }
            if ((tile instanceof TileEntityDiscRack))
            {
                returnValue = BiblioCraft_Compatibility.discRackAngle(world, x, y, z);
            }
            if ((tile instanceof TileEntityBookcase))
            {
                returnValue = BiblioCraft_Compatibility.rotateBookcase(tile);
            }
            if ((tile instanceof TileEntityDinnerPlate))
            {
                returnValue = BiblioCraft_Compatibility.rotateDinnerPlate(tile);
            }
            if ((tile instanceof TileEntityGenericShelf))
            {
                returnValue = BiblioCraft_Compatibility.rotateGenericShelf(tile);
            }
            if ((tile instanceof TileEntityLamp))
            {
                returnValue = BiblioCraft_Compatibility.rotateLamp(tile);
            }
            if ((tile instanceof TileEntityLantern))
            {
                returnValue = BiblioCraft_Compatibility.rotateLantern(tile);
            }
            if ((tile instanceof TileEntityMapFrame))
            {
                returnValue = BiblioCraft_Compatibility.rotateMapFrame(tile);
            }
            if ((tile instanceof TileEntityPotionShelf))
            {
                returnValue = BiblioCraft_Compatibility.rotatePotionShelf(tile);
            }
            if ((tile instanceof TileEntityWeaponCase))
            {
                returnValue = BiblioCraft_Compatibility.rotateDisplayCase(tile);
            }
            if ((tile instanceof TileEntityWeaponRack))
            {
                returnValue = BiblioCraft_Compatibility.rotateWeaponRack(tile);
            }
            if ((tile instanceof TileEntityWritingDesk))
            {
                returnValue = BiblioCraft_Compatibility.rotateDesk(tile);
            }
            if ((tile instanceof TileEntityTypeMachine))
            {
                returnValue = BiblioCraft_Compatibility.rotate4baseMeta(world, x, y, z);
                world.markBlockForUpdate(x, y, z);
            }
            if ((tile instanceof TileEntityPrintPress))
            {
                returnValue = BiblioCraft_Compatibility.rotate4baseMeta(world, x, y, z);
                world.markBlockForUpdate(x, y, z);
            }
            if ((tile instanceof TileEntityArmorStand))
            {
                returnValue = BiblioCraft_Compatibility.rotateArmorStand(world, x, y, z);
            }
            if ((tile instanceof TileEntityTable))
            {
                returnValue = BiblioCraft_Compatibility.removeTableCarpet(world, tile, x, y, z);
            }
        }
        else
        {
            if ((tile instanceof TileEntityDiscRack))
            {
                returnValue = BiblioCraft_Compatibility.discRackRotate(world, x, y, z);
            }
            if ((tile instanceof TileEntityLamp))
            {
                returnValue = BiblioCraft_Compatibility.setLampStyle(tile);
            }
            if ((tile instanceof TileEntityLantern))
            {
                returnValue = BiblioCraft_Compatibility.setLanternStyle(tile);
            }
            if ((tile instanceof TileEntityWeaponCase))
            {
                returnValue = BiblioCraft_Compatibility.setDisplayCaseStyle(tile);
            }

        }
        return returnValue;
    }

    static class BiblioCraft_Compatibility {
        private static TileEntity tile1;
        private static TileEntity tile2;

        public static boolean removeTableCarpet(World world, TileEntity tile, int i, int j, int k)
        {
            TileEntityTable tableTile = (TileEntityTable) tile;
            if (tableTile != null)
            {
                dropCarpet(world, i, j, k, 2);
                tableTile.setCarpet(null);
                return true;
            }
            return false;
        }

        private static void dropCarpet(World world, int i, int j, int k, int slot) {
            TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
            if (!(tileEntity instanceof IInventory))
            {
                return;
            }

            IInventory inventory = (IInventory)tileEntity;
            TileEntityTable tableTile = (TileEntityTable)tileEntity;
            ItemStack stuff;
            if ((tableTile.isSlotFull()) && (slot == 0))
            {
                stuff = inventory.getStackInSlot(0);
            }
            else
            {
                if ((tableTile.isClothSlotFull()) && (slot == 1))
                {
                    stuff = inventory.getStackInSlot(1);
                }
                else
                {
                    if ((tableTile.isCarpetFull()) && (slot == 2))
                    {
                        stuff = inventory.getStackInSlot(2);
                    }
                    else
                    {
                        stuff = null;
                    }
                }
            }
            if ((stuff != null) && (stuff.stackSize > 0))
            {
                EntityItem entityItem = new EntityItem(world, i + 0.5F, j + 1.4F, k + 0.5F, new ItemStack(stuff.itemID, stuff.stackSize, stuff.getItemDamage()));

                if (stuff.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound)stuff.getTagCompound().copy());
                }
                entityItem.motionX = 0.0D;
                entityItem.motionY = 0.0D;
                entityItem.motionZ = 0.0D;
                world.spawnEntityInWorld(entityItem);
                stuff.stackSize = 0;
            }
        }

        public static boolean rotateArmorStand(World world, int i, int j, int k)
        {
            int currMeta = world.getBlockMetadata(i, j, k);
            if (currMeta >= 3)
            {
                currMeta = 0;
            }
            else
            {
                currMeta++;
            }
            world.setBlockMetadataWithNotify(i, j, k, currMeta, 0);
            currMeta = world.getBlockMetadata(i, j + 1, k);
            if (currMeta >= 7)
            {
                currMeta = 4;
            }
            else
            {
                currMeta++;
            }
            world.setBlockMetadataWithNotify(i, j + 1, k, currMeta, 0);
            world.markBlockForUpdate(i, j, k);
            world.markBlockForUpdate(i, j + 1, k);
            return true;
        }

        public static boolean rotate4baseMeta(World world, int i, int j, int k)
        {
            int currMeta = world.getBlockMetadata(i, j, k);

            if (currMeta >= 3)
            {
                currMeta = 0;
            }
            else
            {
                currMeta++;
            }

            world.setBlockMetadataWithNotify(i, j, k, currMeta, 0);

            return true;
        }

        public static boolean rotateSeat(TileEntity tile)
        {
            if ((tile instanceof TileEntitySeat))
            {
                TileEntitySeat seat = (TileEntitySeat)tile;
                int angle = seat.getAngle();
                if (angle >= 3)
                {
                    angle = 0;
                }
                else
                {
                    angle++;
                }
                seat.setAngle(angle);
            }
            return false;
        }

        public static boolean rotateBookcase(TileEntity tile)
        {
            if ((tile instanceof TileEntityBookcase))
            {
                TileEntityBookcase te = (TileEntityBookcase)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateDinnerPlate(TileEntity tile) {
            if ((tile instanceof TileEntityDinnerPlate))
            {
                TileEntityDinnerPlate te = (TileEntityDinnerPlate)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateGenericShelf(TileEntity tile) {
            if ((tile instanceof TileEntityGenericShelf))
            {
                TileEntityGenericShelf te = (TileEntityGenericShelf)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateLamp(TileEntity tile) {
            if ((tile instanceof TileEntityLamp))
            {
                TileEntityLamp te = (TileEntityLamp)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean setLampStyle(TileEntity tile) {
            if ((tile instanceof TileEntityLamp))
            {
                TileEntityLamp te = (TileEntityLamp)tile;
                if (te != null)
                {
                    int currAngle = te.getStyle();
                    if (currAngle >= 2)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setStyle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateLantern(TileEntity tile) {
            if ((tile instanceof TileEntityLantern))
            {
                TileEntityLantern te = (TileEntityLantern)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean setLanternStyle(TileEntity tile) {
            if ((tile instanceof TileEntityLantern))
            {
                TileEntityLantern te = (TileEntityLantern)tile;
                if (te != null)
                {
                    int currAngle = te.getStyle();
                    if (currAngle >= 2)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setStyle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateMapFrame(TileEntity tile) {
            if ((tile instanceof TileEntityMapFrame))
            {
                TileEntityMapFrame te = (TileEntityMapFrame)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotatePotionShelf(TileEntity tile) {
            if ((tile instanceof TileEntityPotionShelf))
            {
                TileEntityPotionShelf te = (TileEntityPotionShelf)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateDisplayCase(TileEntity tile)
        {
            if ((tile instanceof TileEntityWeaponCase))
            {
                TileEntityWeaponCase te = (TileEntityWeaponCase)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle > 3)
                    {
                        if (currAngle >= 7)
                        {
                            currAngle = 4;
                        }
                        else
                        {
                            currAngle++;
                        }

                    }
                    else if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }

                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean setDisplayCaseStyle(TileEntity tile) {
            if ((tile instanceof TileEntityWeaponCase))
            {
                TileEntityWeaponCase te = (TileEntityWeaponCase)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle > 3)
                    {
                        currAngle -= 4;
                    }
                    else
                    {
                        currAngle += 4;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateWeaponRack(TileEntity tile) {
            if ((tile instanceof TileEntityWeaponRack))
            {
                TileEntityWeaponRack te = (TileEntityWeaponRack)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean rotateDesk(TileEntity tile) {
            if ((tile instanceof TileEntityWritingDesk))
            {
                TileEntityWritingDesk te = (TileEntityWritingDesk)tile;
                if (te != null)
                {
                    int currAngle = te.getAngle();
                    if (currAngle >= 3)
                    {
                        currAngle = 0;
                    }
                    else
                    {
                        currAngle++;
                    }
                    te.setAngle(currAngle);
                    return true;
                }
            }
            return false;
        }

        public static boolean discRackAngle(World world, int i, int j, int k)
        {
            TileEntity tile = world.getBlockTileEntity(i, j, k);
            if ((tile != null) && ((tile instanceof TileEntityDiscRack)))
            {
                TileEntityDiscRack discTile = (TileEntityDiscRack)tile;
                int angle = discTile.getAngle();

                if (angle >= 3)
                {
                    discTile.setAngle(0);
                }
                else
                {
                    angle++;
                    discTile.setAngle(angle);
                }
                return true;
            }
            return false;
        }

        public static boolean discRackRotate(World world, int i, int j, int k) {
            TileEntity tile = world.getBlockTileEntity(i, j, k);
            if ((tile != null) && ((tile instanceof TileEntityDiscRack)))
            {
                TileEntityDiscRack discTile = (TileEntityDiscRack)tile;
                int angle = discTile.getVertAngle();
                if (angle == 1)
                {
                    discTile.setWallRotation();
                    return true;
                }
            }

            return false;
        }

        public static boolean connectChairs(EntityPlayer player, World world, int i, int j, int k)
        {
            if (player.isSneaking())
            {
                if (tile1 == null)
                {
                    tile1 = world.getBlockTileEntity(i, j, k);
                    if (tile1 != null)
                    {
                        if ((tile1 instanceof TileEntitySeat))
                        {
                            player.addChatMessage(StatCollector.translateToLocal("screwgun.firstSeat"));
                            return true;
                        }

                        tile1 = null;
                        tile2 = null;
                        return false;
                    }
                }

                if ((tile1 != null) && ((tile1 instanceof TileEntitySeat)))
                {
                    if ((tile1.xCoord != i) || (tile1.yCoord != j) || (tile1.zCoord != k))
                    {
                        tile2 = world.getBlockTileEntity(i, j, k);
                        if (tile2 != null)
                        {
                            if ((tile2 instanceof TileEntitySeat))
                            {
                                player.addChatMessage(StatCollector.translateToLocal("screwgun.secondSeat"));

                                int diffX = tile1.xCoord - tile2.xCoord;
                                int diffY = tile1.yCoord - tile2.yCoord;
                                int diffZ = tile1.zCoord - tile2.zCoord;
                                setChairConnects((TileEntitySeat)tile1, (TileEntitySeat)tile2, diffX, diffY, diffZ);
                                tile1 = null;
                                tile2 = null;
                                return true;
                            }

                            tile1 = null;
                            tile2 = null;
                            return false;
                        }
                    }
                }

            }

            return false;
        }

        public static void setChairConnects(TileEntitySeat seatTile1, TileEntitySeat seatTile2, int diffX, int diffY, int diffZ)
        {
            if (diffY != 0)
            {
                return;
            }
            if ((diffX == 1) && (diffZ == 0))
            {
                if (!seatTile1.getWestConnect())
                {
                    seatTile1.setWestConnect(true);
                }
                else
                {
                    seatTile1.setWestConnect(false);
                }
                if (!seatTile2.getEastConnect())
                {
                    seatTile2.setEastConnect(true);
                }
                else
                {
                    seatTile2.setEastConnect(false);
                }
            }
            else if ((diffX == -1) && (diffZ == 0))
            {
                if (!seatTile1.getEastConnect())
                {
                    seatTile1.setEastConnect(true);
                }
                else
                {
                    seatTile1.setEastConnect(false);
                }
                if (!seatTile2.getWestConnect())
                {
                    seatTile2.setWestConnect(true);
                }
                else
                {
                    seatTile2.setWestConnect(false);
                }
            }
            else if ((diffZ == 1) && (diffX == 0))
            {
                if (!seatTile1.getNorthConnect())
                {
                    seatTile1.setNorthConnect(true);
                }
                else
                {
                    seatTile1.setNorthConnect(false);
                }
                if (!seatTile2.getSouthConnect())
                {
                    seatTile2.setSouthConnect(true);
                }
                else
                {
                    seatTile2.setSouthConnect(false);
                }
            }
            else if ((diffZ == -1) && (diffX == 0))
            {
                if (!seatTile1.getSouthConnect())
                {
                    seatTile1.setSouthConnect(true);
                }
                else
                {
                    seatTile1.setSouthConnect(false);
                }
                if (!seatTile2.getNorthConnect())
                {
                    seatTile2.setNorthConnect(true);
                }
                else
                {
                    seatTile2.setNorthConnect(false);
                }
            }
        }
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

    @Optional.Method(modid = "CarpentersBlocks")
    @Override
    public void onHammerUse(World world, EntityPlayer player) {
        drainWrenchPower(player.getCurrentEquippedItem());
    }

    @Optional.Method(modid = "CarpentersBlocks")
    @Override
    public boolean canUseHammer(World world, EntityPlayer player) {
        return canWrench(player.getCurrentEquippedItem());
    }
}