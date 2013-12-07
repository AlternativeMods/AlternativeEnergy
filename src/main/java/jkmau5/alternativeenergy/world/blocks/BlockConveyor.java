package jkmau5.alternativeenergy.world.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.world.tileentity.TileEntityConveyor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * No description given
 *
 * @author jk-5
 */
public class BlockConveyor extends BlockTileEntity {

    @SideOnly(Side.CLIENT) private Icon textureBottom;
    @SideOnly(Side.CLIENT) private Icon textureSides;
    @SideOnly(Side.CLIENT) private Icon[] textureTop = new Icon[4];
    @SideOnly(Side.CLIENT) private Icon[] textureTopActive = new Icon[4];

    private static final double SPEED = 0.09D;

    public BlockConveyor() {
        super("conveyor", Material.iron);
        this.setBlockBounds(0, 0, 0, 1, 0.4f, 1);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityConveyor();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.textureBottom = register.registerIcon(Constants.TEXTURE_DOMAIN + ":conveyor/bottom");
        this.textureSides = register.registerIcon(Constants.TEXTURE_DOMAIN + ":conveyor/sides");
        for(int i = 0; i < this.textureTop.length; i++) {
            this.textureTop[i] = register.registerIcon(Constants.TEXTURE_DOMAIN + ":conveyor/top_" + i);
            this.textureTopActive[i] = register.registerIcon(Constants.TEXTURE_DOMAIN + ":conveyor/top_" + i + "_active");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if(side == 0) {
            return this.textureBottom;
        }
        if(side == 1) {
            return this.textureTopActive[meta];
        }
        if(meta == 0) if(side == 2) {
                return this.textureTopActive[2];
            } else if(side == 3) {
                return this.textureTopActive[0];
            } else {
                return this.textureSides;
            }
        if(meta == 1) if(side == 5) {
                return this.textureTopActive[2];
            } else if(side == 4) {
                return this.textureTopActive[0];
            } else {
                return this.textureSides;
            }
        if(meta == 2) if(side == 3) {
                return this.textureTopActive[2];
            } else if(side == 2) {
                return this.textureTopActive[0];
            } else {
                return this.textureSides;
            }
        if(meta == 3) if(side == 4) {
                return this.textureTopActive[2];
            } else if(side == 5) {
                return this.textureTopActive[0];
            } else {
                return this.textureSides;
            }
        return this.textureSides;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
        int facing = MathHelper.floor_double((double)(entity.rotationYaw * 4F / 360F) + .5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, facing, 2);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
        double xVel, yVel, zVel;
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection direction = ForgeDirection.UNKNOWN;
        if(meta == 0) {
            direction = ForgeDirection.SOUTH;
        }
        if(meta == 1) {
            direction = ForgeDirection.WEST;
        }
        if(meta == 2) {
            direction = ForgeDirection.NORTH;
        }
        if(meta == 3) {
            direction = ForgeDirection.EAST;
        }
        if(direction == ForgeDirection.UNKNOWN) {
            return;
        }

        xVel = direction.offsetX * SPEED;
        yVel = direction.offsetY * SPEED;
        zVel = direction.offsetZ * SPEED;

        if(direction.offsetX != 0) {
            if(entity.posZ > z + 0.6D) {
                zVel = -0.1D;
            } else if(entity.posZ < z + 0.4D) {
                zVel = 0.1D;
            }
        } else if(direction.offsetZ != 0) {
            if(entity.posX > x + 0.6D) {
                xVel = -0.1D;
            } else if(entity.posX < x + 0.4D) {
                xVel = 0.1D;
            }
        }
        entity.setVelocity(xVel, yVel, zVel);
        if(entity instanceof EntityItem) {
            ((EntityItem) entity).age = 0;
        }
    }
}
