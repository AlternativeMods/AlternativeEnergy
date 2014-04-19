package alternativemods.alteng.tileentities

import cpw.mods.fml.common.registry.GameRegistry

object AltEngTileEntities {

  def load(){
    GameRegistry.registerTileEntity(classOf[TileEntityConveyor], "alteng.conveyor")
    GameRegistry.registerTileEntity(classOf[TileEntityConveyorInsertion], "alteng.conveyorInsertion")
    GameRegistry.registerTileEntity(classOf[TileEntityPowerConsumer], "alteng.powerConsumer")
  }
}
