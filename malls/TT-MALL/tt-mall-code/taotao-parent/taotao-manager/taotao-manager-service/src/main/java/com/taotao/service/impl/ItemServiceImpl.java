package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.print.attribute.standard.Destination;

@Service
public class ItemServiceImpl implements ItemService {
@Autowired
private TbItemMapper mapper;
@Autowired
private TbItemDescMapper descmapper;

@Autowired
private JmsTemplate jmsTemplate;

//@Autowired
@Resource
private javax.jms.Destination topicDestination;

	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//1.设置分页的信息 使用pagehelper
		if(page==null)page=1;
		if(rows==null)rows=30;
		PageHelper.startPage(page, rows);
		//2.注入mapper
		//3.创建example 对象 不需要设置查询条件
		TbItemExample example = new TbItemExample();
		//4.根据mapper调用查询所有数据的方法
		List<TbItem> list = mapper.selectByExample(example);
		//5.获取分页的信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		//6.封装到EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int)info.getTotal());
		result.setRows(info.getList());
		//7.返回
		return result;
	}
	@Override
	public TaotaoResult saveItem(TbItem item, String desc) {
		//生成商品的id
		final long itemId = IDUtils.genItemId();
		//1.补全item 的其他属性
		item.setId(itemId);
		item.setCreated(new Date());
		//1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);
		item.setUpdated(item.getCreated());
		//2.插入到item表 商品的基本信息表
		mapper.insertSelective(item);
		//3.补全商品描述中的属性
		TbItemDesc desc2 = new TbItemDesc();
		desc2.setItemDesc(desc);
		desc2.setItemId(itemId);
		desc2.setCreated(item.getCreated());
		desc2.setUpdated(item.getCreated());
		//4.插入商品描述数据
			//注入tbitemdesc的mapper
		descmapper.insertSelective(desc2);


		//MQ发送toptic消息,更新solr索引库
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage tmsg = session.createTextMessage("" + itemId);
				return tmsg;
			}
		});



		//5.返回taotaoresult
		return TaotaoResult.ok();
	}

	@Override
	public TbItem getItemById(Long itemId) {
		return mapper.selectByPrimaryKey(itemId);
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		return descmapper.selectByPrimaryKey(itemId);
	}


}
