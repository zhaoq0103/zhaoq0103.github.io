#!/bin/bash




 # 用jq 替换换 awk ,需要知道JSON数据的内部结构
 # https://stedolan.github.io/jq/  官方文档
 # https://stedolan.github.io/jq/manual/  使用举例

# set -x


function login(){

	# form 形式是可以的
	# cmd="curl -s -X POST --location 'https://adminapi.yeahgo.com/auth/login/checkLogin' \
	# 	--form 'name="test"' \
	# 	--form 'passwd="tY53LiAYsG3lLpe"'"
	# apiresult=`eval ${cmd}`

	# apiresult='{"code":0,"msg":"登录成功","success":true,"time":1684470511,"data":{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwibmlja25hbWUiOiJhZG1pbmlzdHJhdG9yIiwidGFyZ2V0X2lkIjoiMTQxMjk0MTE1NDY3MDk5NzUwNCIsImdyb3VwX3RpdGxlIjoiXHU4ZDg1XHU3ZWE3XHU3YmExXHU3NDA2XHU1NDU4IiwiZ3JvdXBfaWQiOjEsImF1dGhfa2V5IjoiYWRtaW5faW5mbzozMDBiNDYxNC04MjMxLTQ0ZDYtMDFiMS1lMTc0MzJlNzU5Y2EiLCJpYXQiOjE2ODQ0NzA1MTEsImV4cCI6MTY4NDQ5OTMxMX0.isKznwqJiqafux5jMsGtmdbsA-1AHdgyyJmmDYO052w","id":1,"username":"admin","nickname":"administrator","images":null,"targetId":"1412941154670997504"}}'
   
   #  -X POST  	
   # 直接执行也是可以的
	# cmd=`curl -s --location 'https://adminapi.yeahgo.com/auth/login/checkLogin' \
	# --header 'Content-Type: application/json' \
	# --data '{	
	# 	"name":"test",	
	# 	"passwd":"tY53LiAYsG3lLpe"
	# }' `

	#使用json 文件最好， 一方面方便脱敏，另一方面方便供其它人使用,应该可以根据配置取不同的文件
	cmd="curl -s --location 'https://adminapi.yeahgo.com/auth/login/checkLogin' \
	--header 'Content-Type: application/json' \
	--data @adminpara.json "

   apiresult=`eval ${cmd}`
   # apiresult=${cmd}
   # echo $apiresult
   admintoken=$(echo $apiresult | jq .data.token) # 执行cmd，拿到返回的结果
   echo -e "\033[31m   token:$admintoken    \033[0m"
}




function checkRecomonder(){
	admintoken="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJ1c2VybmFtZSI6InRlc3QiLCJuaWNrbmFtZSI6InRlc3QiLCJ0YXJnZXRfaWQiOiIxNDUzMjQyNDg4NTIyNjQ1NTA0IiwiZ3JvdXBfdGl0bGUiOiJcdTRlMzRcdTY1ZjYwMDEiLCJncm91cF9pZCI6MTQsImF1dGhfa2V5IjoiYWRtaW5faW5mbzowOTIzNmRkYy0wZDgzLTAyYzItMjE0Yi1lYWJkNzQ2ZjQ3M2YiLCJpYXQiOjE2ODU0MzIwMzMsImV4cCI6MTY4NTQ2MDgzM30.OAvVVyGrkf_CRCs2HaRWAxHvo57Z7jIOelrS8ze8Cl8"
	
	# success
	cmd="curl -s -X POST --location 'https://adminapi.yeahgo.com/auth/java-admin/memberInfo/getInviteList' \
	--header 'admin-token: ${admintoken}' \
	--form 'phoneNumber="13076680067"' \
	--form 'page="1"' \
	--form 'size="20"'"

	#failed
	# cmd="curl -s -X POST --location 'https://adminapi.yeahgo.com/auth/java-admin/memberInfo/getInviteList' \
	# --header 'admin-token: ${admintoken}' \
	# --header 'Content-Type: application/json' \
	# --data '{
	# 		"phoneNumber":"13076680067",
	# 		"page":"1",
	# 		"size":"20"
	# 	}' "

	# json文件中不能再使用变量，如果不使用变量，可以请求成功，否则失败
	# phoneNumber="13076680067"
	# cmd="curl -s -X POST --location 'https://adminapi.yeahgo.com/auth/java-admin/memberInfo/getInviteList' \
	# --header 'Content-Type: application/json' \
	# --header 'admin-token: ${admintoken}' \
	# --data @apipara.json "

   api_result=`eval ${cmd}` # 执行cmd，拿到返回的结果
   # echo ${api_result}

	reconGet=$( echo ${api_result} | jq .data.memberInviteInfoDTO.invitePhoneNumber|awk -F'"' '{print $2}')
	echo -e "\033[31m   推荐人是:$reconGet    \033[0m"
}


function testApi(){
	login
	# checkRecomonder
}

testApi
exit




