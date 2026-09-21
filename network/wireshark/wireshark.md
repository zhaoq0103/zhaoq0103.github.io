wireshark抓包

## wireshark官方：
https://gitlab.com/wireshark

WIKI:
https://gitlab.com/wireshark/wireshark/-/wikis/

用户文档:
https://www.wireshark.org/docs/wsug_html_chunked/index.html





## wireshark过滤器表达式的规则
1. 抓包过滤
	抓包过滤器
	类型Type（host、net、port）、
	方向Dir（src、dst）、
	协议Proto（ether、ip、tcp、udp、http、icmp、ftp等）、
	逻辑运算符（&&与、|| 或、！非）


 （1）协议过滤  tcp http icmp (小写)
  (2) IP过滤  
  		host 192.168.0.103
  		src host 192.168.0.103
  		dst host 192.168.0.103
  (3) 端口过滤
  		port 80
  		src port 80
  (4) 逻辑运算
  	src host 192.168.1.104 && dst port 80 
  	！broadcast 


2. 显示过滤
	常见用显示过滤需求及其对应表达式
	eth.src == 04:f9:38:ad:13:26
	ip.addr == 192.168.1.1
	tcp.port == 80
	tcp.srcport == 12345 && tltcp.dstport == 80
	http.request.uri contains ".php"


 1）比较操作符
		比较操作符有== 等于、！= 不等于、> 大于、< 小于、>= 大于等于、<=小于等于。
（2）协议过滤
		比较简单，直接在Filter框中直接输入协议名即可。注意：协议名称需要输入小写。
		tcp，只显示TCP协议的数据包列表
		http，只查看HTTP协议的数据包列表
		icmp，只显示ICMP协议的数据包列表
（3） ip过滤
		ip.src ==192.168.1.104 显示源地址为192.168.1.104的数据包列表
		ip.dst==192.168.1.104, 显示目标地址为192.168.1.104的数据包列表
		ip.addr == 192.168.1.104 显示源IP地址或目标IP地址为192.168.1.104的数据包列表
（4）端口过滤
		tcp.port ==80, 显示源主机或者目的主机端口为80的数据包列表。
		tcp.srcport == 80, 只显示TCP协议的源主机端口为80的数据包列表。
		tcp.dstport == 80，只显示TCP协议的目的主机端口为80的数据包列表。
 5） Http模式过滤
		http.request.method=="GET", 只显示HTTP GET方法的。
（6）逻辑运算符为 and/or/not
		过滤多个条件组合时，使用and/or。比如获取IP地址为192.168.1.104的ICMP数据包表达式为ip.addr == 192.168.1.104 and icmp
（7）按照数据包内容过滤。假设我要以IMCP层中的内容进行过滤，可以单击选中界面中的码流，在下方进行选中数据。	



## wireshark https 的抓包准备工作（重要）：
	-- 最开始有几次没有抓包成功，就是因为这些准备工作没有做好
	-- Step-by-step instructions to decrypt TLS traffic from Chrome or Firefox in Wireshark:
	1. Close the browser completely (check your task manager just to be sure).
	2. Set environment variable SSLKEYLOGFILE to the absolute path of a writable file.
	3. Start the browser.
	4. Verify that the location from step 2 is created.
	5. In Wireshark, go to Edit -> Preferences -> Protocols -> TLS, and change the (Pre)-Master-Secret log filename preference to the path from step 2.
	6. Start the Wireshark capture.
	7. Open a website, for example https://www.wireshark.org/
	8. Check that the decrypted data is visible. For example, using the tls and (http or http2) filter.


	tls解密相关的文档：
	https://lekensteyn.nl/files/wireshark-ssl-tls-decryption-secrets-sharkfest18eu.pdf#page=19
	参考：
	https://zhuanlan.zhihu.com/p/324456506
	https://www.cnblogs.com/satuer/p/9357371.html  代理强制客户端相信代理的证书，使用代理证书处理数据后和代理通信，代理可以得到明文，再与真正的服务器用真实证书进行交互

## chatGPT给出的https代理的工作原理：

    1. 建立连接：当用户通过浏览器或应用程序请求一个HTTPS网站时，请求将被发送到代理服务器而不是直接发送到目标服务器。
    2. SSL/TLS握手：代理服务器收到请求后，会解析用户请求中的目标网址，并向目标服务器发起SSL/TLS握手，以建立安全连接。
    3. 加密通信：一旦SSL/TLS握手成功，代理服务器与目标服务器之间建立了加密通道，所有数据在这个通道上都会进行加密传输。
    4. 代理转发：代理服务器接收来自目标服务器的加密数据，对其进行解密，然后再重新加密并将数据转发给用户。同样，在用户发送的请求经过代理服务器后，也会被解密、重新加密后转发给目标服务器。


### 这里还有一个重要事项，要先启动wireshare抓包， 再启动浏览器， 才能抓到tls 握手的过程，才能正确解密

## wireshark https 的抓包分析：



百度https 抓包数据分析：

TCP三次握手部分：
总长 78字节
链路层： 14个字节  6 dst + 6 src + 2 versin
IP层：	20字节   总长度，版本等（ipv4)
TCP层： 44字节   20 header + 24 options


TLSv1.2部分：（总长度在哪里？ ）

CLIENT HELLO:
	TLS HEADER: content type 22 + versin 1.2 + length 167
	TLS CONTENT: HAND SHAKE 
		握手类型 + 支持的密码算法 + 压缩算法 + Randow 随机数

Multiple Handshake Message: 服务器向客户端发送

Server Hello:
	header:
	content:
		握手类型 + 选择的密码算法 + 压缩算法 + Randow 随机数 + session id
