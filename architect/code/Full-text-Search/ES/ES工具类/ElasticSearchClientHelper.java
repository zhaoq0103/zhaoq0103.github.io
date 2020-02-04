package com.sohu.auto.db.es.core.es.client;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElasticSearchClientHelper {
    private Map<String, Client> clientMap = new ConcurrentHashMap<>();

    private Map<String, Integer> ips = new HashMap<>(); // hostname port

    private String clusterName = "auto-es-pro";

    private boolean clientTransportSniff;

    public String getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    private String inetAddress;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public boolean isClientTransportSniff() {
        return clientTransportSniff;
    }

    public void setClientTransportSniff(boolean clientTransportSniff) {
        this.clientTransportSniff = clientTransportSniff;
    }

    private ElasticSearchClientHelper() {
        init();
    }
    public static final ElasticSearchClientHelper getInstance() {
        return ClientHolder.INSTANCE;
    }

    private static class ClientHolder {
        private static final ElasticSearchClientHelper INSTANCE = new ElasticSearchClientHelper();
    }

    private void init() {
        try {
            // 读取服务配置
            Configuration config = new PropertiesConfiguration("conf/es.properties");
            this.inetAddress = config.getString("es.transport.address");
            this.clientTransportSniff = config.getBoolean("es.client.transport.sniff");
            this.clusterName = config.getString("es.cluster.name");
            // 1.设定Settings
            // 设定集群名称
            Settings settings = Settings.builder().put("cluster.name", clusterName)
                    // 是否自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                    .put("client.transport.sniff", clientTransportSniff).build();
            // 2. 创建访问ES服务器的客户端
            TransportClient client = new PreBuiltTransportClient(settings);
            TransportAddress[] geTransportAddress = getAllAddress();
            if (geTransportAddress != null && geTransportAddress.length > 0) {
                for (int i = 0; i < geTransportAddress.length; i++) {
                    client.addTransportAddress(geTransportAddress[i]);
                }
            }
            clientMap.put(settings.get("cluster.name"),client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TransportAddress[] getAllAddress() throws UnknownHostException {
        if (inetAddress == null || inetAddress.length() <= 10) {
            return null;
        }
        String[] split = inetAddress.split(" ");
        List<TransportAddress> list = new ArrayList<>();

        for (String string : split) {
            if (StringUtils.isNotEmpty(string) && string.length() >= 10 && string.contains(":")) {
                String[] ip = string.split(":");
                if (ip.length == 2 && ip[0].matches("(\\d{1,3}\\.){3}\\d{1,3}") && ip[1].matches("\\d{1,6}")) {
                    list.add(new TransportAddress(InetAddress.getByName(ip[0]), Integer.valueOf(ip[1])));
                }
            }
        }
        TransportAddress[] addresses = new TransportAddress[list.size()];
        for (int i = 0; i < list.size(); i++) {
            addresses[i] = list.get(i);
        }
        return addresses;

    }


    public Client getClient() {
        return getClient(clusterName);
    }

    private Client getClient(String clusterName) {
        return clientMap.get(clusterName);
    }
}

