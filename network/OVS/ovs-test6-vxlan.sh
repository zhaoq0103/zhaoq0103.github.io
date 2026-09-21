#!/bin/bash

: "

	测试
	在三台隔离的物理机上实验隧道打通功能 
	### ======================================== 测试 隧道 TUNNEL ====================================================

	网络 Interface 的常见 type 类型包括以下几种：
		Ethernet：以太网接口是最常见的网络接口类型，用于在本地区域网络（LAN）中传输数据。
		Bridge：桥接接口允许将多个网络接口连接在一起，形成一个逻辑网络以实现数据包转发。
		VLAN（Virtual LAN）：虚拟局域网接口允许对物理网络进行逻辑分割，使得不同 VLAN 可以相互隔离。
		Tunneling Interfaces：隧道接口用于在不同网络之间创建虚拟通道，如 GRE（Generic Routing Encapsulation）和 VxLAN（Virtual Extensible LAN）。
		Bonding/Teaming：绑定接口或团队接口允许将多个物理接口组合为一个逻辑接口，提供负载均衡和冗余功能。
		Loopback：环回接口是用于本地主机内部通信的虚拟接口，通常用于测试和诊断。
		Wireless：无线接口用于连接到无线网络，并支持无线通信标准（如 Wi-Fi）。
		PPP（Point-to-Point Protocol）：点对点协议接口用于建立点对点连接，通常用于拨号连接或特定的网络配置需求。
		GRE（Generic Routing Encapsulation）：通用路由封装接口是一种隧道协议，用于在 IP 网络上封装其他协议的数据包。
		VxLAN（Virtual Extensible LAN）：虚拟可扩展局域网接口用于扩展虚拟局域网（VLAN）的数量，通过在基础网络中创建逻辑网络来提供多租户支持。

	type=internal 是一种特定类型的网络接口，通常用于表示内部网络接口或虚拟的内部通信通道。

	GRE、VXLAN、IPsec_GRE是OVS支持的三种隧道

	查看OVS支持的隧道类型,好像不准
	sudo ovs-appctl dpif/show

	sudo ifconfig enp0s1 172.16.82.5/24

	sudo ip addr del 172.16.82.6/24 dev slipside
	sudo ip addr add 172.16.82.6/24 dev slipside


	sudo ifconfig utun3 down
	sudo ifconfig utun3 destroy

	journalctl -b  #linux 系统启动日志 

	ipsec_gre 这个协议没有配置成功，应该是需要设置密码一类的
	error: could not open network device ipsec0 (Address family not supported by protocol)

	这个topo不容易测试tunnel两端的连通性, 打隧道前是不通的吗？

	打通隧道后互相可以ping tong,没测试好， 没有隧道也能ping pong
		# ping -c 2 172.16.82.6
		# PING 172.16.82.6 (172.16.82.6) 56(84) bytes of data.
		# 64 bytes from 172.16.82.6: icmp_seq=1 ttl=64 time=3.21 ms
		# 64 bytes from 172.16.82.6: icmp_seq=2 ttl=64 time=1.86 ms

	# 准备改造成这种方法，方便测试： 没有试验成功， 网络总是存在不通的链路
		sudo ip netns add left 
		sudo ip link add name veth1 type veth peer name sw1-p1
		sudo ip link set dev veth1 netns left
		sudo ip netns exec left ifconfig veth1 10.0.0.1/24 up
		 
		sudo ovs-vsctl add-br sw1
		sudo ovs-vsctl add-port sw1 sw1-p1
		sudo ip link set sw1-p1 up


	参考：tcpipillustraed/gre-env-setup.sh 

"


sudo ovs-vsctl  del-br br-int
sudo ovs-vsctl  add-br br-int
#sudo ifconfig br-int 10.0.0.1/24
#sudo ip addr add 10.0.0.1/24 dev br-int

sudo ip netns add instA 
sudo ovs-vsctl add-port br-int tapbr -- set Interface tapbr type=internal
sudo ip link set dev tapbr netns instA
sudo ip netns exec instA ip addr add  10.0.0.1/24  dev tapbr  
sudo ip netns exec instA ip link set  dev tapbr up

sudo ovs-vsctl add-port br-int gre0 -- set Interface gre0 type=gre options:local_ip=172.16.82.4 options:remote_ip=172.16.82.5
#sudo ovs-vsctl add-port br-int gre1 -- set Interface gre1 type=gre options:local_ip=172.16.82.4 options:remote_ip=172.16.82.6

