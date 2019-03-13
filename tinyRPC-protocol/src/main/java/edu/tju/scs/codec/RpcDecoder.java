package edu.tju.scs.codec;

import edu.tju.scs.serializer.JacksonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 19:10 18/5/14.
 */
public class RpcDecoder extends ByteToMessageDecoder{

    private Class<?> classType;

    public RpcDecoder(Class<?> classType) {
        this.classType = classType;
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() < 4){
            return;
        }

        int length = byteBuf.readInt();
        if(length != byteBuf.readableBytes()){
            throw new RuntimeException("Wrong bytes to be read, expected: " + length + " bytes.");
        }
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object obj = JacksonUtil.deserialize(bytes, classType);
        list.add(obj);
    }
}
