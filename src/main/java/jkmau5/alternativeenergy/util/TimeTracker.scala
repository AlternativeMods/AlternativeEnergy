package jkmau5.alternativeenergy.util

import net.minecraft.world.World

/**
 * No description given
 *
 * @author jk-5
 */
class TimeTracker {

  private var lastMark = Long.MinValue
  private var duration = -1l

  /**
   * Return true if a given delay has passed since last time marked was called
   * successfully.
   */
  def markIfTimePassed(world: World, delay: Long): Boolean = {
    if(world == null) return false
    val currentTime = world.getTotalWorldTime
    if(currentTime < this.lastMark){
      this.lastMark = currentTime
      false
    }else if(this.lastMark + delay <= currentTime){
      this.duration = currentTime - this.lastMark
      this.lastMark = currentTime
      true
    }else false
  }

  @inline def lastDelay = if(this.duration > 0) this.duration else 0l
  @inline def markTime(world: World) = this.lastMark = world.getTotalWorldTime
}
