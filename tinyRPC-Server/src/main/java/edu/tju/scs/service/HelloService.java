package edu.tju.scs.service;

import edu.tju.scs.annotation.RPCService;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 00:56 18/5/12.
 */
@RPCService(Interface = "edu.tju.scs.service.IHelloService")
public class HelloService implements IHelloService {
    public String say(String name) {
        return "Hello world " + name;
    }
}
