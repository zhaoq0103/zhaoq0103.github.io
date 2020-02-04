package com.kkb.solr.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kkb.solr.po.Result;
import com.kkb.solr.service.SearchService;

@Controller
public class SearchController {

	@Resource
	private SearchService service;

	@RequestMapping("search")
	public String search(Model model, String queryString, String catalog_name, String price, String sort,
			Integer page) throws Exception{

		// 根据条件搜索
		Result result = this.service.search(queryString, catalog_name, price, sort, page);

		// 把结果集放到模型中
		model.addAttribute("result", result);

		// 搜索条件数据回显
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		model.addAttribute("page", page);

		return "search";
	}
}
