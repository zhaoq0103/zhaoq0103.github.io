#网络相关：

# 一
协议的三要素；
语法，语义，顺序



去中心化还是中心化待定：
Mastodon
tribler


MAC层的问题：原则上交换机是二层设备，处理MAC层问题，但是也可以带路由功能，有三层的路由能力
    1. 环路问题：STP（也有缺陷）
    2. 安全隔离：VLAN


    16.158.165.91/22

    16.158.164.0网络
    16.158.167.255广播地址
    网络号不变， 主机号全0时为网络号，主机号全1时为广播地址
    x.x.x.xx  组播地址
    255.255.252.0 掩码

    MTU:

    
    TCP sendto() 数据无大小限制，数据流
    UDP send() 数据有大小限制

    [!image] "./WeChatWorkScreenshot_71f6d7f1-2596-4e68-9d58-6e1fadba98d1.png"

ping traceroute 判断网络问题， 细节需要研究一下。
协议都是定长header+变长body

arp, ICMP是网络层的协议

网络拓扑自动发现的方法：
    基于SNMP的网络拓扑发现方法；
    基于通用协议的网络拓扑发现方法；
    基于路由协议的网络拓扑发现方法


1. 创建一个跳转链接，这个是跳转的基础；
2. 制作一个落地页并镶嵌商跳转链接；
3. 把准备好的落地页通过巨量星图提交审核，审核通过后就获得了一个转化组件


路由：
    静态路由:
    动态路由:
        最短路径常用的有两种方法，一种是 Bellman-Ford 算法，一种是 Dijkstra 算法
        链路状态路由 OSPF算法 -- IGP 数据中心内部 Dijkstra 算法
        矢量路由             -- BGP( eBGP, iBGP) 外部路由算法 Bellman-Ford 算法


ip rule add from 192.168.1.0/24 table 10 
ip rule add from 192.168.2.0/24 table 20
ip route add default scope global nexthop via 100.100.100.1 weight 1 nexthop via 200.200.200.1 weight 2



$ ip route list table main 
60.190.27.189/30 dev eth3  proto kernel  scope link  src 60.190.27.190
183.134.188.1/32 dev eth2  proto kernel  scope link  src 183.134.189.34
192.168.1.0/24 dev eth1  proto kernel  scope link  src 192.168.1.1
127.0.0.0/8 dev lo  scope link
default via 183.134.188.1/32 dev eth2



ip route add default via 60.190.27.189 dev eth3 table chao
ip route flush cache


TCP协议要再理解几遍：
    TCP状态；
    TCP的滑动窗口和拥塞窗口算法
