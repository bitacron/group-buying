package com.example.groupbuying.search.controller;

import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.search.SkuEs;
import com.example.groupbuying.search.service.SkuService;
import com.example.groupbuying.vo.search.SkuEsQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品搜索列表接口
 * </p>
 */
@RestController
@RequestMapping("api/search/sku")
public class SkuApiController {

    @Resource
    private SkuService skuService;

    @ApiOperation(value = "上架商品")
    @GetMapping("inner/upperSku/{skuId}")
    public Result<String> upperGoods(@PathVariable("skuId") Long skuId) {
        skuService.upperSku(skuId);
        return Result.ok(null);
    }

    @ApiOperation(value = "下架商品")
    @GetMapping("inner/lowerSku/{skuId}")
    public Result<String> lowerGoods(@PathVariable("skuId") Long skuId) {
        skuService.lowerSku(skuId);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取爆品商品")
    @GetMapping("inner/findHotSkuList")
    public List<SkuEs> findHotSkuList() {
        return skuService.findHotSkuList();
    }

    /**
     * 查询商品分类
     *
     * @param page 页码
     * @param limit 页面数据量
     * @param searchParamVo 查询对象
     * @return 分类商品信息
     */
    @ApiOperation(value = "搜索商品")
    @GetMapping("{page}/{limit}")
    public Result<Page<SkuEs>> list(
            @ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Integer limit,
            @ApiParam(name = "searchParamVo", value = "查询对象", required = false) SkuEsQueryVo searchParamVo) {
        Pageable pageable = PageRequest.of(page-1, limit);
        Page<SkuEs> pageModel = skuService.search(pageable, searchParamVo);
        return Result.ok(pageModel);
    }

    /**
     * 更新商品热度incrHotScore
     *
     * @param skuId 商品skuId
     * @return 是否更新成功
     */
    @ApiOperation(value = "更新商品incrHotScore")
    @GetMapping("inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId") Long skuId) {
        // 调用服务层
        skuService.incrHotScore(skuId);
        return true;
    }
}