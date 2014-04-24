package alternativemods.alteng.network

import io.netty.buffer.ByteBuf

/**
 * No description given
 *
 * @author jk-5
 */
class PacketGuiIntUpdate(var windowId: Int, var id: Int, var data: Int) extends Packet {
  def this() = this(0, 0, 0 )

  override def encode(buffer: ByteBuf){
    buffer.writeInt(this.windowId)
    buffer.writeInt(this.id)
    buffer.writeInt(this.data)
  }

  override def decode(buffer: ByteBuf){
    this.windowId = buffer.readInt()
    this.id = buffer.readInt()
    this.data = buffer.readInt()
  }
}
