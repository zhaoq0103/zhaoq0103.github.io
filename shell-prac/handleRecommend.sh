#!/bin/bash

# set -x

rm ./handleError.log  2>/dev/null
rm ./handleSuccess.log 2>/dev/null

########################### check login ##########################################

timestamp=1685436751
if [ -f "./updatetime.log" ]; 
	then timestamp=$(cat ./updatetime.log) 
fi

now=$(echo `date '+%s'`)
diffTime=($now-$timestamp)
admintoken="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwibmlja25hbWUiOiJhZG1pbmlzdHJhdG9yIiwidGFyZ2V0X2lkIjoiMTQxMjk0MTE1NDY3MDk5NzUwNCIsImdyb3VwX3RpdGxlIjoiXHU4ZDg1XHU3ZWE3XHU3YmExXHU3NDA2XHU1NDU4IiwiZ3JvdXBfaWQiOjEsImF1dGhfa2V5IjoiYWRtaW5faW5mbzo2YmViMDJkZC1jZWJjLThhMjQtNjMwNS0xNTA5Y2Q0YTg2MDgiLCJpYXQiOjE2ODU0Mzc1NTEsImV4cCI6MTY4NTQ2NjM1MX0.zQNkWrv4ZU8neh5foXgGrQRFU1xkz76rrRiYBazvCYU"
# echo "now:$now , timestamp:$timestamp, diffTime:$diffTime"

function update_token(){
	# cmd="curl -s --location 'https://adminapi.yeahgo.com/auth/login/checkLogin' \
	# --form 'name="admin"' \
	# --form 'passwd="xxxxx"' "

	cmd="curl -s --location 'https://adminapi.yeahgo.com/auth/login/checkLogin' \
	--header 'Content-Type: application/json' \
	--data @adminpara.json "


	apiresult=`eval ${cmd}`
	# apiresult='{"code":0,"msg":"登录成功","success":true,"time":1684470511,"data":{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwibmlja25hbWUiOiJhZG1pbmlzdHJhdG9yIiwidGFyZ2V0X2lkIjoiMTQxMjk0MTE1NDY3MDk5NzUwNCIsImdyb3VwX3RpdGxlIjoiXHU4ZDg1XHU3ZWE3XHU3YmExXHU3NDA2XHU1NDU4IiwiZ3JvdXBfaWQiOjEsImF1dGhfa2V5IjoiYWRtaW5faW5mbzozMDBiNDYxNC04MjMxLTQ0ZDYtMDFiMS1lMTc0MzJlNzU5Y2EiLCJpYXQiOjE2ODQ0NzA1MTEsImV4cCI6MTY4NDQ5OTMxMX0.isKznwqJiqafux5jMsGtmdbsA-1AHdgyyJmmDYO052w","id":1,"username":"admin","nickname":"administrator","images":null,"targetId":"1412941154670997504"}}'
    admintoken=$(echo $apiresult | jq .data.token|awk -F'"' '{print $2}') # 执行cmd，拿到返回的结果, 去除引号,晕，因为引号的问题，调试了两个小时
    # echo "admintoken:$admintoken"
    echo "$admintoken" > token.log
    echo "update_token .."
}

function update_updatetime(){
	echo $now > updatetime.log
	echo "update_updatetime"
}

if [[ $diffTime -gt 60*60*24 ]]; 
	then
		#update token
		update_token
		update_updatetime
	else
		echo 'already loginned'		
fi



