



apihost="https://textapi.fadada.com/api2/"


coms=(
"贵州汇能君丽群健康咨询有限公司"
"贵州国焱圣世健康咨询服务有限公司"
"抚州市晖腾网络科技有限公司"
"贵州祥军健康服务有限公司"
"安顺明鸿君集科技有限公司"
"长汀县宜贝健康科技有限公司"
"重庆蔼美健康科技有限公司"
"衡阳翔崇电子商务有限公司"
"衡阳翔崇电子商务有限公司"
"抚州霖齐网络科技有限公司"
)


urls=(
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20240325154035&v=2.0&msg_digest=N0IxMTI1MzExQTEwQkI0Mjk4OEIxODYwRUM2OUQwMUE4OTM5OEIxMg==&transaction_id=u_tr_fdd_u1421297336172302337_20240325154032&send_app_id=null 
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20240325085030&v=2.0&msg_digest=NkQ3NDU3NTNDNjMwMUJDNDJFMEJGQTRBNTcxQzIxQjZGMjNGRkI1Qg==&transaction_id=u_tr_fdd_u1722965371291758593_20240325085027&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20240324090728&v=2.0&msg_digest=MjI2RTkwMjQ1NkVFNjgyRjVENzUxNjQwNzZGMTM1RTUyNkNDOTE3Rg==&transaction_id=u_tr_fdd_u1407882628114743298_20240324090725&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20240321165900&v=2.0&msg_digest=QzA4QjkyMUFDMjYzMUREQUZCOERGRkQwQjNGRUNGMjEwMDc0Q0UzNw==&transaction_id=u_tr_fdd_u1741699461517213697_20240321165856&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20240314134630&v=2.0&msg_digest=MUJEMTdDMTlEMzhGMjVCMjQ2NDRCMEVGNUYwNTAyN0M5NTdEODA1Qg==&transaction_id=u_tr_fdd_u1437975743230447617_20240314134627&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20240115101447&v=2.0&msg_digest=MTgyMDlFNEE2RDFFRjU4NkI2MkY5NzZBNDlGMjIzMUFDOTg3QzFCNA==&transaction_id=u_tr_fdd_u1421642466448080898_20240115101444&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20231228160921&v=2.0&msg_digest=N0M3NTZBNUFBQkZFQjk4RUIxMTg4QzM3MTVENTRGQUM3MTRGMTM5Rg==&transaction_id=u_tr_fdd_u1437607285336727554_20231228160918&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20231223190458&v=2.0&msg_digest=OUQ2OTBCQjAwNDAwRUFFRUUyREZDRkZFQkIzQTQ0ODcxRDdERjlGMg==&transaction_id=u_tr_fdd_u1421391890653270017_20231223190455&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20231222142142&v=2.0&msg_digest=RTIxMUYzRjQxNEM2Njk5NDEwMEQzOUZCMDBBMkI1MkE5MkNDMkJCMw==&transaction_id=u_tr_fdd_u1515921320894988289_20231222142139&send_app_id=null   
https:\/\/textapi.fadada.com\/api2\/\/viewdocs.action?app_id=501776&timestamp=20231221165940&v=2.0&msg_digest=QURBRjgxMDNEMjM3RjIxNDdERTcwNTgzNDNFQkU3OTJCQ0IzMDM5Mw==&transaction_id=u_tr_fdd_u1421659895748513794_20231221165938&send_app_id=null   
)


arrayLen=${#urls[@]}
index=0
for ((;index<=arrayLen;index++));
do
	curl $urls[index] | grep -o 'data\.push([^;]*);' | awk -F'"' '{print "https://textapi.fadada.com/api2/"$2}' > log.txt &&
	cat log.txt | awk -v coms_var="$coms[index]" -F'_' '{print  $0,coms_var,$2}'  | xargs -n 3 sh -c 'wget $1 -O ./$2/$3.jpg' sh			
done





网址：https://admin.yeahgo.com/
账号：yinliqun
密码：ylq12332100


国家企业信用信息公未系统 广西
https://gx.gsxt.gov.cn/corp-query-search-1.html

全国医疗机构查询：
http://zgcx.nhc.gov.cn:9090/unit/index  