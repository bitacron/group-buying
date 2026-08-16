package com.example.groupbuying.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.groupbuying.client.activity.ActivityFeignClient;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.enums.SkuType;
import com.example.groupbuying.model.product.Category;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.model.search.SkuEs;
import com.example.groupbuying.search.repository.SkuRepository;
import com.example.groupbuying.search.service.SkuService;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.vo.search.SkuEsQueryVo;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private SkuRepository skuEsRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 上架商品列表
     * @param skuId
     */
    @Override
    public void upperSku(Long skuId) {
        log.info("upperSku：{}", skuId);
        //1通过远程调用，根据shuId查询sku信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(null == skuInfo) return;
        // 查询分类
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());

        // 2.获取数据封装ShuEs对象
        SkuEs skuEs = new SkuEs();
        if (category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品

        }
        // 3调用方法添加ElasticSearch
        SkuEs save = skuEsRepository.save(skuEs);
        log.info("upperSku："+ JSON.toJSONString(save));
    }

    /**
     * 下架商品列表
     * @param skuId
     */
    @Override
    public void lowerSku(Long skuId) {
        this.skuEsRepository.deleteById(skuId);
    }

    /**
     * 获取爆品商品
     *
     * @return 爆品商品列表
     */
    @Override
    public List<SkuEs> findHotSkuList() {
        // find read get 开头
        // 关联条件关键字
        Pageable pageable = PageRequest.of(0, 10); // 0 代表第一页
        Page<SkuEs> pageModel = skuEsRepository.findByOrderByHotScoreDesc(pageable);
        return pageModel.getContent();
    }

    /**
     * 查询商品分类
     *
     * @param pageable 分页
     * @param skuEsQueryVo 查询对象
     * @return 分类商品信息
     */
    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        Page<SkuEs> page;
        if(StringUtils.isEmpty(skuEsQueryVo.getKeyword())) {
            // 如果keyWord为空则根据仓库id+分类id进行查询
            page = skuEsRepository.findByCategoryIdAndWareId(skuEsQueryVo.getCategoryId(), skuEsQueryVo.getWareId(), pageable);
        } else {
            // 如果keyWord不为空则根据仓库id+分类id+keyword进行查询
            page = skuEsRepository.findByKeywordAndWareId(skuEsQueryVo.getKeyword(), skuEsQueryVo.getWareId(), pageable);
        }

        List<SkuEs> skuEsList = page.getContent();
        // 获取sku对应的促销活动标签
        if(!CollectionUtils.isEmpty(skuEsList)) {
            List<Long> skuIdList = skuEsList.stream().map(SkuEs::getId).collect(Collectors.toList());
            // Long, List<String> key:skuId值，Long类型  value:sku参加活动里面多个规则列表，List集合
            // 一个商品参加一个活动，一个活动可以有多个规则。
            // 比如有活动：中秋节满减活动。可以有多个规则：满20元减1元，满59元减5元
            Map<Long, List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList);
            if(null != skuIdToRuleListMap) {
                skuEsList.forEach(skuEs -> {
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }
        return page;
    }

    /**
     * 更新商品热度incrHotScore
     *
     * @param skuId 商品skuId
     * @return 是否更新成功
     */
    @Override
    public void incrHotScore(Long skuId) {
        // 定义key
        String hotKey = "hotScore";
        // 保存数据：key为hotScore，value为skuId:{skuId}的值，每次score加一。返回加1后的score。
        Double hotScore = redisTemplate.opsForZSet().incrementScore(hotKey, "skuId:" + skuId, 1);
        if (hotScore % 10 == 0){
            // 更新ElasticSearch
            Optional<SkuEs> optional = skuEsRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            skuEsRepository.save(skuEs);
        }
    }
}