
MAC OS 查看路由配置
netstat -nr 

命令结果：
Routing tables

Internet:
Destination        Gateway            Flags               Netif Expire
default            192.168.10.1       UGScg                 en1       
default            link#21            UCSIg           bridge100      !
default            link#23            UCSIg           bridge101      !



linux 查看路由配置;
ip route ==> ip r 这个简写方式 man ip 有一个简要的说明，没看到详细文档
ip addr  ==> ip a 



linux查看ssh服务状态：
新版本；
systemctl status sshd
# Unit sshd.serveice could not be found

老版本：
service ssh status


二层：MAC层
	二层设备：网桥，交换机等 ，STP解决二层环路风暴（Spanning Tree Protocol）, 转发表
	二层网络的隔离：VLAN
	交换机与交换机之间用trunk口连接


三层：IP层
	三层设备：路由，网关 ,路由表

	路由协议：
		静态路由与动态路由

	静态路由：
		 路由规则：目标网络 + via 下一跳网关 + dev 端口 (p2p网络某些情况路由配置可以省略下一跳 via )
		 ip route add 10.176.48.0/20 via 10.173.32.1 dev eth0

		 还可以配置策略路由：
		 	ip rule add from 192.168.1.0/24 table 10 
			ip rule add from 192.168.2.0/24 table 20
			ip route add default scope global nexthop via 100.100.100.1 weight 1 nexthop via 200.200.200.1 weight 2

	动态路由：
		1.距离矢量路由算法 Bellman-Ford (好消息快，坏消息慢；数据量大；)
			1.1 如 RIP，适用于小型网络
			1.1 BGP 外网路由协议， eBGP（边界路由器）, iBGP (路径矢量路由协议，距离矢量升级版，用于导入AS内部来寻找边界路由器)

		2. 链路状态路由算法 Dijkstra
		   2.1 OSPF 开放式最短路由优先 用于数据中心内部 （IGP)


四层：传输层
	TCP UDP 
	TCP BBR 拥塞算法


数据中心DC：
	数据中心的TOR交换机被称为 接入层交换机， 多个接入层交换机接入汇聚层交换机
	网卡绑定技术（bond) LACP协议， STP协议去环
	交换机堆叠技术（双活）

	汇聚层集群里面，服务器之间通过二层互通，
	这个区域常称为一个 POD（Point Of Delivery），有时候也称为一个可用区（Available Zone）。

	多个可用区之间用核心交换机连接。


	数据中心，核心交换机以下可能全部是二层互联（大二层），STP不能扩大横向流量能力，需要引入TRILL
	（Transparent Interconnection of Lots of Link），即多链接透明互联协议
	（根据二层MAC进行路由转发，模拟路由器的功能）

	运行 TRILL 协议的交换机称为 RBridge，是具有路由转发特性的网桥设备，
	RBridge之间使用 OSPF 路由协议

	数据中心结构：
	边界路由器 -> 核心层 -> 可用区：
		边界路由器 -> 核心层 -> （汇聚层 -> 接入层 -> hosts） 形成南北流量

	叶脊网络（Spine/Leaf），解决东西流量问题（内部数据传输）
		脊交换机不再处理南北流量，通过与叶交换机并行的交换机，接到边界路由器发出去。



VPN
  1. IP Sec （可以自行搭建）
  2. MPLS , 需要标签交换路由器支持LSR  LDP，标签动态生成协议 （需要从运营商购买）
  			CE, PE, P, 外层标签，内层标签


虚拟机与宿主机的网络连接:
	1. host-only 
	2. 桥接和NAT
	
	 把网关设置在虚拟网桥上
	 iptables -t nat -A POSTROUTING -o ethX -j MASQUERADE
	 直接在物理网卡 ethX 上进行 NAT，


	 所有从这个网卡出去的包都 NAT 成这个网卡的地址。
	 通过设置 net.ipv4.ip_forward = 1 进行包转发


SDN 软件定义网络 要多看几次
https://time.geekbang.org/column/article/10755


网络安全控制在云中主要是 利用iptables设置访问规则（安全组等）
NAT:
	源地址转换 (Snat)：iptables -t nat -A -s 私网 IP -j Snat --to-source 外网 IP
	目的地址转换 (Dnat)：iptables -t nat -A -PREROUTING -d 外网 IP -j Dnat --to-destination 私网 IP


QoS控制： 流量控制主要通过队列进行
	入口与出口控制 TC （iproute2 包中的 tc 命令）
	Ingress policy
		ovs-vsctl set Interface tap0 ingress_policing_rate=100000
		ovs-vsctl set Interface tap0 ingress_policing_burst=10000

	Egress shaping
		ovs-vsctl set port first_br qos=@newqos -- --id=@newqos create qos type=linux-htb other-config:max-rate=10000000 queues=0=@q0,1=@q1,2=@q2 -- --id=@q0 create queue other-config:min-rate=3000000 other-config:max-rate=10000000 -- --id=@q1 create queue other-config:min-rate=1000000 other-config:max-rate=10000000 -- --id=@q2 create queue other-config:min-rate=6000000 other-config:max-rate=10000000

	分层令牌桶规则（HTB， Hierarchical Token Bucket）


云网络互通 与 云网络隔离
	物理网络设备组成的网络称为 Underlay 网络
	虚拟机和云中的这些技术组成的网络称为 overlay 网络


	GRE :原IP层上再增加协议头（GRE协议）在三层实现，有一些局限性， 点对点
	VXLAN:原MAC层外增加协议头（VXLAN header），在二层实现。
				VTEP 解析VXLAN协议， 组播。 通过 GMP协议加入组播组


