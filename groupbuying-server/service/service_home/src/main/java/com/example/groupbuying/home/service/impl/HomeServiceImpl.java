package com.example.groupbuying.home.service.impl;

import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.client.search.SkuFeignClient;
import com.example.groupbuying.client.user.UserFeignClient;
import com.example.groupbuying.home.service.HomeService;
import com.example.groupbuying.model.product.Category;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.model.search.SkuEs;
import com.example.groupbuying.vo.user.LeaderAddressVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class HomeServiceImpl implements HomeService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private SkuFeignClient searchFeignClient;

    //@Resource
    //private ActivityFeignClient activityFeignClient;

    @Override
    public Map<String, Object> home(Long userId) {
        Map<String, Object> result = new HashMap<>();

        // 根据userId获取当前登录用户提货点地址信息
        // 远程调用service-user模块的接口
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
        result.put("leaderAddressVo", leaderAddressVo);

        // 获取分类信息
        // 远程调用service-product模块的接口
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList", categoryList);

        // 获取新人专享商品
        // 远程调用service-product模块的接口
        List<SkuInfo> newPersonSkuInfoList =  productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList", newPersonSkuInfoList);

        //TODO 获取用户首页秒杀数据

        // 获取爆品商品
        // 远程调用service-search模块的接口
        List<SkuEs> hotSkuList = searchFeignClient.findHotSkuList();
        //获取sku对应的促销活动标签
        if(!CollectionUtils.isEmpty(hotSkuList)) {
            //List<Long> skuIdList = hotSkuList.stream().map(SkuEs::getId).collect(Collectors.toList());
            //Map<Long, List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList);
            //if(null != skuIdToRuleListMap) {
            //    hotSkuList.forEach(skuEs -> {
            //        skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
            //    });
            //}
        }
        result.put("hotSkuList", hotSkuList);
        return result;
    }
}