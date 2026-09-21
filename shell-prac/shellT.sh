#!/bin/zsh
######!/bin/bash

# set -x


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
# Shell 输入/输出重定向
n >& m  #将输出文件 m 和 n 合并
command > file 2>&1
nohup command > /dev/null 2>&1 &    #含义详解 ,第一个&用来告诉系统1是STDOUT，不是文件；第二&要后台运行
nohup command > /dev/null 2>&1 &   #的意思就是，将command保持在后台运行，并且将输出的日志忽略

> /dev/null 2>&1: 标准输出和错误输出都被重定向到回收站
2>&1 > /dev/null: 错误输出到终端，标准输出被重定向到回收站
2 和 > 之间不可以有空格，2> 是一体的时候才表示错误输出

<< tag # 将开始标记 tag 和结束标记 tag 之间的内容作为输入。
	....
	....
tag

Here Document 是 Shell 中的一种特殊的重定向方式
command << delimiter
    document
delimiter

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