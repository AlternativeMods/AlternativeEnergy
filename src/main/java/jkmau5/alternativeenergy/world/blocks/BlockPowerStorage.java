package jkmau5.alternativeenergy.world.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.util.EnumOutputMode;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerStorage;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * No description given
 *
 * @author jk-5
 */
public class BlockPowerStorage extends BlockTileEntity {

    public String[] iconNames = new String[]{"powerBox", "linkBox"};
    @SideOnly(Side.CLIENT) public Icon[] icon_input = new Icon[iconNames.length];
    @SideOnly(Side.CLIENT) public Icon[] icon_output = new Icon[iconNames.length];
    @SideOnly(Side.CLIENT) public Icon[] icon_disabled = new Icon[iconNames.length];

    public BlockPowerStorage(int id){
        super(id, Material.iron);
        this.setUnlocalizedName("altEng.powerStorage");
        this.setHardness(5.0F);
        if(Config.powerBoxExplosionResistant){
            this.setResistance(6000000.0F);
        }else{
            this.setResistance(10.0F);
        }
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(AlternativeEnergy.tabPowerBox);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if(metadata == 0) return new TileEntityPowerBox();
        else if(metadata == 1) return new TileEntityLinkBox();
        return null;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata == 0 || metadata == 1;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        for(int i = 0; i < iconNames.length; i++){
            icon_input[i] = register.registerIcon(iconNames[i] + "_input");
            icon_output[i] = register.registerIcon(iconNames[i] + "_output");
            icon_disabled[i] = register.registerIcon(iconNames[i] + "_disabled");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof TileEntityPowerStorage){
            TileEntityPowerStorage storage = (TileEntityPowerStorage) tile;
            EnumOutputMode mode = storage.getMode(ForgeDirection.getOrientation(side));
            int meta = world.getBlockMetadata(x, y, z);
            if(mode == EnumOutputMode.INPUT) return icon_input[meta];
            if(mode == EnumOutputMode.OUTPUT) return icon_output[meta];
            if(mode == EnumOutputMode.DISABLED) return icon_disabled[meta];
        }
        return icon_disabled[0];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideInt, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile == null) return false;
        if(tile instanceof TileEntityPowerStorage) {
            TileEntityPowerStorage storageTile = (TileEntityPowerStorage) tile;
            ForgeDirection side = ForgeDirection.getOrientation(sideInt);
            if(player.getHeldItem() != null && AlternativeEnergy.isWrench(player.getHeldItem().itemID) || (!AlternativeEnergy.BCSupplied && !AlternativeEnergy.ICSupplied && player.getHeldItem() == null)) {
                if(player.isSneaking()) {
                    storageTile.setMode(side, storageTile.getNextMode(storageTile.getMode(ForgeDirection.getOrientation(sideInt))));
                    player.addChatMessage("Side \"" + side.toString().toLowerCase() + "\" is now set to " + storageTile.getMode(side).toString().toLowerCase());
                    return true;
                }else {
                    player.addChatMessage("Side \"" + side.toString().toLowerCase() + "\" is set to " + storageTile.getMode(side).toString().toLowerCase());
                    return true;
                }
            }else{
                if(player.isSneaking()) return false;
                if(!world.isRemote) player.openGui(AlternativeEnergy.instance, storageTile.getGuiID(), world, x, y, z);
                return true;
            }
        }
        return false;
    }
}
