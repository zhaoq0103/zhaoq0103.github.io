package com.kkb.solr.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.kkb.solr.mapper.ProductMapper;
import com.kkb.solr.po.Product;

@Service
public class IndexServiceImpl implements IndexService {

	@Resource
	private HttpSolrServer solrServer;

	@Resource
	private ProductMapper mapper;

	@Override
	public String importIndex() {

		try {
			// 采集数据
			List<Product> productList = mapper.queryProductList();

			// 创建文档
			List<SolrInputDocument> docs = new ArrayList<>();
			SolrInputDocument doc;
			for (Product product : productList) {
				doc = new SolrInputDocument();
				doc.addField("id", product.getPid());
				doc.addField("product_name", product.getName());
				doc.addField("product_catalog_name", product.getCatalog_name());
				doc.addField("product_price", product.getPrice());
				doc.addField("product_description", product.getDescription());
				doc.addField("product_picture", product.getPicture());
				docs.add(doc);
			}
			// 先清空索引库
			solrServer.deleteByQuery("*:*");
			// 再索引文档
			solrServer.add(docs);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return "导入失败";
		}

		return "导入成功";
	}

}
