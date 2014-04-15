package alternativemods.alteng.items

import cpw.mods.fml.common.registry.GameRegistry

/**
 * @author: Lordmau5
 * @date: 15.04.14 - 10:50
 */
object AltEngItems {

  var itemEnergyConsumer: ItemEnergyConsumer = _
  def load(){
    itemEnergyConsumer = new ItemEnergyConsumer()

    GameRegistry.registerItem(itemEnergyConsumer, "itemEnergyConsumer")
  }

}
