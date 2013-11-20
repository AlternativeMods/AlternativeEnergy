package jkmau5.alternativeenergy.util

import net.minecraft.world.World
import net.minecraft.tileentity.TileEntity
import net.minecraft.block.Block
import net.minecraftforge.common.ForgeDirection

/**
 * No description given
 *
 * @author jk-5
 */
object BufferedTileEntity {
  def makeBuffer(world: World, x: Int, y: Int, z: Int, loadUnloaded: Boolean): Array[BufferedTileEntity] = {
    val buffer = new Array[BufferedTileEntity](6)
    for(direction <- ForgeDirection.VALID_DIRECTIONS){
      buffer(direction.ordinal()) = new BufferedTileEntity(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, loadUnloaded)
    }
    buffer
  }
}

final class BufferedTileEntity(private val world: World, private val x: Int, private val y: Int, private val z: Int, private val loadUnloaded: Boolean) {

  private var blockID = 0
  private var tileEntity: TileEntity = _
  private val tracker = new TimeTracker

  this.refresh()

  def refresh(){
    this.tileEntity = null
    this.blockID = 0
    if(!this.loadUnloaded && !this.world.blockExists(x, y, z)) return
    this.blockID = this.world.getBlockId(x, y, z)
    val block = Block.blocksList(this.blockID)
    if(block != null && block.hasTileEntity(world.getBlockMetadata(x, y, z))){
      this.tileEntity = world.getBlockTileEntity(x, y, z)
    }
  }

  def set(id: Int, tile: TileEntity){
    this.blockID = id
    this.tileEntity = tile
    this.tracker.markTime(world)
  }

  def getBlockID: Int = {
    if(this.tileEntity != null && !this.tileEntity.isInvalid) return this.blockID
    if(this.tracker.markIfTimePassed(this.world, 20)){
      this.refresh()
      if(this.tileEntity != null && !this.tileEntity.isInvalid) return this.blockID
    }
    0
  }

  def getTileEntity: TileEntity = {
    if(this.tileEntity != null && !this.tileEntity.isInvalid) return this.tileEntity
    if(this.tracker.markIfTimePassed(this.world, 20)){
      this.refresh()
      if(this.tileEntity != null && !this.tileEntity.isInvalid) return this.tileEntity
    }
    null
  }

  def exists: Boolean = {
    if(this.tileEntity != null && !this.tileEntity.isInvalid) true
    else this.world.blockExists(x, y, z)
  }
}
