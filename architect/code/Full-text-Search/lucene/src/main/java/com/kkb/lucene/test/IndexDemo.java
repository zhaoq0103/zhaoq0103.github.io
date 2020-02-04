package com.kkb.lucene.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.kkb.lucene.dao.ItemDao;
import com.kkb.lucene.dao.ItemDaoImpl;
import com.kkb.lucene.po.Item;

public class IndexDemo {

	public static void main(String[] args) throws Exception {

		// 1. 数据采集
		ItemDao itemDao = new ItemDaoImpl();
		List<Item> itemList = itemDao.queryItemList();

		// 2. 创建Document文档对象
		List<Document> documents = new ArrayList<>();
		for (Item item : itemList) {
			Document document = new Document();

			// Document文档中添加Field域
			// 商品Id
			// Store.YES:表示存储到文档域中
			document.add(new StoredField("id", item.getId()));
			// 商品名称
			document.add(new TextField("name", item.getName(), Store.YES));
			// 商品价格
//			document.add(new DoubleField("price", item.getPrice(), Store.YES));
			document.add(new FloatPoint("price", item.getPrice().floatValue()));
			// 商品图片地址+
			document.add(new StoredField("pic", item.getPic()));
			// 商品描述
			document.add(new TextField("description", item.getDescription(), Store.NO));

			// 把Document放到list中
			documents.add(document);
		}

		// 指定分词器：标准分词器（此处可以改为中文分词器）
		 Analyzer analyzer = new StandardAnalyzer();
//		Analyzer analyzer = new IKAnalyzer();
		// 配置文件
//		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		// 指定索引库路径
		String indexPath = "/Users/zhaoq0103/lucene-cangku";
		// 指定索引库对象
//		Directory dir = FSDirectory.open(new File(indexPath));
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		// 创建索引写对象
		// 3. 分析索引并创建索引
		IndexWriter writer = new IndexWriter(dir, iwc);
		writer.addDocuments(documents);

		// 释放资源
		writer.close();

	}
}
