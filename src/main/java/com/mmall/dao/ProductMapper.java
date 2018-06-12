package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    List<Product> selectList();
    List<Product> selectProductByNameAndID(@Param("productName") String productName, @Param("productID") Integer productID);
    List<Product> selectProductByKeywordCategory(@Param("keyword") String keyword, @Param("categoryIdList") List<Integer> categoryIdList);
}