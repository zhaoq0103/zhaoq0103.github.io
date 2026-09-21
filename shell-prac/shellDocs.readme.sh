#!/bin/zsh
######!/bin/bash

# set -x


# 这是单行注释

: '
    这是一个多行注释示例。
    这里可以包含多行文本和注释内容。
'

/***
    没有这个注释
***/

: '
若要在 OS X 终端中将 Option 键视为 alt 键（例如在上面介绍的 alt-b、alt-f 等命令中用到），打开 偏好设置 -> 描述文件 -> 键盘 并勾选“使用 Option 键作为 Meta 键”。

A61618278
'

/*
当你需要对文本文件做集合交、并、差运算时，
sort 和 uniq 会是你的好帮手。具体例子请参照代码后面的，
此处假设 a 与 b 是两内容不同的文件。这种方式效率很高，
并且在小文件和上 G 的文件上都能运用
（注意尽管在 /tmp 在一个小的根分区上时你可能需要 -T 参数，但是实际上 sort 并不被内存大小约束），
参阅前文中关于 LC_ALL 和 sort 的 -u 参数的部分。

*/

      sort a b | uniq > c   # c 是 a 并 b
      sort a b | uniq -d > c   # c 是 a 交 b

      ##将 a 和 b 的内容与 b 合并后排序去重，只保留在 a 中出现的唯一行
      ## 这个求交并补挺好，用于初步的数据分析
      sort a b b | uniq -u > c   # c 是 a - b   



###################################33
echo 1 | sed '\%1%s21232'

这个例子因为用数字做分隔符，看起来有点不容易理解，实际上是这样： echo 1 | sed '/1/s#1#3#'

# mac os 下的sed 增加删除等操作
https://blog.csdn.net/qcx321/article/details/127801932


###################################33



shell 文档
#https://www.runoob.com/linux/linux-shell.html


# https://blog.csdn.net/yetyongjin/article/details/120972642  echo 颜色设置
# https://www.gnu.org/software/  GUN软件说明文档



GREEN="\e[32;1m"
GREEN1="\033[40;32m"
RED1="\033[40;31m"
WHITE="\e[37;1m"
BLACK="\e[30;1m"
RED="\e[31;1m"
YELLOW="\e[33;1m"
BLUE="\e[34;1m"
PURPLE="\e[35;1m"
DARK_GREEN="\e[36;1m"
CLEAR="\e[0m"


function docs(){

# 去重排序等很方便
cut -d: -f 1 | sort | uniq -c | sort -nr  
sort|uniq -c|awk '{print $2}'


# Shell 输入/输出重定向
n >& m  #将输出文件 m 和 n 合并
command > file 2>&1
nohup command > /dev/null 2>&1 &    #含义详解 ,第一个&用来告诉系统,1是STDOUT，不是文件；第二&要后台运行
nohup command > /dev/null 2>&1 &   #的意思就是，将command保持在后台运行，并且将输出的日志忽略

> /dev/null 2>&1: 标准输出和错误输出都被重定向到回收站
2>&1 > /dev/null: 错误输出到终端，标准输出被重定向到回收站
2 和 > 之间不可以有空格，2> 是一体的时候才表示错误输出

在 kubectl apply -f - 命令中，最后的 - 是一个标准输入（stdin）的特殊语法

<< tag # 将开始标记 tag 和结束标记 tag 之间的内容作为输入。
	....
	....
tag

# Here Document 文档说明
# https://zh.wikipedia.org/wiki/Here%E6%96%87%E6%A1%A3#%E5%8F%82%E8%A7%81

Here Document 是 Shell 中的一种特殊的重定向方式
command << delimiter
    document
delimiter



 bash -c "echo '#
    140.252.1.92 aix
    140.252.1.32 solaris
    140.252.1.11 gemini
    140.252.1.4  gateway
    140.252.1.183 netb

    140.252.13.35 bsdi
    140.252.13.33 sun
    140.252.13.34 svr4
    140.252.13.65 slip
    #' >>/etc/hosts"



结尾的delimiter 一定要顶格写，前面不能有任何字符，后面也不能有任何字符，包括空格和 tab 缩进。
开始的delimiter前后的空格会被忽略掉。
}

echo "docs .."
# echo '输入 1 到 4 之间的数字:'
# echo '你输入的数字为:'
# read aNum
# case $aNum in
#     1)  echo '你选择了 1'
#     ;;
#     2)  echo '你选择了 2'
#     ;;
#     3)  echo '你选择了 3'
#     ;;
#     4)  echo '你选择了 4'
#     ;;
#     *)  echo "你没有输入 1 到 4 之间的数字:$aNum"
#     ;;
# esac


# Shell中的 test 命令用于检查某个条件是否成立，它可以进行数值、字符和文件三个方面的测试。



# read 命令从标准输入中读取一行,并把输入行的每个字段的值指定给 shell 变量
# -e 开启转义

# echo "OK! \c" # -e 开启转义 \c 不换行 \n 换行
# echo "It is a test \n" 


# read name 
# echo "$name It is a test"




# echo -e "\033[40;32m ==== color set up  ==== \033[0m"

# echo -e "$GREEN ==== color GREEN test  ==== \033[0m"
# echo -e "$GREEN1 ==== color GREEN1 test  ==== \033[0m"

# echo -e "$RED ==== color RED test  ==== \033[0m"
# echo -e "$RED1 ==== color RED1 test  ==== \033[0m"

# shell 变量   数字，字符串
# 单引号字串中不能出现单独一个的单引号（对单引号使用转义符后也不行），但可成对出现(中间不能出现空格)，作为字符串拼接使用

# str='this is '==' a string'
# echo ${str}

# len=$(`expr index "fdsafdsafdsafds" fs`)
# echo $len

# echo `expr 10 + 5`


# awk -F":" '{print  $5}' /etc/passwd | tail -n 2
# awk '{FS=":"} {print  $5}' /etc/passwd | tail -n 2

# awk '$6 ~ /ESTABLISHED/ || NR==2 {print NR,$4,$5,$6}' OFS="\t" netstat.txt

#打印99乘法表  哦，仔细看一下也很容易理解
# seq 9 | sed 'H;g' | awk -v RS='' '{for(i=1;i<=NF;i++)printf("%dx%d=%d%s", i, NR, i*NR, i==NR?"\n":"\t")}'



# val=`expr 2 + 2`
# echo "两数之和为 : $val"

# Shell 和其他编程语言一样，支持多种运算符，包括：
# 算数运算符  用于比较数字 + - * / = == !=
# 关系运算符 只支持数字，不支持字符串，除非字符串的值是数字 -eq -gt ..
# 布尔运算符 ！ -a -o
# 逻辑运算符  && || 
# 字符串运算符 =    !=   -z   -n   $
# 文件测试运算符 



# a="abc"
# a=""

# if [ -n "$a" ]
# then
#    echo "-n $a : 字符串长度不为 0"
# else
#    echo "-n $a : 字符串长度为 0"
# fi
# if [ $a ]
# then
#    echo "$a : 字符串不为空"
# else
#    echo "$a : 字符串为空"
# fi



# 操作系统差异
# 在 MAC 中 shell 的 expr 语法是：$((表达式))，此处表达式中的 "*" 不需要转义符号 "\" 