Certificate:
Server Key Exchange and Server Hello Done	

Client Key Exchange
Change Cipher Spec 和 Encrypted Handshake Message
Encrypted Handshake Message(加密握手报文)就是Finished报文 -- 双向发送

握手完成，交换数据
Application Data



tls header : 5 
tls record layer:  length 标志
tls segment data:

tls 中可以查看解密的数据( 解密后可以看到 http协议的明文数据，如果看不到明文，说明解密失败 )

## https的数据包保存时，如果要解密，这个log文件也要同时保存下来。
pre-master log file:
/Users/zhaoq0103/wireshark-tls-keylogfile.txt
/Users/zhaoq0103/DEV/zhaoq0103.github.io/network/wireshark/wireshark-officia-tls-dump-premaster.txt




## 公钥私钥，数字签名和证书

数字签名：
	对要发送的数据进行hash,把hash值用私钥加密，得到数字签名。
	签名和数据一起发送给对方，对方验签。
	对方用公钥解密签名，得hash A , 再用公钥解密数据后对数据hash, 得hash B ,
	对比A,B ，确认是否数据有无篡改。

	比如电商中的定单数据


可以通过公钥证书来证明自己的身份，并识别对方的身份。它一般是向权威机构申请得到的。
公钥证书里包含了公钥信息



# Create the key
openssl genrsa -out ~/.ssh/my-private-root-ca.key.pem 2048
# Create the CA cert
openssl req -x509 -new -nodes -key ~/.ssh/my-private-root-ca.key.pem -days 1024 -out ~/.ssh/my-private-root-ca.crt.pem -subj "/C=CN/ST=GD/L=SZ/O=ZQ/OU=IT/CN=example.com"

然后把这个证书加到信任证书列表：


创建服务器用的证书：
# Create the key
openssl genrsa -out ./my-server.key.pem 2048
# Create the certificate signing request
openssl req -new -key ./my-server.key.pem -out ./my-server.csr.pem -subj "/C=CN/ST=GD/L=SZ/O=ZQ/OU=IT/CN=localhost"
# Create the cert
openssl x509 -req -in ./my-server.csr.pem -CA ~/.ssh/my-private-root-ca.crt.pem -CAkey ~/.ssh/my-private-root-ca.key.pem -CAcreateserial -out ./my-server.crt.pem -days 500





如何使用openssl生成证书：
1. 生成私钥和公钥
	私钥： openssl genrsa -out ./my-server.key.pem 2048
	-- 公钥： openssl rsa -in ./my-server.key.pem -pubout -out public.pem
	查看证书内容：
	1.1 cat file 查看证书内容
	1.2 openssl rsa -in ./my-server.key.pem -noout -text
2. 根据私钥生成证书签名请求 (.csr) CA是用用户的私钥生成证书
	openssl req -new -key ./my-server.key.pem -out ./my-server.csr.pem -subj "/C=CN/ST=GD/L=SZ/O=ZQ/OU=IT/CN=localhost/emailAddress=zhaoq0103@163.com"
   查看请求文件
   	openssl req -noout -text -in csr.pem
   	cat file ..
3. 发送签发请求到CA进行签发，生成 x509证书 (下面是模拟CA， 这样不用给官方的CA付费)
	3.1 生成CA私钥  
		openssl genrsa -out ~/.ssh/my-private-root-ca.key.pem 2048
	3.2 根据CA私钥生成CA的自签名证书（根证书） （CA证书可以给其它证书签名，自己的证书不可以）
		openssl req -new -x509 -days 365 -key ~/.ssh/my-private-root-ca.key.pem -out ~/.ssh/my-private-root-ca.crt.pem -subj "/C=CN/ST=GD/L=SZ/O=ZQ/OU=IT/CN=localhost/emailAddress=zhaoq0103@163.com"

		然后把这个证书加到信任证书列表(MAC OS)：
		sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain ~/.ssh/my-private-root-ca.crt.pem



	3.3 自签名的CA证书查看
		openssl x509 -in ~/.ssh/my-private-root-ca.crt.pem -noout -text
	3.4 使用CA的私钥和证书对用户证书签名 (.cer or .crt) -- 签发证书
		openssl x509 -req -days 3650 -in ./my-server.csr.pem -CA ~/.ssh/my-private-root-ca.crt.pem -CAkey ~/.ssh/my-private-root-ca.key.pem -CAcreateserial -out ./my-server.crt.pem
		查看生成的证书
		openssl x509 -in crt.pem -noout -text

## 如何使用openssl进行数据签名
	1. 私钥签名：openssl dast -sha1 -sign private.pem -out data.sign data.txt
	2. 公钥验签：openssl dast -sha1 -verify public.pem -signature data.sign data.txt



## 代理服务器
常见的代理服务器搭建方法包括Shadowsocks、V2Ray和Squid等


## fiddler 手机https抓包的过程
	1. 手机上安装 fiddler的CA 证书
	2. 手机和电脑在同一个网络，手机的网络代理设置成fiddler的监听地址 电脑IP:8888 (默认情况)
	3. 可以抓包了（亲试不能抓到 微信的视频号流量，这个不可能的，不符合网络原理呢）-- 相比,wireshark能抓到，只是不能解密，说明微信视频号的tls和chome机制不同(也有可能是没有抓到 tls 握手的过程，需要提前启动wireshark,再启动微信)

	wireshark 是监听的方式抓包， fiddler 是代理的方式抓包







## MAC os chrome 插件默认安装位置：
cd ~/Library/'Application Support'/Google/Chrome/Default/Extensions