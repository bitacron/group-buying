package com.example.groupbuying.home.controller;

import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.home.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "商品详情")
@RestController
@RequestMapping("api/home")
public class ItemApiController {

   @Resource
   private ItemService itemService;

   /**
    * 获取sku详细信息
    *
    * @param request 请求参数
    * @return 商品详情
    */
   @ApiOperation(value = "获取sku详细信息")
   @GetMapping("item/{id}")
   public Result<Map<String, Object>> index(@PathVariable Long id, HttpServletRequest request) {
      Long userId = AuthContextHolder.getUserId();
      Map<String, Object> item = itemService.item(id, userId);
      return Result.ok(item);
   }

}