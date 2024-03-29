package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhaoq0103
 * @Description:
 * @Date:Create：in 2020/1/10 4:16 PM
 * @Modified By：zhaoq0103
 */


@Controller
//@RequestMapping("content")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;



    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> queryContentCategoryByParentId(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        // 调用服务查询
        List<TbContentCategory> list = this.contentCategoryService.queryContentCategoryByParentId(parentId);

        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory cat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(cat.getId());
            node.setText(cat.getName());
            node.setState(cat.getIsParent()?"closed":"open");//"open",closed
            nodes.add(node);
        }

        return nodes;
    }

    //add

    @RequestMapping(value="/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createContentCategory(long parentId, String name) {
        return this.contentCategoryService.createContentCategory(parentId,name);
    }


    //update
    @RequestMapping(value="/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategory(long parentId, String name) {

        return TaotaoResult.ok();
    }

    //delete
    @RequestMapping(value="/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(long parentId) {

        return TaotaoResult.ok();
    }

}

