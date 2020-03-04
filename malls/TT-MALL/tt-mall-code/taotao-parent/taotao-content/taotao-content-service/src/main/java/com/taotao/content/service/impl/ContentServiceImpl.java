package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.jedis.JedisClient;
import com.taotao.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper mapper;

	@Autowired
//	private Jedis jedis;
    private JedisClient jedis;

	@Value("${content_category_id_key}")
	private String content_category_id_key;
	
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//1.注入mapper
		
		//2.补全其他的属性
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		//3.插入内容表中
		mapper.insertSelective(content);

		try{
			jedis.hdel(content_category_id_key, content.getCategoryId()+"");
		}catch (Exception e){

		}
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentListByCatID(Long catID) {

		try{
			String con = jedis.hget(content_category_id_key, catID+"");
			if(con != null){
				System.out.println("get from redis");
				return JsonUtils.jsonToList(con, TbContent.class);
			}
		}catch (Exception e){

		}

		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(catID);

		List<TbContent> list = mapper.selectByExample(example);

		System.out.println("cache to redis");
		jedis.hset(content_category_id_key, catID+"", JsonUtils.objectToJson(list));

		return list;
	}

}
