# cat drugcode.txt| xargs -n 1 sh -c './drugImageExist.sh $1' sh  
# cat failed2.log| xargs -n 1 sh -c './drugImageExist.sh $1' sh


#!/bin/bash

image_url_png="http://pro-yeahgo.oss-cn-shenzhen.aliyuncs.com/crawl/jp_drug/$1/$1.PNG"
image_url_png_1="http://pro-yeahgo.oss-cn-shenzhen.aliyuncs.com/crawl/jp_drug/$1/$1_1.PNG"

image_url_jpg="http://pro-yeahgo.oss-cn-shenzhen.aliyuncs.com/crawl/jp_drug/$1/$1.JPG"
image_url_jpg_1="http://pro-yeahgo.oss-cn-shenzhen.aliyuncs.com/crawl/jp_drug/$1/$1_1.JPG"



# 使用 curl 发送 HEAD 请求检查图片地址是否可访问
response_code1=$(curl -sL -w "%{http_code}" -I "$image_url_jpg" -o /dev/null)

if [ "$response_code1" = "200" ]; then
    echo "$image_url_jpg" "可入库" >> success.log
else
	response_code2=$(curl -sL -w "%{http_code}" -I "$image_url_png" -o /dev/null)
    if [ "$response_code2" = "200" ]; then
		echo "$image_url_png" "可入库" >> success.log
	else

			response_code3=$(curl -sL -w "%{http_code}" -I "$image_url_jpg_1" -o /dev/null)
		    if [ "$response_code3" = "200" ]; then
				echo "$image_url_jpg_1" "可入库" >> success.log
			else
				response_code4=$(curl -sL -w "%{http_code}" -I "$image_url_png_1" -o /dev/null)
			    if [ "$response_code4" = "200" ]; then
					echo "$image_url_png_1" "可入库" >> success.log
				else
					echo "$image_url_jpg" "不可访问" >> failed3.log
				fi
			fi
	fi
fi



