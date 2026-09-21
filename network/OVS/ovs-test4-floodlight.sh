#!/bin/bash

/***
	有什么想说的吗
***/

#================================================test 05 controller================================================================
#实验5
#按提示安装JAVA
#sudo apt install openjdk-8-jre-headless -- 单纯安装这个不行

openjdk version "1.8.0_352"
OpenJDK Runtime Environment (build 1.8.0_352-8u352-ga-1~22.04-b08)
OpenJDK 64-Bit Server VM (build 25.352-b08, mixed mode)


#sudo apt install openjdk-8-jdk

openjdk version "1.8.0_352"
OpenJDK Runtime Environment (build 1.8.0_352-8u352-ga-1~22.04-b08)
OpenJDK 64-Bit Server VM (build 25.352-b08, mixed mode)

git clone https://github.com/floodlight/floodlight.git
cd floodlight/
git submodule init
git submodule update
ant

## 注意 floodlight的配置文件中的端口设置
sudo nohup java -jar ./floodlight.jar -cf ./floodlight.properties >floodlight.log 2>&1 &


#远程库分支情况
git remote -v 
#拉取到本地分支
git checkout -b v1.2 origin/v1.2



<dependency>
  <groupId>javax.xml.bind</groupId>
  <artifactId>jaxb-api</artifactId>
  <version>2.3.0</version>
</dependency>
<dependency>
  <groupId>com.sun.xml.bind</groupId>
  <artifactId>jaxb-core</artifactId>
  <version>2.3.0</version>
</dependency>
<dependency>
  <groupId>com.sun.xml.bind</groupId>
  <artifactId>jaxb-impl</artifactId>
  <version>2.3.0</version>
</dependency>


#floodlight 没有编译成功，直接使用docker floodlight, pierrecdn/floodlight  可用, 
也可以从这个docker中把floodlight.jar 和 floodlight.properties 拷贝到宿主机中使用



#sudo docker run --privileged=true --net none --name $i -d ${imagename}
#sudo docker run -itd -p 宿主机ip:宿主机端口:容器端口 --name 容器名 镜像名 /bin/bash

#可用，配置成功
#sudo docker pull glefevre/floodlight && sudo  docker run -p  8080:8080 -p 6653:6653 --name flood2 glefevre/floodlight
sudo docker run  --network my-docker-net -p  8080:8080 -p  6653:6653  --name flood -d  pierrecdn/floodlight  


ss -tuln # 查询 6653接口监听成功
ss -lpi state all src 192.168.0.24

# 可以在宿主机上访问到,宿主机的宿主机也可以访问到，通过2个网桥
http://192.168.100.110:8080/ui/index.html
http://192.168.64.2:8080/ui/index.html  == 可以直接在外部访问，通过端口映射到docker实例


增加一个网桥：
sudo ovs-vsctl add-br flood_br

启动3个docker instance:
sudo docker start xxx

sudo ./pipework flood_br pcip100 192.168.100.100/24
sudo ./pipework flood_br pcip101 192.168.100.101/24
sudo ./pipework flood_br pcip102 192.168.100.102/24

sudo ./pipework flood_br flood 192.168.100.110/24

pcs=("h100" "h101" "h102")
for i in ${pcs[@]}
do
	sudo docker exec -it $i bash -c "echo '#
	::1	localhost ip6-localhost ip6-loopback
	fe00::0	ip6-localnet
	ff00::0	ip6-mcastprefix
	ff02::1	ip6-allnodes
	ff02::2	ip6-allrouters

	192.168.100.100 h100
	192.168.100.101 h101
	192.168.100.102 h102
		
	#' >/etc/hosts"
done

# 需要打通docker自建的网络和OVS的网桥（参考chatGPT4)
# !image(./docker-default-bridge-connect-ovs-bridge.jpg)

sudo docker network --help
sudo docker network ls
sudo docker network create --driver bridge my-docker-net
sudo docker network inspect my-docker-net
sudo docker stop flood
sudo docker rm flood
cat /etc/docker/daemon.json ## {"bridge":"my-docker-net"}
sudo vim /etc/docker/daemon.json 