if [[ ${#admintoken} -le 0 ]]; then
	echo "token error "
	exit
fi

# exit;
########################### 查询推荐人 ##########################################

# users=("13959018890" "15387787773" "13881899328" "18273726857" "13688018289" "15208114370" "18200469705" "15812532347")
# recons=("13338392656" "13707048615" "13765261758" "13973781421" "18200581926" "18200581926" "18200581926" "18973752128")

# or
# users=(
# 	"13959018890" 
# 	"15387787773" 
# 	"13881899328" 
# )

# recons=(
# 	"13338392656" 
# 	"13707048615" 
# 	"13765261758"
# )


#从文件中获取数据
# https://blog.csdn.net/yogima/article/details/128833660 参考
users=($(cat ./users.data))
recons=($(cat ./recommender.data))


arrayLen=${#users[@]}
index=0

# 查直推人
# admin-token 需要根据情况更新,注意双引号的问题
for((;index<arrayLen;index++));
do
	echo "handle index:$index, user: ${users[index]}, recons: ${recons[index]}"

	# 之前有一段时间可用，忽然又不可行了，只知道是字符串处理的问题，没找到解决方法（token 引号的问题，也是一个崩溃）
	# cmd="curl -s -X POST --location 'https://adminapi.yeahgo.com/auth/java-admin/memberInfo/getInviteList' \
	# --header 'admin-token: ${admintoken}' \
	# --form 'phoneNumber=${users[index]}' \
	# --form 'page="1"' \
	# --form 'size="20"'"

	cmd="curl -s -X POST --location 'https://adminapi.yeahgo.com/auth/java-admin/memberInfo/getInviteList' \
	--header 'admin-token: ${admintoken}' \
	--form 'phoneNumber=${users[index]}' \
	--form 'page="1"' \
	--form 'size="20"' "

    api_result=`eval ${cmd}` # 执行cmd，拿到返回的结果
        
    # 下面这个方法也可以正常执行，就是需要手工处理命令字符串，比较繁琐
	# api_result=`curl --location "https://adminapi.yeahgo.com/auth/java-admin/memberInfo/getInviteList" \
	# --header "admin-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwibmlja25hbWUiOiJhZG1pbmlzdHJhdG9yIiwidGFyZ2V0X2lkIjoiMTQxMjk0MTE1NDY3MDk5NzUwNCIsImdyb3VwX3RpdGxlIjoiXHU4ZDg1XHU3ZWE3XHU3YmExXHU3NDA2XHU1NDU4IiwiZ3JvdXBfaWQiOjEsImF1dGhfa2V5IjoiYWRtaW5faW5mbzo3ZjVhNGY2OS00MmZlLTM4YjYtN2Y1OS02NTM3MWM1Mzk5OGUiLCJpYXQiOjE2ODQzNzcwNTIsImV4cCI6MTY4NDQwNTg1Mn0.Evvwb8WRSax_unkmFZ4DJgxuSGEFGDjWtZPkhOJNCKA" \
	# --form "phoneNumber=${users[index]}" \
	# --form "page=\"1\"" \
	# --form "size=\"20\"" `

	# reconGet=$( echo ${api_result} | awk -F'invitePhoneNumber' '{print $2}' | awk -F'"' '{print $3}')
	# jq 的结果中含有双引号，用 awk -F'"' '{print $2} 去除
	# echo ${api_result}

	reconGet=$( echo ${api_result} | jq .data.memberInviteInfoDTO.invitePhoneNumber|awk -F'"' '{print $2}')
	reconGiven=${recons[index]}

	if [ "$reconGet" = "$reconGiven" ]
		then `echo "T == No:$index  ${users[index]} 查询推荐人是  $reconGet ,给出的数据是 $reconGiven" >> handleSuccess.log`
		else `echo "F == No:$index  ${users[index]} 查询推荐人是  $reconGet ,给出的数据是 $reconGiven" >> handleError.log`
	fi

done

# exit;

########################### 查询推荐人是否是VIP ##########################################
# recons=("13338392656" "13707048615" "13765261758" "13973781421" "18200581926" "18200581926" "18200581926" "18973752128")
reconsRemoveDup=($(awk -v RS=' ' '!newArray[$1]++' <<< ${recons[@]}))
arrayLen=${#reconsRemoveDup[@]}
index=0
for((;index<arrayLen;index++));
do
	cmd="curl -s --location 'https://adminapi.yeahgo.com/auth/store/memberShop/page' \
	--header 'admin-token: ${admintoken}' \
	--form 'memberPhone=${reconsRemoveDup[index]}' \
	--form 'operation="normal"' \
	--form 'page="1"' \
	--form 'size="20"'"
    isVipApiResult=`eval ${cmd}` # 执行cmd，拿到返回的结果
    # isVipApiResult='{"code":0,"msg":"success","success":true,"time":1684481269,"data":{"size":10,"page":1,"total":1,"totalPage":1,"records":[{"id":124530,"storeId":124530,"applyId":867,"storeNo":"store_m_124530","shopMemberAccount":"湘6-126","memberId":"1421038471102824450","nickname":"大栗港社区店","realname":"张叶青","storeName":"大栗港社区店","storeLogo":"https:\/\/pro-yeahgo-secret-oss.yeahgo.com\/user\/0a78e75ae772419ebc165658fe750989.jpg","storeHours":"","linkman":"张叶青","phone":"189****2128","memberPhone":"18973752128","provinceId":1827,"cityId":1905,"regionId":1909,"houseNumber":"罗溪路123号","areaInfo":{"1827":"湖南省","1905":"益阳市","1909":"桃江县"},"areaInfoStr":"湖南省 益阳市 桃江县","address":"桃花江镇罗溪路123号宾购直营店","fulladdressRear":"桃花江镇罗溪路123号宾购直营店 桃花江镇 罗溪路123号","longitude":"112.154069","latitude":"28.512051","status":{"code":1,"desc":"已启用"},"statusDesc":"已启用","operationId":375,"operationCompanyName":"桃江运营中心","memberShopType":{"code":30,"desc":"生活馆"},"memberShopTypeStr":"生活馆","remark":"","applyType":{"code":10,"desc":"正常申请"},"createTime":"2021-09-15 14:55:59","updateTime":"2022-07-30 20:52:00","deposit":0,"depositYuan":0,"serviceFee":0,"serviceFeeYuan":0,"iotType":"","vip":1,"isVip":1,"vipDesc":"是","vipSource":"upgrade","vipExpireTime":"2023-07-30","vipRemainingDay":72,"servicefeeType":"已确认保证金转服务费","isLifeHouse":1,"lifeHouseOpenTime":"","lifeHouseExpireTime":"2023-07-30","lifeHouseRemainingDay":72,"provideTime":"2021-09-15 14:55:51","lifeHouseFreeOpen":0,"lifeHouseFreeOpenDesc":"缴授权费开通","packagePeriodAmount":0,"packagePeriodAmountYuan":0,"packageTitle":"","packagePeriod":"","lifeHouseContractStatus":"未签写","contractId":0,"auditTime":"2021-09-15 17:05:34","cancleTime":"","isChangePrice":0,"depositRefendList":[{"id":2347,"refendOrderNo":"202207307512019995349931515","refendTime":"2022-07-30 20:52:00","refendAmount":80000,"refendType":3,"optAdminName":"","optAdminId":"1421038471102824450","attach":{"empty":1}}],"depositStatusDesc":"正常未退保证金","freshApplyRow":{"id":0,"verifyStatus":{"code":0,"desc":""}},"isStationManager":0,"cancelInfo":{"id":0},"communityName":"桃花江镇","score":0,"shopMemberLevelName":"五星店主","level":{"level":5,"levelName":"五星店主","levelShow":"V5","icon":"http:\/\/www.44444.png"},"wholeTotal":4,"productTotal":4,"saleOrderTotal":1235,"userTotal":13,"shopkeeperInvitedTotal":111,"statusAction":0,"productList":[],"isJoinLoveFeedback":2}]}}'
	# echo ${isVipApiResult} 

	vipResult=$( echo ${isVipApiResult} | jq .data.records[0].isVip)

    if [ "$vipResult" = "1" ];
		then echo "T == ${reconsRemoveDup[index]} isVip, 符合分成条件" >> handleSuccess.log
		else echo "F == ${reconsRemoveDup[index]} not Vip,不符合分成条件" >> handleError.log
	fi

done


########################### 提示处理结果 ##########################################
# https://blog.csdn.net/yetyongjin/article/details/120972642  echo 颜色设置

if [ -f "./handleError.log" ]; 
	then echo -e  "\033[31m ==== error hanppen  ==== \033[0m" && cat handleError.log 
fi


echo -e "\033[40;32m ==== handle over, if success , result at ./handleSuccess.log  ==== \033[0m"