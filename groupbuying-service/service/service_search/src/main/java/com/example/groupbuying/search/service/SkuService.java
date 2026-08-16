package com.example.groupbuying.search.service;

import com.example.groupbuying.model.search.SkuEs;
import com.example.groupbuying.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface SkuService {

    /**
     * 上架商品列表
     * @param skuId 商品id
     */
    void upperSku(Long skuId);

    /**
     * 下架商品列表
     * @param skuId 商品id
     */
    void lowerSku(Long skuId);
    /**
     * 获取爆品商品
     * @return 爆品商品列表
     */
    List<SkuEs> findHotSkuList();

    /**
     * 查询商品分类
     *
     * @param pageable 分页
     * @param skuEsQueryVo 查询对象
     * @return 分类商品信息
     */
    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo);

    /**
     * 更新商品热度incrHotScore
     *
     * @param skuId 商品skuId
     */
    void incrHotScore(Long skuId);
}