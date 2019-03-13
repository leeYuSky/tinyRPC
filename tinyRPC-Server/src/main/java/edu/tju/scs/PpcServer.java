package edu.tju.scs;

import edu.tju.scs.annotation.RPCService;
import edu.tju.scs.codec.RpcDecoder;
import edu.tju.scs.codec.RpcEncoder;
import edu.tju.scs.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 00:17 18/5/11.
 */
public class PpcServer implements ApplicationContextAware, InitializingBean{


    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    private final String host;

    private final int port;

    private ServiceRegistry serviceRegistry;

    public PpcServer(String host, int port, ServiceRegistry serviceRegistry) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = serviceRegistry;
    }

    public void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                            socketChannel.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                            socketChannel.pipeline().addLast(new ServerHandler(handlerMap));

                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(host,port)).sync();

            serviceRegistry.init();
            serviceRegistry.registry(host,port);

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ServerConfig.xml");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans =  applicationContext.getBeansWithAnnotation(RPCService.class);

        if(beans != null && beans.size() > 0){
            for(Object obj : beans.values()){
                String service = obj.getClass().getAnnotation(RPCService.class).Interface();
                handlerMap.put(service,obj);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
