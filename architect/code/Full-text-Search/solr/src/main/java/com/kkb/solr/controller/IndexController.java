package com.kkb.solr.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkb.solr.service.IndexService;

import javax.annotation.Resource;

@Controller
public class IndexController {

	@Resource
	private IndexService service;

	@RequestMapping(value="indexImport",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String indexImport() throws Exception {
		return service.importIndex();
	}
}
