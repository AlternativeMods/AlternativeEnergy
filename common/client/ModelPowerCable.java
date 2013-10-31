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
    ModelRenderer[] toNorth = new ModelRenderer[2];
    ModelRenderer[] toSouth = new ModelRenderer[2];
    ModelRenderer[] toEast = new ModelRenderer[2];
    ModelRenderer[] toWest = new ModelRenderer[2];
    ModelRenderer[] toTop = new ModelRenderer[2];
    ModelRenderer[] toBottom = new ModelRenderer[2];

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

        toNorth[0] = new ModelRenderer(this, 0, 64);
        toNorth[0].addBox(0F, 0F, 0F, squareSize, squareSize, 25);
        toNorth[0].setRotationPoint(-5F, 27F, -25F);
        toNorth[0].setTextureSize(textureWidth, textureHeight);
        setRotation(toNorth[0], 0F, 0F, 0F);
        /*toNorth[1] = new ModelRenderer(this, 0, 64);
        toNorth[1].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toNorth[1].setRotationPoint(-5F, 27F, -27F);
        toNorth[1].setTextureSize(textureWidth, textureHeight);
        setRotation(toNorth[1], 0F, 0F, 0F);   */

        toSouth[0] = new ModelRenderer(this, 0, 64);
        toSouth[0].addBox(0F, 0F, 0F, squareSize, squareSize, 25);
        toSouth[0].setRotationPoint(-5F, 27F, 15F);
        toSouth[0].setTextureSize(textureWidth, textureHeight);
        setRotation(toSouth[0], 0F, 0F, 0F);
        /*toSouth[1] = new ModelRenderer(this, 0, 64);
        toSouth[1].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toSouth[1].setRotationPoint(-5F, 27F, 17F);
        toSouth[1].setTextureSize(textureWidth, textureHeight);
        setRotation(toSouth[1], 0F, 0F, 0F); */

        //-----------------------------------------------------------------

        toEast[0] = new ModelRenderer(this, 128, 0);
        toEast[0].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toEast[0].setRotationPoint(-15F, 27F, -5F);
        toEast[0].setTextureSize(textureWidth, textureHeight);
        setRotation(toEast[0], 0F, 0F, 0F);
        toEast[1] = new ModelRenderer(this, 128, 0);
        toEast[1].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toEast[1].setRotationPoint(-16F, 27F, -5F);
        toEast[1].setTextureSize(textureWidth, textureHeight);
        setRotation(toEast[1], 0F, 0F, 0F);

        toWest[0] = new ModelRenderer(this, 128, 0);
        toWest[0].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toWest[0].setRotationPoint(6F, 27F, -5F);
        toWest[0].setTextureSize(textureWidth, textureHeight);
        setRotation(toWest[0], 0F, 0F, 0F);
        toWest[1] = new ModelRenderer(this, 128, 0);
        toWest[1].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toWest[1].setRotationPoint(5F, 27F, -5F);
        toWest[1].setTextureSize(textureWidth, textureHeight);
        setRotation(toWest[1], 0F, 0F, 0F);

        //-----------------------------------------------------------------

        toTop[0] = new ModelRenderer(this, 128, 64);
        toTop[0].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toTop[0].setRotationPoint(-5F, 17F, -5F);
        toTop[0].setTextureSize(textureWidth, textureHeight);
        setRotation(toTop[0], 0F, 0F, 0F);
        toTop[1] = new ModelRenderer(this, 128, 64);
        toTop[1].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toTop[1].setRotationPoint(-5F, 16F, -5F);
        toTop[1].setTextureSize(textureWidth, textureHeight);
        setRotation(toTop[1], 0F, 0F, 0F);

        toBottom[0] = new ModelRenderer(this, 128, 64);
        toBottom[0].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toBottom[0].setRotationPoint(-5F, 37F, -5F);
        toBottom[0].setTextureSize(128, 64);
        setRotation(toBottom[0], 0F, 0F, 0F);
        toBottom[1] = new ModelRenderer(this, 128, 64);
        toBottom[1].addBox(0F, 0F, 0F, squareSize, squareSize, squareSize);
        toBottom[1].setRotationPoint(-5F, 38F, -5F);
        toBottom[1].setTextureSize(textureWidth, textureHeight);
        setRotation(toBottom[1], 0F, 0F, 0F);

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
                    for(ModelRenderer rend : toTop)
                        if(rend != null) rend.render(f5); else continue;
                else if(dr == ForgeDirection.DOWN)
                    for(ModelRenderer rend : toBottom)
                        if(rend != null) rend.render(f5); else continue;
                else if(dr == ForgeDirection.NORTH)
                    for(ModelRenderer rend : toNorth)
                        if(rend != null) rend.render(f5); else continue;
                else if(dr == ForgeDirection.SOUTH)
                    for(ModelRenderer rend : toSouth)
                        if(rend != null) rend.render(f5); else continue;
                else if(dr == ForgeDirection.EAST)
                    for(ModelRenderer rend : toEast)
                        if(rend != null) rend.render(f5); else continue;
                else if(dr == ForgeDirection.WEST)
                    for(ModelRenderer rend : toWest)
                        if(rend != null) rend.render(f5); else continue;
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