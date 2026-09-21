#!/bin/bash

: '
	OVS 的 vlan 实验环境 脚本
	VBOX不支持Apple的M芯片系列，所以安装个人版本的 Fusion Pro 试一下

	https://github.com/mininet/mininet/releases/mininet-2.3.0-210211-ubuntu-20.04.1-legacy-server-amd64-ovf.zip
'

# set -x




#实验一：VXLAN  (直接使用mininet 不挺好吗，为什么要自己创建环境呢? mininet只支持X86芯片)
#image(./obs_vlan_net.png)


sudo ovs-vsctl del-br vlan_br

sudo ovs-vsctl add-br vlan_br
sudo ip link set vlan_br up

#sudo ovs-vsctl list
#sudo ovs-ofctl show vlan_br

# 6台实验 host
imagename="hub.c.163.com/liuchao110119163/ubuntu:tcpip"
ips=("100" "101" "102" "103" "104" "105" "106")

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


#修改实验配置
receivers=("103" "104" "105")
for i in ${receivers[@]}
do
	sudo ovs-vsctl clear Port "h$i""ibr" tag
done

sudo ovs-vsctl set Port h103ibr tag=102
sudo ovs-vsctl set Port h105ibr trunks=100,101


# 查看port上的tag信息
#sudo ovs-vsctl list port portname
#sudo ovs-appctl fdb/show vlan_br


# 配置允许洪泛的 VLAN ID 列表，
# 即当交换机收到来自某个 VLAN 的未知目的地数据包时，在这些 VLAN 上广播数据包以找到目的地址。
sudo ovs-vsctl set bridge vlan_br flood-vlans=100,102,101


#另起几个终端，监听网卡
#sudo docker exec -it h103 tcpdump -vvv -n -e -i h103obr arp 
#sudo docker exec -it h104 tcpdump -vvv -n -e -i h104obr arp 
#sudo docker exec -it h105 tcpdump -vvv -n -e -i h105obr arp 

: '
#网口名称如何修改？
#ip link set xx name newname -- 需要在接口DOWN的状态下操作
#验证 linux 切换tty sudo chvt 2, 显示当前tty tty, 虚拟机中好像不太管用
'


#清理环境
sudo ovs-vsctl clear Bridge vlan_br flood_vlans

sudo ovs-vsctl list Port
sudo ovs-vsctl clear Port h100ibr tag
sudo ovs-vsctl clear Port h101ibr tag
sudo ovs-vsctl clear Port h102ibr tag
sudo ovs-vsctl clear Port h103ibr tag
sudo ovs-vsctl clear Port h104ibr tag
sudo ovs-vsctl clear Port h105ibr tag

sudo ovs-vsctl clear Port h105ibr trunks


# ================================== test data ==================================================

: ' 实验数据
1. 192.168.100.102 来 ping 192.168.100.103
	1.1 192.168.100.102 和 first_br 都配置了 tag103，first_if 是能够收到包的。根据 access port 的规则，从 first_br 出来的包头是没有带 VLAN ID 的。
	1.2 由于 second_br 是 trunk port，所有的 VLAN 都会放行， second_if 也是能收到包的，并且根据 trunk port 的规则，出来的包的包头里面是带有 VLAN ID 的。
	1.3 由于 third_br 仅仅配置了允许 VLAN 101 和 102 通过，不允许 103 通过，因而 third_if 他是收不到包的。

h103obr:
13:04:55.073505 IP (tos 0x0, ttl 64, id 23426, offset 0, flags [DF], proto ICMP (1), length 84)
    192.168.100.102 > 192.168.100.103: ICMP echo request, id 13, seq 2, length 64
13:04:55.073833 IP (tos 0x0, ttl 64, id 28100, offset 0, flags [none], proto ICMP (1), length 84)
    192.168.100.103 > 192.168.100.102: ICMP echo reply, id 13, seq 2, length 64
13:04:59.120272 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.102 tell 192.168.100.103, length 28
13:04:59.124724 ARP, Ethernet (len 6), IPv4 (len 4), Reply 192.168.100.102 is-at 62:8b:6d:97:64:63, length 28

h104obr:
13:04:55.073519 IP (tos 0x0, ttl 64, id 23426, offset 0, flags [DF], proto ICMP (1), length 84)
    192.168.100.102 > 192.168.100.103: ICMP echo request, id 13, seq 2, length 64
13:04:55.078468 IP (tos 0x0, ttl 64, id 28100, offset 0, flags [none], proto ICMP (1), length 84)
    192.168.100.103 > 192.168.100.102: ICMP echo reply, id 13, seq 2, length 64
13:04:59.121074 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.102 tell 192.168.100.103, length 28
13:04:59.124749 ARP, Ethernet (len 6), IPv4 (len 4), Reply 192.168.100.102 is-at 62:8b:6d:97:64:63, length 28

h105obr:
	no data




2.192.168.100.100 来 ping 192.168.100.105。 
	2.1 192.168.100.100 是配置了 VLAN 101 的，因为 second_br 是配置了 trunk 的，是全部放行的，所以说 second_if 是可以收到包的,头里面是带 VLAN 101
	2.2 third_br 是配置了可以放行 VLAN 101 和 102， third_if 是可以收到包的。出来的包是带 VLAN 的，而 third_if 他本身不属于某个 VLAN，ping 不通，
	2.3 first_br 是属于 VLAN 103 的，因而 first_if 是收不到包的。


h103obr:
	no data

h104obr:
	13:09:59.318228 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.105 tell 192.168.100.100, length 28

h105obr:
	13:09:59.318235 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.105 tell 192.168.100.100, length 28
	13:10:00.331473 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.105 tell 192.168.100.100, length 28





3. 192.168.100.101 来 ping 192.168.100.104
	3.1 因为 192.168.100.101 是属于 VLAN 102 的， second_if 和 third_if 都因为配置了 trunk，是都可以收到包的。
	3.2 first_br 是属于 VLAN 103 的，他不属于 VLAN 102，所以 first_if 是收不到包的。

h103obr:
	no data

h104obr:
	13:13:11.425715 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.104 tell 192.168.100.101, length 28
tcpdump 加了 -e 参数:
	13:18:33.531728 52:c6:b3:c5:dd:1a > ff:ff:ff:ff:ff:ff, ethertype 802.1Q (0x8100), length 46: vlan 101, p 0, ethertype ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.104 tell 192.168.100.101, length 28

h105obr:
	13:13:11.425719 ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.104 tell 192.168.100.101, length 28
tcpdump 加了 -e 参数:
	13:18:34.539726 52:c6:b3:c5:dd:1a > ff:ff:ff:ff:ff:ff, ethertype 802.1Q (0x8100), length 46: vlan 101, p 0, ethertype ARP, Ethernet (len 6), IPv4 (len 4), Request who-has 192.168.100.104 tell 192.168.100.101, length 28



'