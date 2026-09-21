#!/bin/bash

/***
	有什么想说的吗
	这个脚本没有进行验证
	
***/


# 基于网络 namespace 的路由器实现
ip netns add routerns

ip netns exec routerns sysctl -w net.ipv4.ip_forward=1

ip netns exec routerns iptables-save -c 
ip netns exec routerns iptables-restore -c

: "
 	-- 是 ovs-vsctl 命令的选项分隔符，用于指定命令中的不同选项和参数
 "

ovs-vsctl -- add-port br0 taprouter \
-- set Interface taprouter type=internal \
-- set Interface taprouter external-ids:iface-status=active \
-- set Interface taprouter external-ids:attached-mac=fa:16:3e:84:6e:cc

ip link set taprouter netns routerns

ip netns exec routerns ip -4 addr add 192.168.1.1/24 brd 192.168.1.255 scope global dev taprouter

ovs-vsctl -- add-port br-ex taprouterex \
-- set Interface taprouterex type=internal \
-- set Interface taprouterex external-ids:iface-status=active \
-- set Interface taprouterex external-ids:attached-mac=fa:16:3e:68:12:c0

ip link set taprouterex netns routerns

ip netns exec routerns ip -4 addr add 16.158.1.100/24 brd 16.158.1.255 scope global dev taprouterex


ip netns exec routerns route -n
Kernel IP routing table
Destination   Gateway     Genmask     Flags Metric Ref  Use Iface
0.0.0.0     16.158.1.1  0.0.0.0     UG  0   0    0 taprouterex
192.168.1.0    0.0.0.0     255.255.255.0  U   0   0    0 taprouter
16.158.1.0  0.0.0.0     255.255.255.0  U   0   0    0 taprouterex


mkdir /sys/fs/cgroup/net_cls
mount -t cgroup -onet_cls net_cls /sys/fs/cgroup/net_cls


## 使用docker 处理 QOS 规则
tc filter add dev eth0 protocol ip parent 1:0 prio 1 u32 match ip src 1.2.3.4 match ip dport 80 0xffff flowid 1:10
tc filter add dev eth0 protocol ip parent 1:0 prio 1 u32 match ip src 1.2.3.4 flowid 1:11

tc filter add dev eth0 protocol ip parent 1:0 prio 1 handle 1: cgroup

##  对用户 a ,b 进行带宽限制
mkdir /sys/fs/cgroup/net_cls/a   
mkdir /sys/fs/cgroup/net_cls/b

echo 0x00010010 > /sys/fs/cgroup/net_cls/a/net_cls.classid    
echo 0x00010011 > /sys/fs/cgroup/net_cls/b/net_cls.classid



ip link add name veth1 mtu 1500 type veth peer name veth2 mtu 1500
ip link set veth1 master testbr    
ip link set veth1 up

docker inspect '--format={{ .State.Pid }}' test

rm -f /var/run/netns/12065    
ln -s /proc/12065/ns/net /var/run/netns/12065

ip netns exec 12065 ip link set veth2 name eth0

ip netns exec 12065 ip addr add 172.17.0.2/16 dev eth0    
ip netns exec 12065 ip link set eth0 up


-A POSTROUTING -s 172.17.0.0/16 ! -o docker0 -j MASQUERADE


-A DOCKER -p tcp -m tcp --dport 10080 -j DNAT --to-destination 172.17.0.2:80