sudo docker run  --network my-docker-net -p  8080:8080 -p  6653:6653  --name flood -d  pierrecdn/floodlight  
sudo docker ps 


sudo ovs-vsctl add-port flood_br patch-br0-br1 -- set interface patch-br0-br1 type=patch options:peer=patch-br1-br0
sudo ovs-vsctl add-port my-docker-net patch-br1-br0 -- set interface patch-br1-br0 type=patch options:peer=patch-br0-br1


sudo docker exec -it pcip100 ip route add 172.18.0.1/16 via 172.18.0.1 dev  br-01792b5a5db1
sudo docker exec -it pcip100 ip route add default via 140.252.13.33 dev eth1




链接网桥和虚拟机宿主机网卡
sudo ip link add name hostside type veth peer name floodbrside

sudo ip addr add 192.168.100.106/24 dev hostside
sudo ip link set hostside up

sudo ovs-vsctl add-port flood_br floodbrside 
sudo ip link set floodbrside up


# sudo ovs-vsctl add-port flood_br veth106 -- set Interface veth106 type=internal
# ## -- set Interface veth106 type=internal: 在添加端口后，使用 -- set 选项来配置该端口的属性

sudo ovs-vsctl del-controller flood_br 
sudo ovs-vsctl get-controller flood_br 
sudo ovs-vsctl set-controller flood_br tcp:192.168.100.110:6653


sudo ovs-ofctl show flood_br
sudo ovs-ofctl dump-flows flood_br


###    反复用docker试验，都不成功， 决定换个思路，直接用虚拟机来测试看看
###    找到问题原因了， 是flood端口问题， flood的端口是在配置文件中配置的，不一定是6633

# 把docker都停了
sudo docker stop $(sudo docker ps -q)

#设置IP和子网
sudo ip add del 192.168.83.3/24 dev enp0s1
sudo ip add add 192.168.83.4/24 dev enp0s1

#设置路由
sudo ip route add default via 172.16.83.1


sudo /sbin/iptables -P FORWARD ACCEPT
sudo bash -c "echo 1 > /proc/sys/net/ipv4/ip_forward"
sudo sysctl -p 
sudo /sbin/iptables -P FORWARD ACCEPT



curl http://192.168.100.108:8080/wm/core/controller/switches/json
curl http://192.168.100.108:8080/wm/core/version/json

安全组是在每台虚拟机网卡前面增加一个虚拟网桥，ip规则配置在这个网桥上并使用一定的技术手段同步到虚拟机中，
从而控制安全组内的机器。
所以把一台机器增加到特定安全组，就是把这个机器出口连接到安全组网桥上。

flood_br: 46:e4:d3:30:cc:4e
flood_ctl: 	00:00:46:e4:d3:30:cc:4e 

pc-ip100: 7a:20:ce:5f:4c:3b
pc-ip101: 36:26:74:a9:85:60
pc-ip102: a6:47:ab:34:b0:52


sudo ovs-ofctl show  flood_br
OFPT_FEATURES_REPLY (xid=0x2): dpid:000046e4d330cc4e
n_tables:254, n_buffers:0
capabilities: FLOW_STATS TABLE_STATS PORT_STATS QUEUE_STATS ARP_MATCH_IP
actions: output enqueue set_vlan_vid set_vlan_pcp strip_vlan mod_dl_src mod_dl_dst mod_nw_src mod_nw_dst mod_nw_tos mod_tp_src mod_tp_dst

 1(veth1pl13607): addr:1a:35:ef:48:fa:a3
     config:     0
     state:      0
     current:    10GB-FD COPPER
     speed: 10000 Mbps now, 0 Mbps max
 2(veth1pl13693): addr:86:61:04:d1:63:83
     config:     0
     state:      0
     current:    10GB-FD COPPER
     speed: 10000 Mbps now, 0 Mbps max
 3(veth1pl13775): addr:82:87:24:d0:12:ea
     config:     0
     state:      0
     current:    10GB-FD COPPER
     speed: 10000 Mbps now, 0 Mbps max
 LOCAL(flood_br): addr:46:e4:d3:30:cc:4e
     config:     0
     state:      0
     speed: 0 Mbps now, 0 Mbps max
