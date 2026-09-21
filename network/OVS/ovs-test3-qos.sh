#!/bin/bash


: "
# ip link show enp0s1
# tc qdisc show dev enp0s1

# 无类别排队规则常用的有3个队列 FIFO SFQ TBF， 
  基于类别排队规则的典型队列有  HTB， 子类可以互相借流量，叶子队列规则也是FIFO SFQ 

sudo docker exec -it test4 netserver
sudo docker exec -it test1 netperf -H 192.168.100.103 -t UDP_STREAM
#sudo docker exec -it test1 bash -c "apt-get update && apt install netperf"


# 用mininet 比较容易测试， 在docker下没有测试成功

mn 
nodes : c0 h1 h12 s1
net:
	h1 h1-eth0:s1-eth1
	h2 h2-eth0:s1-eth2
	s1 lo:  s1-eth1:h1-eth0 s1-eth2:h2-eth0
	c0

sudo ovs-ofctl show s1

	OFPT_FEATURES_REPLY (xid=0x2): dpid:0000000000000001
	n_tables:254, n_buffers:0
	capabilities: FLOW_STATS TABLE_STATS PORT_STATS QUEUE_STATS ARP_MATCH_IP
	actions: output enqueue set_vlan_vid set_vlan_pcp strip_vlan mod_dl_src mod_dl_dst mod_nw_src mod_nw_dst mod_nw_tos mod_tp_src mod_tp_dst
	 1(s1-eth1): addr:56:5a:53:d3:33:4d
	     config:     0
	     state:      0
	     current:    10GB-FD COPPER
	     speed: 10000 Mbps now, 0 Mbps max
	 2(s1-eth2): addr:72:16:e6:26:2e:03
	     config:     0
	     state:      0
	     current:    10GB-FD COPPER
	     speed: 10000 Mbps now, 0 Mbps max
	 LOCAL(s1): addr:d6:1e:9e:95:47:4e
	     config:     PORT_DOWN
	     state:      LINK_DOWN
	     speed: 0 Mbps now, 0 Mbps max
	OFPT_GET_CONFIG_REPLY (xid=0x4): frags=normal miss_send_len=0

sudo ovs-vsctl set port s1-eth1 qos=@newqos -- --id=@newqos create qos type=linux-htb queues=1=@q1 -- --id=@q1 create queue other-config:max-rate=10000

sudo ovs-ofctl add-flow s1 "in_port=2,actions=output:1"
sudo ovs-ofctl add-flow s1 "in_port=2,actions=enqueue:1:0"

限速前：
	 iperf
	*** Iperf: testing TCP bandwidth between h1 and h2 
	*** Results: ['25.8 Gbits/sec', '26.3 Gbits/sec']
限速后：
	 iperf
	*** Iperf: testing TCP bandwidth between h1 and h2 
	*** Results: ['321 Mbits/sec', '341 Mbits/sec']

"

##  创建 QOS 测试 topo 机器：
##  用两个网桥没有成，改用一个网桥试一下


# 机器更换网桥连接
sudo ovs-vsctl del-br vlan_br
sudo ovs-vsctl add-br vlan_br
sudo ip link set vlan_br up


sudo ovs-vsctl del-br qos_br
sudo ovs-vsctl add-br qos_br
sudo ip link set qos_br up


# ips=("100" "101" "102" "103" "104" "105" "106")
sudo docker ps -q | xargs sudo docker stop

ips=("100" "101" "102" "106")

for i in ${ips[@]}
do
	#echo  "h$i""in""out"
	#echo  192.168.100.$i/24

	sudo docker start "h$i" && sudo ./pipework vlan_br -i "h$i""obr" -l "h$i""ibr"  "h$i" 192.168.100.$i/24
	#sudo docker stop "h$i"
	#sudo docker run --privileged=true --name "h$i" -d ${imagename} && sudo ./pipework vlan_br -i "h$i""obr" -l "h$i""ibr"  "h$i" 192.168.100.$i/24

	sudo ovs-vsctl clear Port "h$i""ibr" tag
	#sudo ovs-vsctl set Port "h$i""ibr" tag=$i

	# 设置local DNS
	sudo docker exec -it "h$i" bash -c "echo '#
		::1	localhost ip6-localhost ip6-loopback
		fe00::0	ip6-localnet
		ff00::0	ip6-mcastprefix
		ff02::1	ip6-allnodes
		ff02::2	ip6-allrouters

		192.168.100.100 h100
		192.168.100.101 h101
		192.168.100.102 h102

		192.168.100.103 h103
		192.168.100.104 h104
		192.168.100.105 h105

		192.168.100.106 h106
	#' >/etc/hosts"

