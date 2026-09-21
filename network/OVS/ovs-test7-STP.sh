#!/bin/bash

: "

	### ======================================== 测试 STP ====================================================

	直接在VXLAN那个测试中测试了
"


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
"

ovs-vsctl set Port ipsec0 other_config:stp-path-cost=190

sudo ovs-vsctl set Bridge inner_br stp_enable=true








: "

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

# ===================== end =====================
"


