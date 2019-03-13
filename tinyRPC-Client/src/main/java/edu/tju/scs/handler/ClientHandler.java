package edu.tju.scs.handler;

import edu.tju.scs.RpcRequest;
import edu.tju.scs.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 01:18 18/5/16.
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    private RpcRequest rpcRequest;
    private RpcResponse rpcResponse;

    public ClientHandler(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        this.rpcRequest = rpcRequest;
        this.rpcResponse = rpcResponse;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println(o.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(rpcRequest);
    }
}