Docker 网络
	flennel
	Calico: 重要组件
		Felix 
		BGP Speaker, BGP Route Reflector
		iptables 安全组
		IPIP 模式 - 跨网段互通（隧道）


不同的网段间才需要配置和使用路由。






# sudo docker exec -it slip curl -k  https://whatismyip.com | grep -E -o '([0-9]{1,3}\.){3}[0-9]{1,3}'

影响到网络连通性的可能因素：
	IP转发 ip_forward
	路由设置 ip route
	防火墙 iptables

语法：四表五链
iptables [-t table] COMMAND [chain]  CRETIRIA  -j ACTION

	[table] : raw,mangle,nat,filter  ... 4表, 默认filter
	COMMAND: -A,-D,-I,-L,-F,-R,-Z,-P ... -A append -I insert
	[chain] : PREROUTING,INPUT,FORWARD,OUTPUT,POSTROUTING   ... 等5个
	CRETIRIA: -s -d -i -o ...
	ACTION: ACCEPT,DROP,MASQUERADE,REJECT,REDIRECT,SNAT,DNAT,LOG ...


在第6条的位置插入一个规则，规则是接受来自10.0.60.21的tcp对目标端口3306的访问。
	iptables -I INPUT 6 -s 10.0.60.21 -p tcp --dport 3306 -j ACCEPT

	-A POSTROUTING -s 172.17.0.0/16 ! -o docker0 -j MASQUERADE 
	感叹号（!）表示逻辑“非”操作符，它用于对所匹配的条件取反。在这种情况下，感叹号将应用于 -o docker0 条件，使得规则中指定的动作 MASQUERADE 只有在数据包不匹配 -o docker0 条件时才会生效

https://pro-yeahgo.oss-cn-shenzhen.aliyuncs.com/goods/base/rc-upload-1726104089285-15%E6%B7%B1%E5%9C%B3%E5%89%8D%E6%B5%B7%E6%B1%87%E8%83%BD%E7%A7%91%E6%8A%80%E4%BA%A7%E4%B8%9A%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8_20240912100859.pdf



解释一下这个iptables规则：
*filter
:INPUT ACCEPT [0:0]
:FORWARD DROP [0:0]
:OUTPUT ACCEPT [0:0]
:DOCKER - [0:0]
:DOCKER-ISOLATION-STAGE-1 - [0:0]
:DOCKER-ISOLATION-STAGE-2 - [0:0]
:DOCKER-USER - [0:0]
-A FORWARD -j DOCKER-USER
-A FORWARD -j DOCKER-ISOLATION-STAGE-1
-A FORWARD -o br-763bda580b1c -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT
-A FORWARD -o br-763bda580b1c -j DOCKER
-A FORWARD -i br-763bda580b1c ! -o br-763bda580b1c -j ACCEPT
-A FORWARD -i br-763bda580b1c -o br-763bda580b1c -j ACCEPT
-A FORWARD -o docker0 -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT
-A FORWARD -o docker0 -j DOCKER
-A FORWARD -i docker0 ! -o docker0 -j ACCEPT
-A FORWARD -i docker0 -o docker0 -j ACCEPT
-A DOCKER-ISOLATION-STAGE-1 -i br-763bda580b1c ! -o br-763bda580b1c -j DOCKER-ISOLATION-STAGE-2
-A DOCKER-ISOLATION-STAGE-1 -i docker0 ! -o docker0 -j DOCKER-ISOLATION-STAGE-2
-A DOCKER-ISOLATION-STAGE-1 -j RETURN
-A DOCKER-ISOLATION-STAGE-2 -o br-763bda580b1c -j DROP
-A DOCKER-ISOLATION-STAGE-2 -o docker0 -j DROP
-A DOCKER-ISOLATION-STAGE-2 -j RETURN
-A DOCKER-USER -j RETURN



1. :INPUT ACCEPT [0:0]、:FORWARD DROP [0:0]、:OUTPUT ACCEPT [0:0]：
这些是设置默认策略的指令，分别表示 INPUT、FORWARD 和 OUTPUT 链的默认动作。即接受来自本地、拒绝转发以及接受出站流量。

2. :DOCKER - [0:0]、:DOCKER-ISOLATION-STAGE-1 - [0:0]、:DOCKER-ISOLATION-STAGE-2 - [0:0]、:DOCKER-USER - [0:0]：
这些是用户定义的链，用于处理与 Docker 相关的数据包。

3. -A FORWARD -j DOCKER-USER、-A FORWARD -j DOCKER-ISOLATION-STAGE-1：
将 FORWARD 链中的数据包跳转到相应的自定义链中处理。

4. -A FORWARD -o br-763bda580b1c -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT、-A FORWARD -o br-763bda580b1c -j DOCKER：
针对目标输出接口为 br-763bda580b1c 的数据包，执行不同的操作。

5. -A DOCKER-ISOLATION-STAGE-1 -i br-763bda580b1c ! -o br-763bda580b1c -j DOCKER-ISOLATION-STAGE-2：
当输入接口是 br-763bda580b1c 且输出接口不是 br-763bda580b1c 时，进入 DOCKER-ISOLATION-STAGE-2 进行处理。


6. -A DOCKER-ISOLATION-STAGE-2 -o br-763bda580b1c -j DROP：
当数据包要流出到网络接口 br-763bda580b1c 时，将数据包丢弃（DROP），即阻止数据包从 Docker 容器流向指定的网络接口。

7.-A DOCKER-ISOLATION-STAGE-2 -o docker0 -j DROP：
类似地，当数据包要流出到 docker0 网络接口时，也将数据包丢弃，以增强安全性或限制访问。

























