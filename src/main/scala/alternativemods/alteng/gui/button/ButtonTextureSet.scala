package alternativemods.alteng.gui.button

/**
 * No description given
 *
 * @author jk-5
 */
trait ButtonTextureSet {
  def getX: Int
  def getY: Int
  def getWidth: Int
  def getHeight: Int
}

object ButtonTextureSets {
  val largeButton = new DefaultButtonTextureSet(0, 88, 20, 200)
  val smallButton = new DefaultButtonTextureSet(0, 168, 15, 200)
  val lockedButton = new DefaultButtonTextureSet(224, 0, 16, 16)
  val unlockedButton = new DefaultButtonTextureSet(240, 0, 16, 16)
  val leftButton = new DefaultButtonTextureSet(204, 0, 16, 10)
  val rightButton = new DefaultButtonTextureSet(214, 0, 16, 10)
}

class DefaultButtonTextureSet(x: Int, y: Int, width: Int, height: Int) extends ButtonTextureSet {
  override def getX = x
  override def getY = y
  override def getWidth = width
  override def getHeight = height
}
