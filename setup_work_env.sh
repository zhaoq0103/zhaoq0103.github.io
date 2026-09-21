# 1. 输入法
# 2. chrome
# 3. weixin_work from appstore
# 4. youdaoyun
# 5. wps

# oh-my-zsh link:
# https://github.com/ohmyzsh/ohmyzsh


git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.di diff

# git终端中文显示问题
git config --global core.quotepath false

# 确认一下显示中文是否正常
# git log --stat

# 不同级别的配置
# --system, --global, --local
git config --global --list
git config --global --unset user.name
git config --global --unset user.email


///////////////////// 用户名密码 方式 /////////////////////////
# 当前项目有效
git config --local  user.name "OnTheWay0103"
git config --local  user.email "OnTheWay0103@163.com"


# 当前项目有效
git config --local  user.name "zhaoq0103"
git config --local  user.email "zhaoq0103@163.com"


///////////////////// ssh 方式 /////////////////////////

~/.ssh/config

# GitHub account 1
Host github.com-account1
  HostName github.com
  User git
  IdentityFile ~/.ssh/id_rsa_account1

# GitHub account 2
Host github.com-account2
  HostName github.com
  User git
  IdentityFile ~/.ssh/id_rsa_account2

# Git 存储库
git remote set-url origin git@github.com-account1:username/repo.git
git remote set-url origin git@github.com-account2:username/repo.git

//////////////////////////////////////////////

# go env setup
brew install go
go env -w  GOPROXY=https://goproxy.cn,direct

# go env -w  GO111MODULE="on"
# export GOPATH="/Users/zhaoq0103/go/resource"


# 多个git账号提交处理方法
https://blog.csdn.net/luolianxi/article/details/106483748



1. 输入法
2. chrome
3. weixin_work from appstore
4. youdaoyun
5. wps



安装 wget

brew ?

199.232.28.133 raw.githubusercontent.com
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"


更新 homebrew 源
