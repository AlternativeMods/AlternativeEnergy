package alternativemods.alteng.util

import net.minecraft.world.World

/**
 * No description given
 *
 * @author jk-5
 */
private abstract class Side {
  val isClient: Boolean
  val isServer: Boolean
}
private object Client extends Side {
  val isClient = true
  val isServer = false
}
private object Server extends Side {
  val isClient = false
  val isServer = true
}

object Side {
  def apply(world: World): Side = if(world.isRemote) Client else Server
}
