package alternativemods.alteng.util

import com.google.common.collect.{Lists, ForwardingList}
import java.util
import net.minecraft.util.EnumChatFormatting

/**
 * No description given
 *
 * @author jk-5
 */
class ToolTip(private val delay: Long = 0) extends ForwardingList[ToolTipLine] {

  private val lines = Lists.newArrayList[ToolTipLine]() //Why does this thing not support scala lists? A shame!
  private var hoverTime: Long = 0

  def onTick(hover: Boolean){
    if(delay == 0) return
    if(hover){
      if(hoverTime == 0) hoverTime = System.currentTimeMillis()
    }else hoverTime = 0
  }

  def display(): Boolean =
    if(delay == 0) true
    else if(hoverTime == 0) false
    else System.currentTimeMillis() - hoverTime >= delay

  def refresh(){}

  override def delegate(): util.List[ToolTipLine] = this.lines
}

case class ToolTipLine(var text: String = "", color: EnumChatFormatting = EnumChatFormatting.GRAY) {
  var spacing: Int = 0
}
