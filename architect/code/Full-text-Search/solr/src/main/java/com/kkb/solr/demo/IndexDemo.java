package com.kkb.solr.demo;


import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

public class IndexDemo {

	private HttpSolrServer httpSolrServer;

	// 提取HttpSolrServer创建
	@Before
	public void init() {
		// 1. 创建HttpSolrServer对象
		// 设置solr服务接口,浏览器客户端地址http://127.0.0.1:8081/solr/#/
		String baseURL = "http://solr8080:8080/solr";
		this.httpSolrServer = new HttpSolrServer(baseURL);
	}

	@Test
	public void indexTest() throws Exception{
		// 2. 创建SolrInputDocument对象
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "007");
		document.addField("content", "hello world , I'm James Bond");
		// 3. 把SolrInputDocument对象添加到索引库中
		httpSolrServer.add(document);
		// 4. 提交
		httpSolrServer.commit();
	}
}
