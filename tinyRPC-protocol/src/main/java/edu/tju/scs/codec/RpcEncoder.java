package edu.tju.scs.codec;

import edu.tju.scs.serializer.JacksonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 16:48 18/5/14.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> classType;

    public RpcEncoder(Class<?> classType) {
        this.classType = classType;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = JacksonUtil.serialize(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
