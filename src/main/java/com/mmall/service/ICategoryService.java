package com.mmall.service;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ICategoryService {
    public ServerResponse addCategory(Integer parentID,String categoryName);
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName);
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