done

#更新一下 网络连接
sudo ovs-vsctl del-port vlan_br h106ibr
sudo ovs-vsctl add-port qos_br h106ibr

# 增加一个 veth peer 直接连接两个网桥, 两侧是同一个网络，用这个方式不行，应该是可行的，看到有人这么测试，下午再看看
: "
	sudo ip link add name vlanbrside mtu 1500 type veth peer name qosbrside mtu 1500
	# sudo ip link del name vlanbrside mtu 1500 type veth peer name qosbrside mtu 1500
	sudo ovs-vsctl add-port vlan_br vlanbrside
	sudo ovs-vsctl add-port qos_br qosbrside
	sudo ip link set vlanbrside up
	sudo ip link set qosbrside up
"

# 连接同一个网段的两个网桥， ChatGPT帮忙：
sudo ovs-vsctl add-port vlan_br patch-br0-br1 -- set interface patch-br0-br1 type=patch options:peer=patch-br1-br0
sudo ovs-vsctl add-port qos_br  patch-br1-br0 -- set interface patch-br1-br0 type=patch options:peer=patch-br0-br1


#(或者拷贝一个功能进去)
#sudo docker cp path/filename dockername:path/filename
#server
sudo docker cp /usr/bin/netserver h106:/usr/bin/netserver
sudo docker exec -it h106 netserver &

#client
sudo docker cp /usr/bin/netperf h100:/usr/bin/netperf
sudo docker cp /usr/bin/netperf h101:/usr/bin/netperf
sudo docker cp /usr/bin/netperf h102:/usr/bin/netperf

## qos_br的入口 port 限流
: "
ingress_policing_rate：
为接口最大收包速率，单位kbps，超过该速度的报文将被丢弃，默认值为0表示关闭该功能；

ingress_policing_burst：
为最大突发流量大小，单位kb。默认值0表示1000kb，这个参数最小值应不小于接口的MTU，通常设置为ingress_policing_rate的10%更有利于tcp实现全速率；
"

# sudo ovs-vsctl set Interface patch-br1-br0 ingress_policing_rate=100000
# sudo ovs-vsctl set Interface patch-br1-br0 ingress_policing_burst=10000

## 实验完毕，清理
# sudo ovs-vsctl set Interface patch-br1-br0  ingress_policing_burst=0
# sudo ovs-vsctl set Interface patch-br1-br0  ingress_policing_rate=0
# sudo ovs-vsctl list Interface patch-br1-br0 


## 从这里开始设置规则 在出口 port 上设置
sudo ovs-vsctl set port patch-br0-br1 qos=@newqos \
-- --id=@newqos create qos type=linux-htb other-config:max-rate=1000000 queues=0=@q0,1=@q1,2=@q2 \
-- --id=@q0 create queue other-config:min-rate=30000 other-config:max-rate=100000 \
-- --id=@q1 create queue other-config:min-rate=10000 other-config:max-rate=100000 \
-- --id=@q2 create queue other-config:min-rate=60000 other-config:max-rate=100000

: "
	# 只用一个网桥可以看到限速，但是限速的比例不明显
	sudo ovs-vsctl set port h106ibr qos=@newqos \
	-- --id=@newqos create qos type=linux-htb other-config:max-rate=1000000 queues=0=@q0,1=@q1,2=@q2 \
	-- --id=@q0 create queue other-config:min-rate=30000 other-config:max-rate=100000 \
	-- --id=@q1 create queue other-config:min-rate=10000 other-config:max-rate=100000 \
	-- --id=@q2 create queue other-config:min-rate=60000 other-config:max-rate=100000

	sudo ovs-ofctl add-flow vlan_br "in_port=1 nw_src=192.168.100.100 actions=enqueue:4:0"
	sudo ovs-ofctl add-flow vlan_br "in_port=2 nw_src=192.168.100.101 actions=enqueue:4:1"
	sudo ovs-ofctl add-flow vlan_br "in_port=3 nw_src=192.168.100.102 actions=enqueue:4:2"

"

## 添加 flow, 入口port 2,3,4 出口 port 9
sudo ovs-ofctl show vlan_br #查看端口序号
sudo ovs-ofctl add-flow vlan_br "in_port=1 nw_src=192.168.100.100 actions=enqueue:5:0"
sudo ovs-ofctl add-flow vlan_br "in_port=2 nw_src=192.168.100.101 actions=enqueue:5:1"
sudo ovs-ofctl add-flow vlan_br "in_port=3 nw_src=192.168.100.102 actions=enqueue:5:2"

