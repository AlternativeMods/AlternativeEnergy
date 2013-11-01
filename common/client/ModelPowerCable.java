package client;

import core.Main;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * Author: Lordmau5
 * Date: 29.10.13
 * Time: 18:10
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ModelPowerCable extends ModelBase {

    ModelRenderer base;
    ModelRenderer toNorth;
    ModelRenderer toSouth;
    ModelRenderer toEast;
    ModelRenderer toWest;
    ModelRenderer toTop;
    ModelRenderer toBottom;

    public ModelPowerCable() {
        initModels();
    }

    public void initModels() {
        textureWidth = 256;
        textureHeight = 128;

        int squareSize = 20;
        int textureWidth = 128;
        int textureHeight = 64;

        base = new ModelRenderer(this, 0, 0);
        base.addBox(0F, 0F, 0F, 20, 20, 20);
        base.setRotationPoint(-5F, 27F, -5F);
        base.setTextureSize(textureWidth, textureHeight);
        setRotation(base, 0F, 0F, 0F);

        //-----------------------------------------------------------------

        toNorth = new ModelRenderer(this, 0, 64);
        toNorth.addBox(0F, 0F, -2F, squareSize, squareSize, squareSize + 2);
        toNorth.setRotationPoint(-5F, 27F, -25F);
        toNorth.setTextureSize(textureWidth, textureHeight);
        setRotation(toNorth, 0F, 0F, 0F);

        toSouth = new ModelRenderer(this, 0, 64);
        toSouth.addBox(0F, 0F, 0F, squareSize, squareSize, squareSize + 2);
        toSouth.setRotationPoint(-5F, 27F, 15F);
        toSouth.setTextureSize(textureWidth, textureHeight);
        setRotation(toSouth, 0F, 0F, 0F);

        //-----------------------------------------------------------------

        toEast = new ModelRenderer(this, 128, 0);
        toEast.addBox(-2F, 0F, 0F, squareSize + 2, squareSize, squareSize);
        toEast.setRotationPoint(-25F, 27F, -5F);
        toEast.setTextureSize(textureWidth, textureHeight);
        setRotation(toEast, 0F, 0F, 0F);

        toWest = new ModelRenderer(this, 128, 0);
        toWest.addBox(-2F, 0F, 0F, squareSize + 2, squareSize, squareSize);
        toWest.setRotationPoint(17F, 27F, -5F);
        toWest.setTextureSize(textureWidth, textureHeight);
        setRotation(toWest, 0F, 0F, 0F);

        //-----------------------------------------------------------------

        toTop = new ModelRenderer(this, 128, 64);
        toTop.addBox(0F, -2F, 0F, squareSize, squareSize + 2, squareSize);
        toTop.setRotationPoint(-5F, 7F, -5F);
        toTop.setTextureSize(textureWidth, textureHeight);
        setRotation(toTop, 0F, 0F, 0F);

        toBottom = new ModelRenderer(this, 128, 64);
        toBottom.addBox(0F, -2F, 0F, squareSize, squareSize + 2, squareSize);
        toBottom.setRotationPoint(-5F, 49F, -5F);
        toBottom.setTextureSize(128, 64);
        setRotation(toBottom, 0F, 0F, 0F);

    }

    public void render(TileEntity tile, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(null, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
        base.render(f5);
        for(int i=0; i<6; i++) {
            ForgeDirection dr = ForgeDirection.getOrientation(i);
            TileEntity tmpTile = tile.worldObj.getBlockTileEntity(tile.xCoord + dr.offsetX, tile.yCoord + dr.offsetY, tile.zCoord + dr.offsetZ);
            if(tmpTile != null && Main.isValidPowerTile(tmpTile)) {
                if(dr == ForgeDirection.UP)
                    toTop.render(f5);
                else if(dr == ForgeDirection.DOWN)
                    toBottom.render(f5);
                else if(dr == ForgeDirection.NORTH)
                    toNorth.render(f5);
                else if(dr == ForgeDirection.SOUTH)
                    toSouth.render(f5);
                else if(dr == ForgeDirection.EAST)
                    toEast.render(f5);
                else if(dr == ForgeDirection.WEST)
                    toWest.render(f5);
            }
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}