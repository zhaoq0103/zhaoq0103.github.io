# 1. 输入法
# 2. chrome
# 3. weixin_work from appstore
# 4. youdaoyun
# 5. wps


git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.di diff

# git终端中文显示问题
git config --global core.quotepath false

# 确认一下显示中文是否正常
# git log --stat

# --system, --global, --local
git config --global --list
git config --global --unset user.name
git config --global --unset user.email




git config --local  user.name "OnTheWay0103"
git config --local  user.email "OnTheWay0103@163.com"



git config --local  user.name "zhaoq0103"
git config --local  user.email "zhaoq0103@163.com"



# go env setup
brew install go
go env -w  GOPROXY=https://goproxy.cn,direct

# go env -w  GO111MODULE="on"
# export GOPATH="/Users/zhaoq0103/go/resource"

