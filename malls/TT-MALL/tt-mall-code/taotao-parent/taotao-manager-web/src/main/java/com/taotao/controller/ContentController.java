package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhaoq0103
 * @Description:
 * @Date:Create：in 2020/1/10 4:16 PM
 * @Modified By：zhaoq0103
 */


@Controller
@RequestMapping("content")
public class ContentController {
    @Autowired
    private ContentService contentService;


    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveContent(TbContent content){
        return this.contentService.saveContent(content);
    }
}
