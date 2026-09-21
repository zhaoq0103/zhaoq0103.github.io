

: "
视频编码标准：

容器标准（保存为视频文件的格式，例如 RMVB 和 MP4）：
	AVI、MPEG、RMVB、MP4、MOV、FLV、WebM、WMV、ASF、MKV

ITU的VCEG压缩标准：
	H.261、 H.262、H.263、H.264、H.265

ISO的MPEG压缩标准：
	MPEG-1、MPEG-2、MPEG-4、MPEG-7

ITU 与 MPEG 联合的标准：
 	H.264/MPEG-4 AVC

"

sudo apt install ffmpeg libavcodec-extra

ffmpeg -i input.mp4 -c:v libxvid -c:a libmp3lame 
// -b:v 2000k -b:a 192k -ar 44100 
output.avi


ffmpeg -i input.mp4 -c:v copy -c:a copy output.avi