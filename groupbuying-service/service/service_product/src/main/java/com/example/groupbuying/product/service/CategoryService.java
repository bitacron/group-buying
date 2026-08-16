package com.example.groupbuying.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.model.product.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.vo.product.CategoryQueryVo;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
public interface CategoryService extends IService<Category> {

    //获取商品分类分页列表
    IPage<Category> selectPageCategory(Page<Category> pageParam, CategoryQueryVo categoryQueryVo);

    //查询所有商品分类
    List<Category> findAllList();
}
