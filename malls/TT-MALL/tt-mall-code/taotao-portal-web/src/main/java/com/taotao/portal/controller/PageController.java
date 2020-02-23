package com.taotao.portal.controller;

import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示首页
 * @title PageController.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
@Controller
public class PageController {
	
	@Autowired
	private ContentService service;

	@Value("${ad1_category_id}")
	private Long categoryId;


    @Value("${ad1_height_b}")
    private String ad1_height_b;


    @Value("${ad1_height}")
    private String ad1_height;

    @Value("${ad1_width_b}")
    private String ad1_width_b;

    @Value("${ad1_width}")
    private String ad1_width;

	/**
	 * 展示首页
	 * @return
	 */
	//接收URL的请求http://mytmall.com:8882/index.html
	@RequestMapping("/index")
	public String showIndex(Model model){
		List<TbContent> contentList = service.getContentListByCatID(categoryId);
		//转成AD model
        List<Ad1Node> nodes = new ArrayList<>();

        for (TbContent tc:  contentList ) {
            Ad1Node ad = new Ad1Node();
            ad.setAlt(tc.getSubTitle());
            ad.setHref(tc.getUrl());
            ad.setSrc(tc.getPic());
            ad.setSrcB(tc.getPic2());
            ad.setHeight(ad1_height);
            ad.setHeightB(ad1_height_b);
            ad.setWidth(ad1_width);
            ad.setWidthB(ad1_width_b);

            nodes.add(ad);
        }

        model.addAttribute("ad_lunbo", JsonUtils.objectToJson(nodes));
		return "index";//响应jsp
	}

}
