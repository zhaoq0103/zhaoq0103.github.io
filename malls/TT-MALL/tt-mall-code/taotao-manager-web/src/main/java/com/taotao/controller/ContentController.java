package com.taotao.controller;

import com.taotao.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
