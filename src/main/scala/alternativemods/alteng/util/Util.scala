package alternativemods.alteng.util

object Util {
  def diff(a: Double, b: Double) = if(a > b) a - b else b - a
}
