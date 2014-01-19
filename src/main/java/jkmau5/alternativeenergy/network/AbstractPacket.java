package jkmau5.alternativeenergy.network;

import io.netty.buffer.ByteBuf;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AbstractPacket {

    public abstract void encode(ByteBuf buffer);
    public abstract void decode(ByteBuf buffer);
}
