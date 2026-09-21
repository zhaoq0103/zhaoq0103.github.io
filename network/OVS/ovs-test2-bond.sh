#!/bin/bash


#另起几个终端，监听网卡
#sudo docker exec -it h103 tcpdump -vvv -n -i h103obr -e
#sudo docker exec -it h104 tcpdump -vvv -n -i h104obr -e
#sudo docker exec -it h105 tcpdump -vvv -n -i h105obr -e

echo "test comment"



# 实验二：BOND - todo, 这个比较容易理解，以后再试吧
#image(./ovs_bond_net.png)

# // echo "not crorrect"
# echo "one line commnet"

: '
ovs-vsctl add-bond br0 bond0 first_br second_br
ovs-vsctl add-bond br1 bond1 first_if second_if
ovs-vsctl set Port bond0 lacp=active
ovs-vsctl set Port bond1 lacp=active



ovs-vsctl set Port bond0 bond_mode=balance-slb
ovs-vsctl set Port bond1 bond_mode=balance-slb

'


: '
实验五：配置使用 OpenFlow Controller，体验一把作为小区物业在监控室里面管控整个小区道路的样子。
实验八：测试 Port 的 VLAN 功能。看一下 VLAN 隔离究竟是什么样的。
实验十：QoS 功能。体验一把如果使用 HTB 进行网卡限流。
实验十一：GRE 和 VXLAN 隧道功能，看虚拟网络如何进行租户隔离。
实验十五：对 Flow Table 的操作，体验流表对网络包随心所欲的处理。
'
