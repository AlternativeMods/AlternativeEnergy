package alternativemods.alteng.network

import io.netty.buffer.ByteBuf

trait Packet {
  def encode(buffer: ByteBuf)
  def decode(buffer: ByteBuf)
}

class PacketTileEntityData extends Packet {

  var coords: (Int, Int, Int) = _
  var data: ByteBuf = _

  override def decode(buffer: ByteBuf){
    this.coords = (buffer.readInt(), buffer.readInt(), buffer.readInt())
    this.data = buffer.slice()
  }

  override def encode(buffer: ByteBuf){
    buffer.writeInt(this.coords._1)
    buffer.writeInt(this.coords._2)
    buffer.writeInt(this.coords._3)
    buffer.writeBytes(this.data)
  }
}