OFPT_GET_CONFIG_REPLY (xid=0x4): frags=normal miss_send_len=0


flood container没有连上来

55: eth0@if56: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever

host：       
56: veth628eb7d@if55: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 6a:34:cc:08:00:83 brd ff:ff:ff:ff:ff:ff link-netnsid 9
    inet6 fe80::6834:ccff:fe08:83/64 scope link 
       valid_lft forever preferred_lft forever




sudo ovs-vsctl show
sudo ovs-vsctl list-br
sudo ovs-vsctl list-ports BRIDGE 
sudo ovs-vsctl list-ifaces BRIDGE


    Bridge flood_br
        Controller "tcp:192.168.64.2:6633"
        Port floodbr_veth1
            Interface floodbr_veth1
        Port flood_br
            Interface flood_br
                type: internal
        Port veth1pl48221
            Interface veth1pl48221
        Port veth1pl48304
            Interface veth1pl48304
        Port veth1pl23659
            Interface veth1pl23659

sudo ovs-vsctl set-controller flood_br tcp:192.168.100.108:6633
sudo ovs-vsctl get-controller flood_br



mininet 虚拟机的用户名和密码都是 mininet

可以设置成功，但是可能版本不匹配，配置后所有网络都不通了
curl -d '{"switch": "00:00:00:00:00:00:00:01", "name":"static-flow1", "cookie":"0", "priority":"32768", "src-mac":"da:ff:62:b2:80:bb","active":"true", "actions":"output=3"}'  http://192.168.56.101:8080/wm/staticflowpusher/json

curl -d '{"switch": "00:00:00:00:00:00:00:01", "name":"static-flow2", "cookie":"0", "priority":"32768", "src-mac":"16:9e:a2:7c:55:6a","active":"true", "actions":"output=1"}'  http://192.168.56.101:8080/wm/staticflowpusher/json


可以设置成功，能正常匹配规则
curl -d '{"switch": "00:00:00:00:00:00:00:01", "name":"static-flow1", "cookie":"0", "priority":"32768", "in_port":"1","active":"true", "actions":"output=3"}'  http://192.168.56.101:8080/wm/staticflowpusher/json

curl -d '{"switch": "00:00:00:00:00:00:00:01", "name":"static-flow2", "cookie":"0", "priority":"32768", "in_port":"3","active":"true", "actions":"output=1"}'  http://192.168.56.101:8080/wm/staticflowpusher/json



## 重点参考文档
## FloodLight Rest API 版本说明文档
https://floodlight.atlassian.net/wiki/spaces/floodlightcontroller/pages/1343518/Static+Entry+Pusher+API#StaticEntryPusherAPI-AddingaFlow


curl http://192.168.56.101:8080/wm/staticflowentrypusher/list/all/json  404 < 1.0
curl http://192.168.56.101:8080/wm/staticflowpusher/list/all/json	1.0~1.2  表明我用的是这个版本
curl http://192.168.56.101:8080/wm/staticentrypusher/list/all/json	master



curl http://192.168.56.101:8080/wm/core/switch/1/flow/json  OK
curl http://192.168.56.101:8080/wm/core/controller/switches/json OK


读取流表:
curl http://192.168.56.101:8080/wm/core/switch/1/flow/json  OK
读取流表: 这个看起来更正常一些
curl http://192.168.56.101:8080/wm/staticflowpusher/list/all/json  OK

删除流表:
1.删除所有流表 OK
curl http://192.168.56.101:8080/wm/staticflowpusher/clear/all/json 
curl http://192.168.56.101:8080/wm/staticflowpusher/clear/00:00:00:00:00:00:00:01/json OK


