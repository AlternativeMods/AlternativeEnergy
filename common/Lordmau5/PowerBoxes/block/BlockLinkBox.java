package Lordmau5.PowerBoxes.block;

import Lordmau5.PowerBoxes.core.Config;
import Lordmau5.PowerBoxes.core.GUIHandler;
import Lordmau5.PowerBoxes.core.Main;
import Lordmau5.PowerBoxes.tile.TileEntityLinkBox;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * Author: Lordmau5
 * Date: 03.11.13
 * Time: 11:33
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class BlockLinkBox extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public BlockLinkBox(int par1, Material par2Material) {
        super(par1, par2Material);
        setUnlocalizedName("LinkBox");
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
        return new TileEntityLinkBox();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
            return false;

        TileEntity tempTile = world.getBlockTileEntity(x, y, z);
        if(tempTile == null)
            return false;

        if(player.getHeldItem() != null)
            return false;

        if(tempTile instanceof TileEntityLinkBox) {
            TileEntityLinkBox pBox = (TileEntityLinkBox) tempTile;

            if(player.getHeldItem() != null && Main.isWrench(player.getHeldItem().itemID) || (!Main.BCSupplied && !Main.ICSupplied && player.getHeldItem() == null)) {
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
            else {
                if(player.isSneaking())
                    return false;
                player.openGui(Main.instance, GUIHandler.ID_GUI_LinkBox, world, x, y, z);
                return true;
            }
        }
        return false;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess bAccess, int x, int y, int z, int side) {
        TileEntity tmpTile = bAccess.getBlockTileEntity(x, y, z);
        if(tmpTile != null && tmpTile instanceof TileEntityLinkBox) {
            TileEntityLinkBox pBox = (TileEntityLinkBox) tmpTile;

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