sudo ovs-vsctl add-port br-int vxlan0 -- set Interface vxlan0 type=vxlan options:local_ip=172.16.82.4 options:remote_ip=172.16.82.6

sudo ip netns exec instA ping -c 3 10.0.0.2

: "
VXLAN: ping 没有响应
	09:22:39.411200 aa:6d:80:f0:ac:09 > aa:6d:80:f0:ac:0b, ethertype IPv4 (0x0800), length 92: (tos 0x0, ttl 64, id 47239, offset 0, flags [DF], proto UDP (17), length 78)
    172.16.82.4.48551 > 172.16.82.6.4789: [no cksum] VXLAN, flags [I] (0x08), vni 0
06:ef:d8:c8:73:59 > ff:ff:ff:ff:ff:ff, ethertype ARP (0x0806), length 42: Ethernet (len 6), IPv4 (len 4), Request who-has 10.0.0.3 tell 10.0.0.1, length 28
"

#监听test1的eth0: 
sudo tcpdump -vvv -n -e -i enp0s1 icmp

sudo tcpdump -vvv -n -e -i enp0s1 -c -w  1024.log 


sudo ovs-vsctl  del-br br-int
sudo ovs-vsctl add-br br-int
#sudo ifconfig br-int 10.0.0.2/24
#sudo ip addr add 10.0.0.2/24 dev br-int

sudo ip netns add instA 
sudo ovs-vsctl add-port br-int tapbr -- set Interface tapbr type=internal
sudo ip link set dev tapbr netns instA
sudo ip netns exec instA ip addr add  10.0.0.2/24  dev tapbr  
sudo ip netns exec instA ip link set  dev tapbr up

sudo ovs-vsctl add-port br-int gre0 -- set Interface gre0 type=gre options:local_ip=172.16.82.5 options:remote_ip=172.16.82.4

#系统不支持 type=ipsec_gre
sudo ovs-vsctl del-port br-int ipsec0
sudo ovs-vsctl add-port br-int vxlan0 -- set Interface vxlan0 type=vxlan options:local_ip=172.16.82.5 options:remote_ip=172.16.82.6 
# sudo ovs-vsctl add-port br-int ipsec0 -- set Interface ipsec0 type=ipsec_gre options:local_ip=172.16.82.5 options:remote_ip=172.16.82.6 options:psk=password


sudo ip netns exec instA ping -c 3 10.0.0.1
: "

GRE:

09:10:00.912818 aa:6d:80:f0:ac:09 > aa:6d:80:f0:ac:0a, ethertype IPv4 (0x0800), length 136: (tos 0x0, ttl 64, id 17737, offset 0, flags [DF], proto GRE (47), length 122)
    172.16.82.4 > 172.16.82.5: GREv0, Flags [none], proto TEB (0x6558), length 102
	06:ef:d8:c8:73:59 > 02:96:07:cd:e0:89, ethertype IPv4 (0x0800), length 98: (tos 0x0, ttl 64, id 54716, offset 0, flags [DF], proto ICMP (1), length 84)
    10.0.0.1 > 10.0.0.2: ICMP echo request, id 31739, seq 3, length 64
09:10:00.914610 aa:6d:80:f0:ac:0a > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 136: (tos 0x0, ttl 64, id 39615, offset 0, flags [DF], proto GRE (47), length 122)
    172.16.82.5 > 172.16.82.4: GREv0, Flags [none], proto TEB (0x6558), length 102
	02:96:07:cd:e0:89 > 06:ef:d8:c8:73:59, ethertype IPv4 (0x0800), length 98: (tos 0x0, ttl 64, id 44135, offset 0, flags [none], proto ICMP (1), length 84)
    10.0.0.2 > 10.0.0.1: ICMP echo reply, id 31739, seq 3, length 64


"
#监听test2的eth0: 
sudo tcpdump -vvv -n -e -i enp0s1 icmp



sudo ovs-vsctl  del-br br-int
sudo ovs-vsctl add-br br-int
#sudo ifconfig br-int 10.0.0.3/24
#sudo ip addr add 10.0.0.3/24 dev br-int

