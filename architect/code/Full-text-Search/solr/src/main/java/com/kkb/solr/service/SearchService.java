package com.kkb.solr.service;

import com.kkb.solr.po.Result;

public interface SearchService {
	public Result search(String queryString, String catalog_name, String price, String sort, Integer page)
			throws Exception;
}
