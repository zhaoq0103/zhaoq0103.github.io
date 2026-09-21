#协议分层：

物理层，
链路层，
网络层，
传输层，
应用层


![IP数据帧](./WechatIMG987.jpg)



一。 链路层协议：最底层的协议

Ethernet, Wi-fi, SLIP, PPP,ARP 等

链路层如何确定MTU:（从交互和线路利率用考虑）
吞吐量的计算：


二。 IP

IP网络段，子网段，主机段

用什么标志子网段？  （用IP和子网掩码就可以算出来）CIDR


三。ARP

ARP协议： arp数据包是否含有ip和tcp数据段？- tcpdump一下
委托ARP（当路由器对来自于另一个路由器接口的ARP请求进行应答时）
免费ARP（发送自己IP地址的ARP请求，一般发生在引导过程中）


四。RARP

无盘引导



五。ICMP

8位类型：8位代码：16位校验和

IP:ICMP:TCP-UDP:APP

部分古老的系统，对TFTP的ICMP差错返回报文不提交给应用程序处理，
导致应用程序收不到回复，就会进行请求重试


六。ping
ping程序是对两个TCP/IP系统连通性进行测试的基本工具。
它只利用ICMP回显请求和回显应答报文，而不用经过传输层（TCP/UDP）。
Ping服务器一般在内核中实现ICMP的功能

七。traceroute
 	-g -G 

八。RIP（Routing Information Protocol）
netstat
sudo docker exec -it svr4 netstat -rn   

路由器标志： U G H D M
U（Up）：表示该路由条目是活动的，可用于发送数据包。
G（Gateway）：表示下一跳地址是网关地址，数据包将通过该网关进行转发。
H（Host）：表示目标网络是一个主机，而不是一个网络范围。
D（Dynamic）：表示路由条目是通过动态路由协议自动添加的。
M（Modified）：表示路由条目已修改，并且尚未同步到内核的路由缓存。
R（Reject）：表示目标不可达，数据包将被拒绝。

IP选路可以通过ICMP进行路由重定向，给主机提供一个学习路由的机会

删除 svr4 到 140.252.13.64/27 网络的路由后，会由 sun 进行 Redirect
sudo docker exec -it svr4 ip route del 140.252.13.64/27
# sudo docker exec -it svr4 ping -c 5 slip
# PING slip (140.252.13.65) 56(84) bytes of data.
# From sun (140.252.13.33): icmp_seq=1 Redirect Host(New nexthop: sun (140.252.13.33))
# 64 bytes from sun (140.252.13.33): icmp_seq=1 ttl=63 time=6.38 ms
# From sun (140.252.13.33): icmp_seq=2 Redirect Host(New nexthop: sun (140.252.13.33))
# 64 bytes from sun (140.252.13.33): icmp_seq=2 ttl=63 time=1.00 ms


静态路由设置
动态路由设置


九。路由交换协议： OSPF和BGP （UDP数据）
IP:UDP:RIP

两种基本的选路协议：
即用于同一自治系统各路由器之间的内部网关协议（IGP）
和用于不同自治系统内路由器通信的外部网关协议（EGP）


内部网关协议IGP：RIP -- 采用距离向量
	RIP只能用在主机间最大跳数值为15的AS内。
	度量为16表示到无路由到达该IP地址。

	OSPF:链路状态协议，收敛快 （多播， 不是广播）
外部网关协议EGP：BGP
	BGP使用TCP作为其传输层协议


IP数据报分片以后，只有到达目的地才进行重新组装
（这里的重新组装与其他网络协议不同，它们要求在下一站就进行进行重新组装，而不是在最终的目的地） 
重新组装由目的端的IP层来完成

IP数据报是指IP层端到端的传输单元（在分片之前和重新组装之后），
分组是指在IP层和链路层之间传送的数据单元。
一个分组可以是一个完整的IP数据报，也可以是IP数据报的一个分片。

点到点的链路中，不要求两个方向的MTU为相同值


十。UDP
UDP协议，数据分片等


十一。单播unicast，多播（组播？），广播
广播和多播仅应用于UDP


指向子网的广播


这段找时间再理解一下：

指向子网的广播地址为主机号为全1且有特定子网号的地址。作为子网直接广播地址的IP地址需要了解子网的掩码。
例如，如果路由器收到发往128.1.2.255的数据报，
当B类网络128.1的子网掩码为255.255.255.0时，该地址就是指向子网的广播地址；
但如果该子网的掩码为255.255.254.0，该地址就不是指向子网的广播地址


十二。IGMP:Internet组管理协议  --这个协议不太明白20231209
支持主机和路由器进行多播的Internet组管理协议
所有主机组地址 如：224.0.0.1


十三. DNS
域名系统（DNS）是一种用于TCP/IP应用程序的分布式数据库

A 			IP地址
NS 			名字服务器
CNAME		规范名称
PTR 		指针记录
HINPO		主机信息
MX			邮件交换记录
* ANY	    对所有记录的请求