sudo ip netns add instA 
sudo ovs-vsctl add-port br-int tapbr -- set Interface tapbr type=internal
sudo ip link set dev tapbr netns instA
sudo ip netns exec instA ip addr add  10.0.0.3/24  dev tapbr  
sudo ip netns exec instA ip link set  dev tapbr up

sudo ovs-vsctl add-port br-int gre0 -- set Interface gre0 type=gre options:local_ip=172.16.82.6 options:remote_ip=172.16.82.4
sudo ovs-vsctl add-port br-int vxlan0 -- set Interface vxlan0 type=vxlan options:local_ip=172.16.82.6 options:remote_ip=172.16.82.4

sudo ovs-vsctl add-port br-int vxlan1 -- set Interface vxlan1 type=vxlan options:local_ip=172.16.82.6 options:remote_ip=172.16.82.5
# 先不连这根线看一下数据
# sudo ovs-vsctl add-port br-int ipsec0 -- set Interface ipsec0 type=ipsec_gre options:local_ip=172.16.82.6 options:remote_ip=172.16.82.5 options:psk=password


sudo ip netns exec instA ping -c 3 10.0.0.2
sudo ip netns exec instA ping -c 3 10.0.0.1

: "
09:44:43.791255 aa:6d:80:f0:ac:0a > ff:ff:ff:ff:ff:ff, ethertype ARP (0x0806), length 60: Request who-has 10.123.74.253 tell 172.16.82.5, length 46
09:44:43.818597 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 710: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 28660:29304, ack 1, win 501, options [nop,nop,TS val 812495021 ecr 2172288205], length 644
09:44:43.819350 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 29304, win 2048, options [nop,nop,TS val 2172288308 ecr 812495021], length 0
09:44:43.922241 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 558: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 29304:29796, ack 1, win 501, options [nop,nop,TS val 812495125 ecr 2172288308], length 492
09:44:43.922734 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 29796, win 2048, options [nop,nop,TS val 2172288412 ecr 812495125], length 0
09:44:44.026647 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 558: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 29796:30288, ack 1, win 501, options [nop,nop,TS val 812495229 ecr 2172288412], length 492
09:44:44.027492 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 30288, win 2048, options [nop,nop,TS val 2172288516 ecr 812495229], length 0
09:44:44.130417 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 558: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 30288:30780, ack 1, win 501, options [nop,nop,TS val 812495333 ecr 2172288516], length 492
09:44:44.130892 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 30780, win 2048, options [nop,nop,TS val 2172288620 ecr 812495333], length 0
09:44:44.233443 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 558: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 30780:31272, ack 1, win 501, options [nop,nop,TS val 812495436 ecr 2172288620], length 492
09:44:44.234281 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 31272, win 2048, options [nop,nop,TS val 2172288723 ecr 812495436], length 0
09:44:44.335653 aa:6d:80:f0:ac:0b > ff:ff:ff:ff:ff:ff, ethertype ARP (0x0806), length 60: Request who-has 10.123.74.253 tell 172.16.82.4, length 46
09:44:44.341527 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 558: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 31272:31764, ack 1, win 501, options [nop,nop,TS val 812495544 ecr 2172288723], length 492
09:44:44.342063 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 31764, win 2048, options [nop,nop,TS val 2172288831 ecr 812495544], length 0
09:44:44.380075 aa:6d:80:f0:ac:09 > ff:ff:ff:ff:ff:ff, ethertype ARP (0x0806), length 42: Request who-has 10.123.74.253 tell 172.16.82.6, length 28
09:44:44.442727 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 854: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 31764:32552, ack 1, win 501, options [nop,nop,TS val 812495645 ecr 2172288831], length 788
09:44:44.443145 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 32552, win 2048, options [nop,nop,TS val 2172288933 ecr 812495645], length 0
09:44:44.549249 aa:6d:80:f0:ac:09 > 16:98:77:c5:49:64, ethertype IPv4 (0x0800), length 558: 172.16.82.6.22 > 172.16.82.1.50184: Flags [P.], seq 32552:33044, ack 1, win 501, options [nop,nop,TS val 812495752 ecr 2172288933], length 492
09:44:44.549824 16:98:77:c5:49:64 > aa:6d:80:f0:ac:09, ethertype IPv4 (0x0800), length 66: 172.16.82.1.50184 > 172.16.82.6.22: Flags [.], ack 33044, win 2048, options [nop,nop,TS val 2172289039 ecr 812495752], length 0
"
#监听test3的eth0: 
sudo tcpdump -vvv -n -e -i enp0s1 icmp


