package edu.tju.scs;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: liyuze
 * @Description: zookeeper 管理器
 * @Date: Created in 20:23 18/5/9.
 */
public class ZookeeperManager {

    private String host;
    private int port;

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2181;
    private static final int DEFAULT_TIMEOUT = 5000;
    public static final String DEFAULT_REGISTRY_PATH = "/tinyRPC";

    private ZooKeeper zooKeeper;
    private String connectString;

    public ZookeeperManager(){
        this(DEFAULT_HOST,DEFAULT_PORT);
    }

    public ZookeeperManager(String host){
        this(host,DEFAULT_PORT);
    }

    public ZookeeperManager(String host, int port){
        this.host = host;
        this.port = port;
        this.connectString = host + ":" + port;
    }

    public void connect(){
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
             zooKeeper = new ZooKeeper(connectString, DEFAULT_TIMEOUT, new Watcher() {
                 @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println(watchedEvent);
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            System.out.println("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createNode(String path){
        try {

            // 先判断根节点是否存在, 若根节点不存在, 则不能创建子节点。
            if(!isExistNode(DEFAULT_REGISTRY_PATH)){
                zooKeeper.create(DEFAULT_REGISTRY_PATH,
                        "hello world".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            if(!isExistNode(DEFAULT_REGISTRY_PATH + "/" + path)){
                zooKeeper.create(DEFAULT_REGISTRY_PATH + "/" + path,
                        "hello world".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void deleteNode(String path){

        try {
            if (isExistNode(path)) {
                List<String> childern = listChildrenNode(path);
                for (String s : childern) {
                    deleteNode(path + "/" + s);
                }
                // 《从PAXOS到ZOOKEEPER分布式一致性原理与实践》p111
                //  -1 代表了基于数据的最新版本做操作
                zooKeeper.delete(path, -1);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public List<String> listChildrenNode(String path){
        List<String> children = null;
        try {
             children =  zooKeeper.getChildren(path, new Watcher() {
                 @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("listChildrenNode: " + watchedEvent);
                }
             });
            System.out.println(children);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return children;
    }


    public boolean isExistNode(String path){
        Stat stat = null;
        try {
            stat = zooKeeper.exists(path, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("isExistNode: " + watchedEvent);
                }
            });
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return stat == null ? false : true;
    }

    public static void main(String[] args){
        ZookeeperManager ZookeeperManager = new ZookeeperManager("47.94.243.191");
        ZookeeperManager.connect();
//        ZookeeperManager.deleteNode(DEFAULT_REGISTRY_PATH);
        ZookeeperManager.createNode("127.0.0.1");
//        ZookeeperManager.createNode("127.0.0.2");
//        ZookeeperManager.createNode("127.0.0.2/123");
//        ZookeeperManager.listChildrenNode(DEFAULT_REGISTRY_PATH);
    }

}
