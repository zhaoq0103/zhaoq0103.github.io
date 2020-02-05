package com.taotao.common.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class APIService {
	//1.不带参数的get请求的方法
	/**
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public HttpResult doGet(String url) throws Exception{
		return doGet(url,null);
	}
	
	
	
	//2.带参数的get请求的方法
	/**
	 * 
	 * @param url  请求的URL
	 * @param map  请求传递的参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doGet(String url,Map<String, Object> map) throws Exception{
		
		//1.创建httpclient的对象  
		CloseableHttpClient client = HttpClients.createDefault();
		//2.创建httpget get请求对象
		URIBuilder builder =  new URIBuilder(url);//设置URL
		//遍历参数，设置参数的值
		if(map!=null){
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				
				builder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		URI uri = builder.build();
		HttpGet httpGet = new HttpGet(uri);
		//3.执行请求
		CloseableHttpResponse response = client.execute(httpGet);
		//4.获取响应的结果 封装到httpresult中
		Integer code = response.getStatusLine().getStatusCode();//状态码
		String body =null;
		if(response.getEntity()!=null){
			body = EntityUtils.toString(response.getEntity(), "utf-8");
		}
		HttpResult result = new HttpResult(code, body);
		return result;
	}
	
	
	//3.不带参数的post请求的方法
	public HttpResult doPost(String url) throws Exception{
		return doPost(url,null);
	}
	//4.带参数的post请的方法
	public HttpResult doPost(String url,Map<String, Object> map) throws Exception{
		//1. 创建httpclient 对象
		CloseableHttpClient client = HttpClients.createDefault();
		
		//2.创建httppost 请求对象
		
		HttpPost httpPost = new HttpPost(url);
		
		if(map!=null){
			//遍历参数的map集合  设置参数列表
			List<NameValuePair> parameters = new ArrayList<>();
			
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			//创建表单实体对象，将参数设置到表单实体中
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(parameters);
			
			//设置表单实体到httpost请求对象中
			httpPost.setEntity(encodedFormEntity);
		}
		
		//3.执行请求
		CloseableHttpResponse response = client.execute(httpPost);
		//4.获取响应结果，封装到httpresult中
		Integer code = response.getStatusLine().getStatusCode();//状态码
		String body = null;
		if(response.getEntity()!=null){
			body =  EntityUtils.toString(response.getEntity(), "utf-8");
		}
		//返回httpresult
		HttpResult result = new HttpResult(code, body);
		return result;
	}
	
	
	//put delete
}
