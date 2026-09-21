# github ssh config


# 1. 生成密钥对
ssh-keygen -t ed25519 -C "OnTheWay0103@163.com"
ssh-keygen -t ed25519 -C "zhaoq0103@163.com"

# 2. 把公钥配置到 github (多台电脑是不是每台电脑单独生成keys比较安全)
# 如果修改生成的密钥文件文件名， 需要更新密钥配置文件


# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIKRBiy5JFhcvteiGMetdagh+iFikRQHJwSDfyPBeKHFH  #mac mini onTheWay@github - pub key
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIETxGuTwJ7qqX6700cWDr7BxbMBA+4IG3cX/TiVZ9Luu  #mac mini zhaoq0103@github - pub key

# win-pc ssh pubkey
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAILhWKHLhR4sbTqHL0MLb3P7/pjvqDqyzXaxD9M1OSeZ6 关联了zhaoq0103@github
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIMFEt2sgLyOYiBBdvxJ6AwxeNlkUzZOknyuzz2WjxHu4 关联了OnTheWay@github

# 3. 本地的ssh 配置文件，主要是别名和关联的key 文件路径
# linux mac 平台
Host github-ontheway  #这里github-ontheway 是别名，是有用的，可以用来区分多个不同的账号和仓库等
  HostName github.com
  User git 
  IdentityFile ~/.ssh/id-ed25519-github-ontheway # 更新路径
  IdentitiesOnly yes


Host github-zhaoq0103  
  HostName github.com
  User git 
  IdentityFile ~/.ssh/id-ed25519-github-zhaoq0103 # 更新路径
  IdentitiesOnly yes


# win 平台
# 启动 SSH 代理
eval "$(ssh-agent -s)"

# 将私钥添加到代理
ssh-add ~/.ssh/id_ed25519
ssh-add ~/.ssh/id_ed25519_ontheway

# ~/.ssh/config  配置文件
echo '## config file
Host zhaoq0103.github.com
  HostName github.com
  User git
  IdentityFile ~/.ssh/id_ed25519

Host ontheway.github.com
  HostName github.com
  User git
  IdentityFile ~/.ssh/id_ed25519_ontheway

' > ~/.ssh/config


# 4. 测试连通性
ssh -Tv git@github-ontheway   # 这里用的是别名
ssh -Tv git@github-zhaoq0103  # 这里用的是别名


# 5. git 仓库 改用SSH 链接
# git remote -v  
git remote set-url origin git@github-ontheway:OnTheWay0103/How-to-Make-Money.git
git remote set-url origin git@ssh_config_alias:username/AIProjects.git
git remote add origin git@github-ontheway:OnTheWay0103/AIProjects.git （add 与 set-url 有区别）



# git config

# 注意保持多端用同一个分支，都使用 main 分支， 不再使用 master 分支

# 把远程的master分支合并到当前main分支
  git fetch origin master
  git merge origin/master


  或者
  git pull origin/master



	git branch -u origin/<远程分支名> <本地分支名>
	git branch -u origin/main main  # 关联本地 main 与远程 origin/main
	git checkout -b main --track origin/main



# 全局设置（默认）
git config --global user.name "Your Name"
git config --global user.email "personal@example.com"

# 在单独的仓库区设置
## 当前项目有效
git config --local  user.name "OnTheWay0103"
git config --local  user.email "OnTheWay0103@163.com"

## 当前项目有效
git config --local  user.name "zhaoq0103"
git config --local  user.email "zhaoq0103@163.com"


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
git config --global --list  #列出全局配置 
git config --global --unset user.name
git config --global --unset user.email