十四。TFTP
TFTP(Trivial File Transfer Protocol)即简单文件传送协议
和TCP的文件传送协议（FTP）不同，为了保持简单和短小，TFTP将使用UDP

十五。BOOTP
BOOTP有两个熟知端口：BOOTP服务器为67，BOOTP客户为68


十六。TCP 
segment
TCP不在字节流中插入记录标识符。我们将这称为字节流服务（byte stream service）


积压值(backlog): 
	积压值说明的是TCP监听的端点已被TCP接受而等待应用层接受的最大连接数
	积压值对系统所允许的最大连接数，或者并发服务器所能并发处理的客户数，并无影响


TCP发送方使用 拥塞窗口 来控制流量， 接收方使用 通告窗口 来控制流量；
还有 拥塞避免 技术 控制流量

通道容量计算：带宽时延乘积


capacity (bit) = bandwidth (b/s) × round-trip time (s)

TCP超时与重传
TCP的4个定时器：
重传定时器，坚持(persist)定时器，保活(keepalive)定时器，2MSL定时器

指数退避(exponential backoff)



窗口探查和坚持定时器



https://www.codenong.com/s1190000022929052/ 这篇文章解释的比较清楚了
纳格算法：Nagle
算法规则
如果包长度达到MSS，则允许发送
如果包含FIN，则允许发送
如果设置了TCP_NODELAY，则允许发送
未设置TCP_CORK选项时，若所有发出去的小数据包（包长度小于MSS）均被确认，则允许发送
上述条件都未满足，但发生了超时（一般为200ms），则立即发送。




除了字节流服务，还存在其他几种常见的数据传输服务类型，包括以下几种：

块（Block）服务：
块服务将数据划分为固定大小的数据块进行传输
每个数据块具有自己的边界和长度，并且按照块的顺序进行传输。
块服务可以提供更高的传输效率和易于管理的数据单位，适用于需要按照预定义大小进行处理的场景。

消息（Message）服务：
消息服务在传输数据时，将数据分为离散的消息单元进行传输。
每个消息都是一个完整的、自包含的数据单元，在传输过程中保持完整性。
消息服务侧重于传输有意义的消息对象，适用于需要处理特定消息类型的场景，如协议级别的通信。

数据报（Datagram）服务：
数据报服务以数据报（Datagram）为单位进行传输。
每个数据报都是独立的、自包含的数据单元，具有自己的标识符和边界。
数据报服务提供无连接和不可靠的传输，适用于需要快速传输独立数据单元的场景。





LISTEN：侦听来自远方的TCP端口的连接请求
SYN-SENT：再发送连接请求后等待匹配的连接请求（客户端）
SYN-RECEIVED：再收到和发送一个连接请求后等待对方对连接请求的确认（服务器）
ESTABLISHED：代表一个打开的连接

FIN-WAIT-1：等待远程TCP连接中断请求，或先前的连接中断请求的确认
FIN-WAIT-2：从远程TCP等待连接中断请求
CLOSE-WAIT：等待从本地用户发来的连接中断请求
CLOSING：等待远程TCP对连接中断的确认 ？
LAST-ACK：等待原来的发向远程TCP的连接中断请求的确认
TIME-WAIT：等待足够的时间以确保远程TCP接收到连接中断请求的确认
CLOSED：没有任何连接状态





主动端可能出现的状态：FIN_WAIT1、FIN_WAIT2、CLOSING、TIME_WAIT
被动端可能出现的状态：CLOSE_WAIT LAST_ACK

客户端的状态可以用如下的流程来表示：
CLOSED->SYN_SENT->ESTABLISHED->FIN_WAIT_1->FIN_WAIT_2->TIME_WAIT->CLOSED

服务器的状态可以用如下的流程来表示：
CLOSED->LISTEN->SYN收到->ESTABLISHED->CLOSE_WAIT->LAST_ACK->CLOSED


（1）主动端出现大量的FIN_WAIT1时需要注意网络是否畅通、出现大量的FIN_WAIT2需要仔细检查程序为何迟迟收不到对端的FIN（可能是主动方或者被动方的bug）、出现大量的TIME_WAIT需要注意系统的并发量/socket句柄资源/内存使用/端口号资源等。

（2）被动端出现大量的 CLOSE_WAIT 需要仔细检查为何自己迟迟不愿调用close关闭连接（可能是bug，socket打开用完没有关闭）


十七。SNMP:简单网络管理协议

基本上看不明白，应该是用于网络管理的一些协议





安装NVM
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

source ~/.bashrc

nvm ls-remote       查看node版本

v20.10.0 2023年latest LTS

nvm install node

nvm install v20.10.0   -- 安装指定版本
nvm use v20.10.0 





微信机器人相关的项目：

https://github.com/littlecodersh/ItChat -- python项目
https://github.com/liuwons/wxBot  -- 本项目已停止维护
https://github.com/youfou/wxpy --python版本
https://github.com/wechaty/wechaty -- 多语言支持
https://github.com/hanson/vbot --php版本
https://github.com/kanjielu/jeeves -- java ee 版本
https://github.com/yaphone/itchat4j -- 	java 版本







