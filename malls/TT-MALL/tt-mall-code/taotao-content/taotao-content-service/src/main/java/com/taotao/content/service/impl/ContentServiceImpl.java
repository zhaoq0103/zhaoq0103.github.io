package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import com.taotao.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper mapper;
	
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//1.注入mapper
		
		//2.补全其他的属性
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		//3.插入内容表中
		mapper.insertSelective(content);
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentListByCatID(Long catID) {
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(catID);

		List<TbContent> list = mapper.selectByExample(example);
		return list;
	}

}
