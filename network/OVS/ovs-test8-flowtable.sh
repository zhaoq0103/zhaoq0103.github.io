#!/bin/bash

: "

	### ======================================== 测试 flowtable ====================================================

	参考 ~/code-github/tcpipillustrated/flow-env-setup.sh 
"

	


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



