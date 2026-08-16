package com.example.groupbuying.home.controller;

import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.product.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@Api(tags = "商品分类")
@RestController
@RequestMapping("api/home")
public class CategoryApiController {

	@Resource
	private ProductFeignClient productFeignClient;

	@ApiOperation(value = "获取分类信息")
	@GetMapping("category")
	public Result<List<Category>> index() {
		List<Category> categoryList = productFeignClient.findAllCategoryList();
		return Result.ok(categoryList);
	}
}