2.删除单条流表
curl -X DELETE -d '{"name":"static-flow1"}' http://192.168.56.101:8080/wm/staticflowpusher/json  OK
curl -X DELETE -d '{"name":"static-flow2"}' http://192.168.56.101:8080/wm/staticflowpusher/json	 OK








output=<bridge_port_num>

# 这个规则是对的， 只需要注意dl_src是host的MAC,不是交换机的 in_port=1;nw_src=192.168.100.100;nw_dst=192.168.100.101 这些还不知道如何用

sudo ovs-ofctl -O OpenFlow13 add-flow flood_br "idle_timeout=1000,priority=17,dl_src=7e:aa:fa:e5:49:cb, actions=output:3"
sudo ovs-ofctl -O OpenFlow13 add-flow flood_br "idle_timeout=1000,priority=17,dl_src=fe:37:27:65:b6:83, actions=output:1"



## src-mac 是host的mac, 不是交换机的interface， 上面是直接在交换机上操作， 这里是在控制中心操作   404
curl -d '{"switch": "00:00:46:e4:d3:30:cc:4e", "name":"static-flow1", "cookie":"0", "priority":"32768", "src-mac":"2e:bb:27:7f:93:98","active":"true", "actions":"output=3"}'  http://192.168.100.108:8080/wm/staticflowentrypusher/json
curl -d '{"switch": "00:00:46:e4:d3:30:cc:4e", "name":"static-flow2", "cookie":"0", "priority":"32768", "src-mac":"62:90:33:92:4d:ec","active":"true", "actions":"output=1"}'  http://192.168.100.108:8080/wm/staticflowentrypusher/json


## 流表规则通过 controller 下发没有成功， 报404
h2 mac:
66:5f24:4c:45:94

curl -d '{"switch": "00:00:00:00:00:00:00:01", "name":"static-flow1", "cookie":"0", "priority":"32768", "src-mac":"06:1a:47:5b:9b:37","active":"true", "actions":"output=3"}'  http://192.168.56.101:8080/wm/staticflowentrypusher/json
curl -d '{"switch": "00:00:00:00:00:00:00:01", "name":"static-flow2", "cookie":"0", "priority":"32768", "src-mac":"aa:97:70:f4:21:05","active":"true", "actions":"output=1"}'  http://192.168.56.101:8080/wm/staticflowentrypusher/json





# 清除所有规则
# curl http://192.168.64.2:8080/wm/staticflowentrypusher/clear/<device-mac>/json
curl http://16.158.166.150:8080/wm/staticflowentrypusher/clear/00:00:2a:96:0e:c7:85:49/json

# 将正确的mac导向正确的port
curl -d '{"switch": "00:00:2a:96:0e:c7:85:49", "name":"static-flow1", "cookie":"0", "priority":"32768", "dst-mac":"52:54:00:9b:d5:11","active":"true", "actions":"output=10"}'  http://192.168.64.2:8080/wm/staticflowentrypusher/json
curl -d '{"switch": "00:00:2a:96:0e:c7:85:49", "name":"static-flow2", "cookie":"0", "priority":"32768", "dst-mac":"52:54:00:9b:d5:33","active":"true", "actions":"output=11"}'  http://16.158.166.150:8080/wm/staticflowentrypusher/json
curl -d '{"switch": "00:00:2a:96:0e:c7:85:49", "name":"static-flow3", "cookie":"0", "priority":"32768", "dst-mac":"52:54:00:9b:d5:77","active":"true", "actions":"output=12"}'  http://16.158.166.150:8080/wm/staticflowentrypusher/json


# 用REST API将Instance03的包转发给Instance02
curl -d '{"switch": "00:00:2a:96:0e:c7:85:49", "name":"static-flow3", "cookie":"0", "priority":"32768", "dst-mac":"52:54:00:9b:d5:77","active":"true", "actions":"output=11"}'  http://16.158.166.150:8080/wm/staticflowentrypusher/json


## 控制器还没有连接上
## 连接上后，下发流表也不成功， 先放在这儿吧， 知道控制中心是怎么回事就可以了。。








``