# STP 去环
# 形成环后，查看arp表项，发现已经无法学习到一些mac地址 
# arp -n 
: "
	Address                  HWtype  HWaddress           Flags Mask            Iface
	172.16.82.6              ether   aa:6d:80:f0:ac:09   C                     enp0s1
	172.16.82.5              ether   aa:6d:80:f0:ac:0a   C                     enp0s1
	10.123.74.253                    (incomplete)                              enp0s1
	8.8.8.8                          (incomplete)                              enp0s1
	172.16.82.1              ether   16:98:77:c5:49:64   C                     enp0s1
"

# 查看MAC 转发表，出现错乱
# ovs-appctl fdb/show br1

# ovs-vsctl list bridge查看stp默认是关闭的

: "
	可以设备和查询 stp 的 stp_enable ， stp-path-cost, stp-priority 等


	ovs-vsctl set Port ipsec0 other_config:stp-path-cost=190

"

sudo ovs-vsctl get Bridge br-int stp_enable
sudo ovs-vsctl set Bridge br-int stp_enable=true

sudo ip netns exec instA ip neigh flush all
# 查看配置
sudo ovs-appctl fdb/show br-int
sudo ovs-ofctl show br-int

sudo ovs-vsctl list Bridge

sudo ovs-vsctl show 
: "
#	Bridge br-int
#        Port br-int
#            Interface br-int
#                type: internal
#        Port ipsec0
#            Interface ipsec0
#                type: ipsec_gre
#                options: {local_ip="172.16.82.5", psk=password, remote_ip=\"172.16.82.6\"}
#                error: \"could not open network device ipsec0 (Address family not supported by protocol)\"
#        Port gre0
#            Interface gre0
#                type: gre
#                options: {local_ip="172.16.82.5", remote_ip="172.16.82.4"}
#	

"
# 
sudo tcpdump -n -e  -i enp0s1 icmp

----------------------

gre-env-setup-{1,2}.sh
sudo tcpdump -nn -i enp0s1 icmp


# 清理数据
sudo ip netns del instA
sudo ovs-vsctl  del-br br-int
sudo ip link del dev vxlan0 ?




: "
	常见的 OVS 端口支持的接口类型：
	ovs-system：系统接口，用于与 Linux 内核交互并处理数据包转发。
	internal：内部接口，仅在 OVS 内部使用，不与物理网络接口相连接。	
	patch：用于将 OVS 桥连接到另一个 OVS 桥或外部网络设备。	
	gre：用于 GRE（通用路由封装）隧道。	
	vxlan：用于 VXLAN（虚拟化扩展局域网）隧道。	
	vlan：用于 VLAN 接口。	
	bond：用于创建链路聚合组（LACP 或者 static bond）。	
	dpdk：用于 DPDK 加速。	
	tap：用于 TAP（虚拟网络设备）接口。	
	geneve：用于 Geneve 隧道。

"


### ======================================== 第二个测试topo ====================================================
#vbox虚拟机1

: " sudo ip netns add instanse1  #可以直接增加一个网络，这个太好了  "

sudo ip netns add left 
sudo ip link add name veth1 type veth peer name sw1-p1
sudo ip link set dev veth1 netns left
sudo ip netns exec left ifconfig veth1 10.0.0.1/24 up
 
sudo ovs-vsctl add-br sw1
sudo ovs-vsctl add-port sw1 sw1-p1
sudo ip link set sw1-p1 up
 
#vbox虚拟机2
sudo ip netns add right
sudo ip link add name veth1 type veth peer name sw2-p1
sudo ip link set dev veth1 netns right
sudo ip netns exec right ifconfig veth1 10.0.0.2/24 up
 
sudo ovs-vsctl add-br sw2
sudo ovs-vsctl add-port sw2 sw2-p1
sudo ip link set sw2-p1 up

# tunnel前先测试一下连通性, 发现不通，正常
sudo ip netns exec left ping -c 2  10.0.0.2

# tunnel
sudo ovs-vsctl add-port sw1 tun0 -- set Interface tun0 type=gre options:remote_ip=172.16.82.5
sudo ovs-vsctl add-port sw2 tun0 -- set Interface tun0 type=gre options:remote_ip=172.16.82.4

# 再次测试，通了
sudo ip netns exec left ping -c 2  10.0.0.2




