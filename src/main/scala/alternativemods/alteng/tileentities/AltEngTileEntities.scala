package alternativemods.alteng.tileentities

import cpw.mods.fml.common.registry.GameRegistry

/**
 * Author: Lordmau5
 * Date: 02.04.14
 * Time: 17:06
 */
object AltEngTileEntities {

  def load(){
    GameRegistry.registerTileEntity(classOf[TileEntityConveyor], "alteng.conveyor")
  }
}
