package com.kkb.lucene.test;

import java.io.File;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class SearchDemo {

	public static void main(String[] args) throws Exception {
		// 指定索引库路径
		String indexPath = "/Users/zhaoq0103/lucene-cangku";
		// 指定索引库对象
		Directory dir = FSDirectory.open(Paths.get(indexPath));

		// 索引读对象
		IndexReader reader = DirectoryReader.open(dir);
		// 索引搜索器
		IndexSearcher searcher = new IndexSearcher(reader);

		 Analyzer analyzer = new StandardAnalyzer();
//		Analyzer analyzer = new IKAnalyzer();
		// 通过QueryParser解析查询语法，获取Query对象
		QueryParser parser = new QueryParser("name", analyzer);
		// 参数是查询语法
		Query query = parser.parse("name:搜罗");
		TopDocs topDocs = searcher.search(query, 100);

		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		for (ScoreDoc scoreDoc : scoreDocs) {
			Document document = searcher.doc(scoreDoc.doc);
			System.out.println("name : " + document.get("name"));
		}

		reader.close();
	}

}
