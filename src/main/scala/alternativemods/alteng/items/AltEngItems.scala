package alternativemods.alteng.items

import cpw.mods.fml.common.registry.GameRegistry

object AltEngItems {

  var itemEnergyConsumer: ItemEnergyConsumer = _
  def load(){
    itemEnergyConsumer = new ItemEnergyConsumer()

    GameRegistry.registerItem(itemEnergyConsumer, "itemEnergyConsumer")
  }

}
