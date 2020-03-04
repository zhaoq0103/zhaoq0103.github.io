package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhaoq0103
 * @Description:
 * @Date:Create：in 2020/1/10 4:13 PM
 * @Modified By：zhaoq0103
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper mapper;


    @Override
//    public List<EasyUITreeNode> getContentCategoryList(Long parentId);
    public List<TbContentCategory> queryContentCategoryByParentId(Long parentId) {

//        TbContentCategory contentCategory = new TbContentCategory();
//        contentCategory.setParentId(parentId);

        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        List<TbContentCategory> list = mapper.selectByExample(example);

        return list;
    }


    public TaotaoResult createContentCategory(Long parentId, String name){
        TbContentCategory category = new TbContentCategory();
        category.setParentId(parentId);
        category.setCreated(new Date());
        category.setIsParent(false);
        category.setName(name);
        category.setSortOrder(1);
        category.setUpdated(category.getCreated());

        mapper.insertSelective(category);

        TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
        if(parent.getIsParent() == false){
            parent.setIsParent(true);
            mapper.updateByPrimaryKeySelective(parent);
        }

        return TaotaoResult.ok(category);
    }

    public TaotaoResult deleteContentCategory(long parentId){
        return TaotaoResult.ok();
    }
}