function get_json(){
   # echo "${1//\"/}" | sed "s/.*$2:\([^,}]*\).*/\1/"
   echo "get_json"
}


function deal_json(){
        # cmd=""
        # echo ${cmd}
        # api_result=`eval ${cmd}`
        #api_result='{"code":0,"msg":"success","success":true,"time":1684315498,"data":{"list":{"page":1,"records":[],"size":20,"total":0,"totalPage":0},"memberInviteInfoDTO":{"aedLeaderPhone":"13425222366","beFrTime":null,"inviteCount":0,"inviteNickName":"130****4133","invitePhoneNumber":"13076624133","isAEDLeader":0,"isTeamLeader":0,"memberShopType":0,"nickName":"158****1379","phoneNumber":"15816241379","subsidiaryName":"清远市凯诚科技有限公司","teamLeaderPhone":"","userType":0,"vipStore":0}}}'
        #value=$(get_json "${api_result}" "time")  从api_result中获取status对应的值


        isVipApiResult='{"code":0,"msg":"success","success":true,"time":1684481269,"data":{"size":10,"page":1,"total":1,"totalPage":1,"records":[{"id":124530,"storeId":124530,"applyId":867,"storeNo":"store_m_124530","shopMemberAccount":"湘6-126","memberId":"1421038471102824450","nickname":"大栗港社区店","realname":"张叶青","storeName":"大栗港社区店","storeLogo":"https:\/\/pro-yeahgo-secret-oss.yeahgo.com\/user\/0a78e75ae772419ebc165658fe750989.jpg","storeHours":"","linkman":"张叶青","phone":"189****2128","memberPhone":"18973752128","provinceId":1827,"cityId":1905,"regionId":1909,"houseNumber":"罗溪路123号","areaInfo":{"1827":"湖南省","1905":"益阳市","1909":"桃江县"},"areaInfoStr":"湖南省 益阳市 桃江县","address":"桃花江镇罗溪路123号宾购直营店","fulladdressRear":"桃花江镇罗溪路123号宾购直营店 桃花江镇 罗溪路123号","longitude":"112.154069","latitude":"28.512051","status":{"code":1,"desc":"已启用"},"statusDesc":"已启用","operationId":375,"operationCompanyName":"桃江运营中心","memberShopType":{"code":30,"desc":"生活馆"},"memberShopTypeStr":"生活馆","remark":"","applyType":{"code":10,"desc":"正常申请"},"createTime":"2021-09-15 14:55:59","updateTime":"2022-07-30 20:52:00","deposit":0,"depositYuan":0,"serviceFee":0,"serviceFeeYuan":0,"iotType":"","vip":1,"isVip":1,"vipDesc":"是","vipSource":"upgrade","vipExpireTime":"2023-07-30","vipRemainingDay":72,"servicefeeType":"已确认保证金转服务费","isLifeHouse":1,"lifeHouseOpenTime":"","lifeHouseExpireTime":"2023-07-30","lifeHouseRemainingDay":72,"provideTime":"2021-09-15 14:55:51","lifeHouseFreeOpen":0,"lifeHouseFreeOpenDesc":"缴授权费开通","packagePeriodAmount":0,"packagePeriodAmountYuan":0,"packageTitle":"","packagePeriod":"","lifeHouseContractStatus":"未签写","contractId":0,"auditTime":"2021-09-15 17:05:34","cancleTime":"","isChangePrice":0,"depositRefendList":[{"id":2347,"refendOrderNo":"202207307512019995349931515","refendTime":"2022-07-30 20:52:00","refendAmount":80000,"refendType":3,"optAdminName":"","optAdminId":"1421038471102824450","attach":{"empty":1}}],"depositStatusDesc":"正常未退保证金","freshApplyRow":{"id":0,"verifyStatus":{"code":0,"desc":""}},"isStationManager":0,"cancelInfo":{"id":0},"communityName":"桃花江镇","score":0,"shopMemberLevelName":"五星店主","level":{"level":5,"levelName":"五星店主","levelShow":"V5","icon":"http:\/\/www.44444.png"},"wholeTotal":4,"productTotal":4,"saleOrderTotal":1235,"userTotal":13,"shopkeeperInvitedTotal":111,"statusAction":0,"productList":[],"isJoinLoveFeedback":2}]}}'
		  vipResult=$( echo ${isVipApiResult} | jq .data.records[0].isVip)

        echo "vipResult:$vipResult"

        if [ $vipResult = "1" ];
			then echo "T == isVip" 
			else echo "F == not Vip"
		fi

}


recons=("13338392656" "13707048615" "13765261758" "13765261758" "13973781421" )
reconsRemoveDup=($(awk -v RS=' ' '!newArray[$1]++' <<< ${recons[@]}))