sudo ovs-ofctl dump-flows vlan_br


## 规则配置完毕， 可以测试了
# sudo ovs-vsctl list Interface patch-br0-br1
# sudo ovs-vsctl list Port patch-br0-br1 
# sudo ovs-vsctl -- --id=d72e770d-b466-4cc8-bedd-8194f1bc9ea3 get qos d72e770d-b466-4cc8-bedd-8194f1bc9ea3

# 从各个实例上访问 103   netperf是网络性能测试工具, 没看出有很大不同,应该是有问题，没找到原因，先不管了
# QOS 确实没有生效，流量没有限制住
# UDP_STREAM 结果有两行测试数，第一行显示的是本地系统的发送统计，第二行是远端系统的数据统计
# !image(./netpert-UDP_STREAM-result.jpg)
# 参考： https://www.cnblogs.com/ykhyq/p/9634522.html


sudo docker exec -it h100 netperf -H 192.168.100.106 -t UDP_STREAM -l 20
sudo docker exec -it h101 netperf -H 192.168.100.106 -t UDP_STREAM -l 20
sudo docker exec -it h102 netperf -H 192.168.100.106 -t UDP_STREAM -l 20

# 底层是tc实现的， 可以直接用tc查询
# https://blog.csdn.net/qq_44577070/article/details/123115535

# tc -s -d class show dev h106ibr

## 测试完成，清理环境
sudo ovs-vsctl list  QoS    # 显示2条，也不太对
sudo ovs-vsctl list  QoS  | grep -E  "_uuid" | awk -F':' '{print $2}' | xargs sudo ovs-vsctl destroy  QoS 
#sudo ovs-vsctl destroy  QoS  eb99252a-08e2-4bc8-b66c-f1dfce69a5a4


sudo ovs-vsctl list  Queue  
sudo ovs-vsctl list  Queue  | grep -E  "_uuid" | awk -F':' '{print $2}'| xargs sudo ovs-vsctl destroy  Queue
# sudo ovs-vsctl destroy  Queue  5baa9d24-4c7b-4d73-b7a7-f8c6693d55fe
# sudo ovs-vsctl destroy  Queue  290e0ca1-5241-4e5a-a600-faafbbba6bdb
# sudo ovs-vsctl destroy  Queue  f275f2fd-4117-49fc-8892-2216ce8b4501

sudo ovs-vsctl list Port patch-br0-br1 
sudo ovs-vsctl clear Port patch-br0-br1  qos #清除后应该QOS为空， 要先删除qos和queue,再clear, 顺序反了会报错

#也可以一步删除
# sudo ovs-vsctl  -- --all destroy qos -- --all destroy queue

sudo ovs-ofctl dump-flows vlan_br
sudo ovs-ofctl del-flows vlan_br




: "
	OVS 的QOS也是通过下面 tc 来实现的：

	## 创建一个HTB的qdisc在eth0上，句柄为1:，default 12表示默认发送给1:12

tc qdisc add dev eth0 root handle 1: htb default 12
## 创建一个root class，然后创建几个子class
## 同一个root class下的子类可以相互借流量，如果直接不在qdisc下面创建一个root class，而是直接创建三个class，他们之间是不能相互借流量的。
tc class add dev eth0 parent 1: classid 1:1 htb rate 100kbps ceil 100kbps
tc class add dev eth0 parent 1:1 classid 1:10 htb rate 30kbps ceil 100kbps
tc class add dev eth0 parent 1:1 classid 1:11 htb rate 10kbps ceil 100kbps
tc class add dev eth0 parent 1:1 classid 1:12 htb rate 60kbps ceil 100kbps
## 创建叶子qdisc，分别为fifo和sfq
tc qdisc add dev eth0 parent 1:10 handle 20: pfifo limit 5
tc qdisc add dev eth0 parent 1:11 handle 30: pfifo limit 5
tc qdisc add dev eth0 parent 1:12 handle 40: sfq perturb 10
## 设定规则：从1.2.3.4来的，发送给port 80的包，从1:10走；其他从1.2.3.4发送来的包从1:11走；其他的走默认
tc filter add dev eth0 protocol ip parent 1:0 prio 1 u32 match ip src 1.2.3.4 match ip dport 80 0xffff flowid 1:10
tc filter add dev eth0 protocol ip parent 1:0 prio 1 u32 match ip src 1.2.3.4 flowid 1:11



"







