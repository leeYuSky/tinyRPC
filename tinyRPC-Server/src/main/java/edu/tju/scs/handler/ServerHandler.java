package edu.tju.scs.handler;

import edu.tju.scs.RpcRequest;
import edu.tju.scs.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 22:47 18/5/14.
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    private Map<String,Object> handlerMap;

    public ServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        System.out.println(request.toString());

        RpcResponse rpcResponse = handlerRequest(request);

        channelHandlerContext.writeAndFlush(rpcResponse);


    }


    private RpcResponse handlerRequest(RpcRequest request){

        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        String className = request.getClassName();

        Object result = null;

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResponseId(request.getRequestId());

        if(handlerMap.containsKey(className)){
            Object o = handlerMap.get(className);
            Method[] methods = o.getClass().getDeclaredMethods();
            Method method = null;
            for(Method m : methods){
                if (m.getName().equals(methodName)){
                    method = m;
                    break;
                }
            }
            if(method != null){
                try {
                    result = method.invoke(o,parameters);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                rpcResponse.setError("success");
            }else{
                rpcResponse.setError("error");
            }
        } else {
            rpcResponse.setError("error");
        }

        rpcResponse.setResult(result);
        return  rpcResponse;
    }
}
