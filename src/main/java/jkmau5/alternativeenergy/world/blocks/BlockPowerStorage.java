package jkmau5.alternativeenergy.world.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.Config;
import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.util.EnumOutputMode;
import jkmau5.alternativeenergy.world.tileentity.TileEntityLinkBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerBox;
import jkmau5.alternativeenergy.world.tileentity.TileEntityPowerStorage;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
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

    public static String[] iconNames = new String[] {"powerBox", "linkBox"};
    @SideOnly(Side.CLIENT) public Icon[] icon_input = new Icon[iconNames.length];
    @SideOnly(Side.CLIENT) public Icon[] icon_output = new Icon[iconNames.length];
    @SideOnly(Side.CLIENT) public Icon[] icon_disabled = new Icon[iconNames.length];

    public BlockPowerStorage() {
        super("powerStorage", Material.iron);
        this.setHardness(5.0F);
        if(Config.powerBoxExplosionResistant) {
            this.setResistance(6000000.0F);
        } else {
            this.setResistance(10.0F);
        }
        this.setStepSound(soundMetalFootstep);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return this.icon_disabled[meta];
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if(metadata == 0) {
            return new TileEntityPowerBox();
        } else if(metadata == 1) {
            return new TileEntityLinkBox();
        }
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
        for(int i = 0; i < iconNames.length; i++) {
            icon_input[i] = register.registerIcon(Constants.TEXTURE_DOMAIN + ":" + iconNames[i] + "_input");
            icon_output[i] = register.registerIcon(Constants.TEXTURE_DOMAIN + ":" + iconNames[i] + "_output");
            icon_disabled[i] = register.registerIcon(Constants.TEXTURE_DOMAIN + ":" + iconNames[i] + "_disabled");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if(tile != null && tile instanceof TileEntityPowerStorage) {
            TileEntityPowerStorage storage = (TileEntityPowerStorage) tile;
            EnumOutputMode mode = storage.getMode(ForgeDirection.getOrientation(side));
            int meta = world.getBlockMetadata(x, y, z);
            if(mode == EnumOutputMode.INPUT) {
                return icon_input[meta];
            }
            if(mode == EnumOutputMode.OUTPUT) {
                return icon_output[meta];
            }
            if(mode == EnumOutputMode.DISABLED) {
                return icon_disabled[meta];
            }
        }
        return icon_disabled[0];
    }
}
