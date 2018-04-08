package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value="categoryService")
public class CategoryServiceImpl implements ICategoryService{
    @Autowired
    CategoryMapper categoryMapper;
    public ServerResponse addCategory(Integer parentID, String categoryName){
        if(parentID==null||categoryName==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),"参数为空");
        }
        Category category=new Category();
        category.setParentId(parentID);
        category.setName(categoryName);
        category.setStatus(true);
        int count=categoryMapper.insert(category);
        if(count>0){
            return ServerResponse.createBySuccess("添加分类成功");
        }else
            return ServerResponse.createByErrorMessage("添加分类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId==null||categoryName==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),"参数为空");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int count=categoryMapper.updateByPrimaryKeySelective(category);
        if(count>0){
            return ServerResponse.createBySuccess("更新分类成功");
        }else
            return ServerResponse.createByErrorMessage("更新分类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),"参数为空");
        }
        List<Category> categories=categoryMapper.selectByParentID(categoryId);
        if(categories!=null&&categories.size()!=0)
            return ServerResponse.createBySuccess("查找成功",categories);
        else
            return ServerResponse.createByErrorMessage("查找失败");
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),"参数为空");
        }
        List<Category> categories=categoryMapper.selectByParentID(categoryId);
        List<Integer> result=new ArrayList<Integer>();
        result.add(categoryId);
        if(categories!=null) {
            for (Category category : categories) {
                ServerResponse<List<Integer>> child=selectCategoryAndChildrenById(category.getId());
                result.addAll(child.getData());
            }
        }
        return ServerResponse.createBySuccess("查找成功",result);
    }
}