arrayLen=${#reconsRemoveDup[@]}
index=0
for((;index<arrayLen;index++));
do
	# cmd="curl -s --location 'https://adminapi.yeahgo.com/auth/store/memberShop/page' \
	# --header 'admin-token: ${admintoken}' \
	# --form 'memberPhone=${reconsRemoveDup[index]}' \
	# --form 'operation="normal"' \
	# --form 'page="1"' \
	# --form 'size="20"'"
 #    isVipApiResult=`eval ${cmd}` # 执行cmd，拿到返回的结果
 #    # isVipApiResult='{"code":0,"msg":"success","success":true,"time":1684481269,"data":{"size":10,"page":1,"total":1,"totalPage":1,"records":[{"id":124530,"storeId":124530,"applyId":867,"storeNo":"store_m_124530","shopMemberAccount":"湘6-126","memberId":"1421038471102824450","nickname":"大栗港社区店","realname":"张叶青","storeName":"大栗港社区店","storeLogo":"https:\/\/pro-yeahgo-secret-oss.yeahgo.com\/user\/0a78e75ae772419ebc165658fe750989.jpg","storeHours":"","linkman":"张叶青","phone":"189****2128","memberPhone":"18973752128","provinceId":1827,"cityId":1905,"regionId":1909,"houseNumber":"罗溪路123号","areaInfo":{"1827":"湖南省","1905":"益阳市","1909":"桃江县"},"areaInfoStr":"湖南省 益阳市 桃江县","address":"桃花江镇罗溪路123号宾购直营店","fulladdressRear":"桃花江镇罗溪路123号宾购直营店 桃花江镇 罗溪路123号","longitude":"112.154069","latitude":"28.512051","status":{"code":1,"desc":"已启用"},"statusDesc":"已启用","operationId":375,"operationCompanyName":"桃江运营中心","memberShopType":{"code":30,"desc":"生活馆"},"memberShopTypeStr":"生活馆","remark":"","applyType":{"code":10,"desc":"正常申请"},"createTime":"2021-09-15 14:55:59","updateTime":"2022-07-30 20:52:00","deposit":0,"depositYuan":0,"serviceFee":0,"serviceFeeYuan":0,"iotType":"","vip":1,"isVip":1,"vipDesc":"是","vipSource":"upgrade","vipExpireTime":"2023-07-30","vipRemainingDay":72,"servicefeeType":"已确认保证金转服务费","isLifeHouse":1,"lifeHouseOpenTime":"","lifeHouseExpireTime":"2023-07-30","lifeHouseRemainingDay":72,"provideTime":"2021-09-15 14:55:51","lifeHouseFreeOpen":0,"lifeHouseFreeOpenDesc":"缴授权费开通","packagePeriodAmount":0,"packagePeriodAmountYuan":0,"packageTitle":"","packagePeriod":"","lifeHouseContractStatus":"未签写","contractId":0,"auditTime":"2021-09-15 17:05:34","cancleTime":"","isChangePrice":0,"depositRefendList":[{"id":2347,"refendOrderNo":"202207307512019995349931515","refendTime":"2022-07-30 20:52:00","refendAmount":80000,"refendType":3,"optAdminName":"","optAdminId":"1421038471102824450","attach":{"empty":1}}],"depositStatusDesc":"正常未退保证金","freshApplyRow":{"id":0,"verifyStatus":{"code":0,"desc":""}},"isStationManager":0,"cancelInfo":{"id":0},"communityName":"桃花江镇","score":0,"shopMemberLevelName":"五星店主","level":{"level":5,"levelName":"五星店主","levelShow":"V5","icon":"http:\/\/www.44444.png"},"wholeTotal":4,"productTotal":4,"saleOrderTotal":1235,"userTotal":13,"shopkeeperInvitedTotal":111,"statusAction":0,"productList":[],"isJoinLoveFeedback":2}]}}'
	# vipResult=$( echo ${isVipApiResult} | jq .data.records[0].isVip)

 #    echo "vipResult:$vipResult"

 #    if [ $vipResult = "1" ];
	# 	then echo "T == isVip" 
	# 	else echo "F == ${reconsRemoveDup[index]} not Vip,不符合分成条件" >> handleError.log
	# fi
	echo "handle ${reconsRemoveDup[index]}"

done


exit


# POST方式. -X POST
# curl发送post请求，默认的content-type是：application/x-www-form-urlencoded
# curl -X POST http://localhost:8080/api -H "Content-Type: application/json" -d @sendfile.json


curl --location 'https://xiyingzhou.xyzjkjt.com/health/api/third/register' \
--header 'Content-Type: application/json' \
--data '{
 "token":"c2f4e1a1f63e529edd67d366b9a2545f",
 "name":"肖小试",
 "gender":"1",
 "birthday":"1992-04-10",
 "height":"168",
 "weight":"60",
 "mobile":"18123858273",
 "orgId":"3725b22b-a4ab-4b04-8b84-1250a5a58d28",
 "packageId":"1847c4a1-0ee9-43ac-8507-db0e0c85329c",
 "openid":"oB4nYjvY13SVtaWC-AFztM2f3XBB",
 "device":"1208652612cf6ce5_20230224093404"
}'
























