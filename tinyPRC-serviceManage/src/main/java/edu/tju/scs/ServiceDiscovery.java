package edu.tju.scs;

import java.util.List;
import java.util.Random;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 23:57 18/5/10.
 */
public class ServiceDiscovery {


    private ZookeeperManager zookeeperManager;


    public ServiceDiscovery(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }

    public String discover(){
        zookeeperManager.connect();
        List<String> services =  zookeeperManager.listChildrenNode(ZookeeperManager.DEFAULT_REGISTRY_PATH);
        int size = services.size();
        int result = new Random().nextInt(size);
        return services.get(result);
    }

}
