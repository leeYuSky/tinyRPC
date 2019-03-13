package edu.tju.scs;

import edu.tju.scs.codec.RpcDecoder;
import edu.tju.scs.codec.RpcEncoder;
import edu.tju.scs.handler.ClientHandler;
import edu.tju.scs.service.IHelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.Response;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 17:53 18/5/11.
 */
public class RpcClient {

    private int port;

    private String host;

    private ServiceDiscovery serviceDiscovery;

    private static AtomicInteger atomicInteger = new AtomicInteger();


    public RpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    private Object start(final RpcRequest rpcRequest) throws Exception {

        final RpcResponse rpcResponse = new RpcResponse();
        CountDownLatch completedSignal = new CountDownLatch(1);
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                            socketChannel.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                            socketChannel.pipeline().addLast(new ClientHandler(rpcRequest, rpcResponse));

                        }
                    });

            getHostAndPort();

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("Connection established");
                    } else {
                        System.out.println("Connection attempt failed");
                        channelFuture.cause().printStackTrace();
                    }
                }
            }).sync();

        } finally {
            eventLoopGroup.shutdownGracefully();
        }

        return rpcResponse;
    }

    public Object create(Class<?> clazz, Method method, Object[] parameters){
        try {
            RpcRequest request = new RpcRequest(
                    atomicInteger.incrementAndGet(),
                    method.getName(),
                    clazz.getName(),
                    parameters,
                    method.getParameterTypes());
            return start(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getHostAndPort(){
        String hostAndPort = serviceDiscovery.discover();
        String[] result = hostAndPort.split(":");
        this.host = result[0];
        this.port = Integer.parseInt(result[1]);
    }



    public static void main(String[] args) throws Exception {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ClientConfig.xml");
        ClientProxy clientProxy = (ClientProxy) applicationContext.getBean("clientProxy");

        IHelloService iHelloService = clientProxy.newProxy(IHelloService.class);
        for(int i = 0;i < 100;i++) {
            System.out.println(iHelloService.say("liyu"));
        }
//        iHelloService.say("liyu");


    }
}
