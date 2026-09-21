

ifconfig
imagename="hub.c.163.com/liuchao110119163/ubuntu:tcpip"
sudo docker run -d -it --network=custom_net --name  h110  ${imagename}
sudo docker run -d -it --network=custom_net --name  h120  ${imagename}
sudo docker ps
ifconfig
sudo docker exec -it h110 ip a 
sudo docker exec -it h120 ip a 
sudo docker run -d -it --name  h108  ${imagename}
sudo docker exec -it h108 ip a 

sudo docker exec -it h108 ping -c 2 172.18.0.3
sudo docker exec -it h110 ping -c 2 172.18.0.3
ip a
ip r
sudo sysctl net.ipv4.ip_forward
sudo iptables-save

sudo docker ps
sudo docker network connect custom_net d0f74152d094

sudo docker exec -it h108 ip a
sudo docker exec -it h110 ping -c 2 172.18.0.4
sudo docker exec -it h110 ping -c 2 h120


ping -c 2 www.baidu.com
ping -c 2 8.8.8.8
sudo docker network ls

sudo iptables -t nat -S


ping -c 2 192.168.0.102
ping -c 2 192.168.0.103

ping -c 2 172.16.82.5



sudo docker run -d -p 8500:8500 -h consul --name consul progrium/consul -server -bootstrap

# 用 Docker Desktop 安装 Docker 时，Docker 的配置文件
ls ~/.docker 


sudo cp /etc/docker/daemon.json /etc/docker/daemon.json.bak
sudo nano /etc/docker/daemon.json
{
  "registry-mirrors": ["https://hub-mirror.c.163.com"]
}
sudo systemctl daemon-reload
sudo systemctl restart docker


dockerd --validate --config-file=./daemon.json  

"https://mirror.ccs.tencentyun.com",
"https://mirror.ccs.huetalaoshe.com",
"https://mirrors.xmu.edu.cn/docker"