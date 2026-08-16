package com.example.groupbuying.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.product.service.SkuInfoService;
import com.example.groupbuying.vo.product.SkuInfoQueryVo;
import com.example.groupbuying.vo.product.SkuInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Api(value = "SkuInfo管理", tags = "商品Sku管理")
@RestController
@RequestMapping("/admin/product/skuInfo")
//@CrossOrigin
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    // 获取sku分页列表
    @ApiOperation(value = "获取sku分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "skuInfoQueryVo", value = "查询对象", required = false) SkuInfoQueryVo skuInfoQueryVo) {
        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> pageModel = skuInfoService.selectPageSkuInfo(pageParam, skuInfoQueryVo);
        return Result.ok(pageModel);
    }

    //添加商品sku信息
    @ApiOperation(value = "添加商品sku信息")
    @PostMapping("save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    //根据sku的id获取商品sku信息
    @ApiOperation(value = "根据sku的id获取商品sku信息")
    @GetMapping("get/{id}")
    public Result<SkuInfoVo> get(@PathVariable Long id) {
        SkuInfoVo skuInfoVo = skuInfoService.getSkuInfoVo(id);
        return Result.ok(skuInfoVo);
    }

    //修改商品sku信息
    @ApiOperation(value = "修改商品sku信息")
    @PutMapping("update")
    public Result updateById(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    // 根据sku的id删除商品sku信息
    @ApiOperation(value = "根据id删除商品sku信息")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        skuInfoService.removeById(id);
        return Result.ok(null);
    }

    // 根据id列表批量删除商品sku信息
    @ApiOperation(value = "根据id列表批量删除商品sku信息")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        skuInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    /**
     * 商品审核
     * @param skuId
     * @return
     */
    @GetMapping("check/{skuId}/{status}")
    public Result check(@PathVariable("skuId") Long skuId, @PathVariable("status") Integer status) {
        skuInfoService.check(skuId, status);
        return Result.ok(null);
    }

    /**
     * 商品上架
     * @param skuId
     * @return
     */
    @GetMapping("publish/{skuId}/{status}")
    public Result publish(@PathVariable("skuId") Long skuId, @PathVariable("status") Integer status) {
        skuInfoService.publish(skuId, status);
        return Result.ok(null);
    }

    //新人专享
    @GetMapping("isNewPerson/{skuId}/{status}")
    public Result isNewPerson(@PathVariable("skuId") Long skuId, @PathVariable("status") Integer status) {
        skuInfoService.isNewPerson(skuId, status);
        return Result.ok(null);
    }
}

