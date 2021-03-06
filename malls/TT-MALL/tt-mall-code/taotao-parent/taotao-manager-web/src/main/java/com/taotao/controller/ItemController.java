package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.FastDFSClient;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.service.ItemService;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemservice;

	private String TAOTAO_IMAGE_SERVER_URL = "http://OSSServer";
	//url:/item/list
	//method:get
	//参数：page,rows
	//返回值:json
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		//1.引入服务
		//2.注入服务
		//3.调用服务的方法
		return itemservice.getItemList(page, rows);
	}

	/**
	 * @ResponseBody
	 *    默认的content-type:application/json;charset=utf-8  google浏览器是支持
	 * 	//使用火狐浏览器 使用kindeditor的时候不支持 content-type:application/json;charset=utf-8
	 *   //解决：设置content-type:text/plain;charset=utf-8  都支持
	 * @param uploadFile
	 * @return
	 */
	//produces:就可以设置content-type
	@RequestMapping(value="/pics/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	//就需要将字符串转成jONS 格式的字符串就可以了
	public String uploadImage(MultipartFile uploadFile){
		try {
			// 1.获取元文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 2.获取文件的字节数组
			byte[] bytes = uploadFile.getBytes();
			// 3.通过fastdfsclient的方法上传图片（参数要求有 字节数组 和扩展名 不包含"."）
			FastDFSClient client = new FastDFSClient("classpath:fastdfs.conf");
			// 返回值：group1/M00/00/00/wKgZhVk4vDqAaJ9jAA1rIuRd3Es177.jpg
			String string = client.uploadFile(bytes, extName);
			string = string.substring(string.indexOf("/")); //去掉 "group1"
			//拼接成完整的URL
			String path = 	TAOTAO_IMAGE_SERVER_URL+string;
			// 4.成功时，设置map
			Map<String, Object> map = new HashMap<>();
			map.put("error", 0);
			map.put("url", path);
			// 6.返回map
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			// 5.失败时，设置map
			Map<String, Object> map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "上传失败000");
			return JsonUtils.objectToJson(map);
		}
	}

	//添加商品的方法
	//url:：/item/save
	//参数：tbitem ,desc
	//返回值 json
	//method:post

	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveItem(TbItem item,String desc){
//		//1.引入服务
		//2.注入服务
		//3.调用
		return itemservice.saveItem(item, desc);
	}


	@RequestMapping(value="/item/update",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateItem(TbItem item, String  desc){
		return itemservice.saveItem(item, desc);
	}

}
