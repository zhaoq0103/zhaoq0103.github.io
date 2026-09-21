#!/bin/bash


########################### 查询对账单 ##########################################
# 时间处理

startDate=20210601
endDate=20230630



########################### 下载文档 ##########################################


# set -x

rm ./handleError.log  2>/dev/null
rm ./handleSuccess.log 2>/dev/null
# rm ./result.log 2>/dev/null

########################### check login ##########################################
#这里有了个 ms
token="VXoesn6lKa2W9DdFtwUl0g"


jsonstr1='{"respCode":"90000","respDesc":"成功","retList":[],"pageIndex":1,"totalSize":0}'
jsonstr='{"respCode":"90000","respDesc":"成功","retList":[        {
            "id": 8399087,
            "merCustId": "0226873966603840",
            "batchType": "聚合支付",
            "batchDate": 20210623,
            "batchStat": "S",
            "message": "成功",
            "fileKey": "f262f73e-6aa9-3d45-abe0-01e0d259955e",
            "remark": "jobExecutionId=8422773\nchargeTotalSize=39\nrefundTotalSize=9",
            "createTime": "2021-06-24 03:26:09",
            "updateTime": "2021-06-24 03:26:10"
        }],"pageIndex":1,"totalSize":0}'


function testFunc(){
	api_result=$jsonstr
	retList=$( echo ${api_result} | jq '.retList[0].id'  )
	echo $retList

	# idStr=$(echo ${api_result} | jq '.retList[0].id' )
 #    custIdStr=$(echo ${api_result} | jq '.retList[0].merCustId'|awk -F'"' '{print $2}')
 #    batchDate=$(echo ${api_result} | jq '.retList[0].batchDate')

	# fileUrl="https://file.cloudpnr.com/app-bdef1811-3bad-472f-b76c-90230ad840ed%2F11f95ae2-176f-11ee-82fd-0242ac110003.zip?Expires=1688346675&Signature=0hrd74pZ6yeplgbcu5zrNr62ISI%3D&OSSAccessKeyId=LTAI6Yzq9tIYS57"

	# if [ $fileUrl ];
	# 	then
 #    		echo "$1    \"$fileUrl\" " >> result.log
 #    		wget $fileUrl -O BillCheckLog/$1.zip 2>BillCheckLog/$1.error

 #    		# retCode=`eval wget \"$fileUrl\" -O BillCheckLog/$1.zip 2>/dev/null`
 #    		# echo "retCode: $retCode"  

 #    		# if [ reCode != 0 ];
 #    		# 	then mv BillCheckLog/$1.zip BillCheckLog/$1.error
 #    		# fi		
 #    fi
}

# testFunc 20210701
# exit;

# getBillDocsInfo(idStr,custIdStr)
function getBillDocsInfo(){
	cmd="curl -s -X GET --location 'https://api-console.adapay.tech/crhcsl/bill/download?' \
	--header 'Token: ${token}' \
	--header 'Host: api-console.adapay.tech' \
	--header 'Origin: https://console.adapay.tech' \
	--header 'Authorization: api_live_XXXX' \
	--header 'Sec-Fetch-Dest: empty' \
	--header 'Sec-Fetch-Mode: cors' \
	--header 'Sec-Fetch-Site: same-site' \
	--form 'id=$2' \
	--form 'custId="0226873966603840"' "


	if [ "0226873966603840" = $3 ];
		then echo "Date:$1  , custId not changed:$3"
	fi

    api_result=`eval ${cmd}` # 执行cmd，拿到返回的结果
    fileUrl=$( echo ${api_result} | jq '.fileUrl')

    #http://null/
    if [ ${#fileUrl} -gt 12 ];
    	then
    		echo "$1    $fileUrl " >> log/result.log 
    		# bash -c 'wget $fileUrl -O BillCheckFolder/$1.zip'
    		bash -c "wget $fileUrl -O BillCheckFolder/$1.zip 2>log/$1.log"
    fi

}



# getHuifuPayBillChecker(dateStr)
# 这个接口可以批量获取数据
function getHuifuPayBillChecker(){
	cmd="curl -s -X GET --location 'https://api-console.adapay.tech/crhcsl/bill/list' \
	--header 'Token: ${token}' \
	--header 'Authorization: api_live_XXXX' \
	--form 'startDate=$1' \
	--form 'endDate=$1' \
	--form 'downloadType="CSV"' \
	--form 'pageIndex="1"' \
	--form 'pageSize="10"' "

    api_result=`eval ${cmd}` # 执行cmd，拿到返回的结果
    idStr=$(echo ${api_result} | jq '.retList[0].id' )
    custIdStr=$(echo ${api_result} | jq '.retList[0].merCustId'|awk -F'"' '{print $2}')
    batchDate=$(echo ${api_result} | jq '.retList[0].batchDate')

    if [ $idStr -gt 0 ]
    	then	getBillDocsInfo $1 $idStr $custIdStr
    	else echo "parse apiresult error..."
	fi
}


function handleDateFormat(){
	startDate=20230102
	endDate=20230701

	# for((i=0;i<40;i++)); 
	loopStep=0
	dateStr=$startDate
	while(( $dateStr < $endDate ))
	do
		dateStr=$(echo `date -j -f "%Y%m%d"  -v+${loopStep}d $startDate +"%Y%m%d"` )
		# echo $dateStr
		if [ $dateStr -le $endDate ];
			# then echo "right date:$dateStr"  
			then getHuifuPayBillChecker $dateStr
		fi

		# loopStep=$loopStep+1
		#let "loopStep++" #这个值为什么会累加？
		let loopStep+=1
	done 
}


handleDateFormat

exit;




if [ -f "./handleError.log" ]; 
	then echo -e  "\033[31m ==== error hanppen  ==== \033[0m" && cat handleError.log 
fi


echo -e "\033[40;32m ==== handle over, if success , result at ./handleSuccess.log  ==== \033[0m"