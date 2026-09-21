git docs


《Pro Git》
https://git-scm.com/book/zh/v2



===========对GIT 理解不够深入，遇到问题想不明白原因 ========================================

Your branch and 'origin/main' have diverged, and have 1 and 1 different commits each, respectively


chatGPT给出的解决方案：（可行）

1. 首先，运行 git fetch 命令来获取远程仓库的最新更改，但不会自动合并它们到你的分支。

2. 运行 git log --oneline --decorate --graph --all 命令查看分支之间的提交历史。这将显示分支拓扑结构和提交信息，帮助你了解每个分支的不同提交。

3. 根据你的需求，选择一种处理方式：
如果你希望保留你的本地提交并将其合并到远程仓库的 'main' 分支上，请执行以下操作：
git merge origin/main
这会将远程仓库的 'main' 分支合并到你的本地分支上，并创建一个新的合并提交。
如果你希望放弃你的本地提交并完全采用远程仓库的 'main' 分支，请执行以下操作：
git reset --hard origin/main
这会重置你的本地分支到与远程仓库的 'main' 分支相同的状态。请注意，这将丢弃你本地分支上的所有未提交更改，请确保已经备份了必要的更改。


4. 最后，运行 git push 命令将你的本地分支更新到远程仓库，以便与 'origin/main' 分支保持同步。


===================================================



git push origin serverfix:awesomebranch 来将本地的 serverfix 分支推送到远程仓库上的 awesomebranch 分支。

git checkout -b serverfix origin/serverfix 这样会建立跟踪关系

git checkout -b sf origin/serverfix  // 会自动 --track

git br -vv


变基遵守一条准则：
	如果提交存在于你的仓库之外，而别人可能基于这些提交进行开发，那么不要执行变基


chapter 5.3
看起来有点打瞌睡，后面抽时间再继续吧  20230912