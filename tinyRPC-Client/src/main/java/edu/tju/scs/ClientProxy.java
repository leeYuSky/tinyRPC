package edu.tju.scs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 23:16 18/5/15.
 */
public class ClientProxy implements InvocationHandler {

    private RpcClient proxyObject;
    private Class<?> targetClass;

    public ClientProxy(RpcClient proxyObject) {
        this.proxyObject = proxyObject;
    }


    public <T> T newProxy(Class<?> clazz){
        this.targetClass = clazz;
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcResponse result = (RpcResponse) proxyObject.create(targetClass,method,args);
        return result.getResult();
    }
}
