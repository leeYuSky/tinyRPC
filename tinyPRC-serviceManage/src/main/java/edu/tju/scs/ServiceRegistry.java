package edu.tju.scs;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 23:45 18/5/10.
 */
public class ServiceRegistry {

    private ZookeeperManager zookeeperManager;

    public ServiceRegistry(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }

    public void registry(String host, int port){
        String service = host + ":" + port;
        zookeeperManager.createNode(service);
    }

    public void init(){
        zookeeperManager.connect();
        zookeeperManager.deleteNode(ZookeeperManager.DEFAULT_REGISTRY_PATH);
    }
}
