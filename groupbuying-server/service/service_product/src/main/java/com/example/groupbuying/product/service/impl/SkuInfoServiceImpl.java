package com.example.groupbuying.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.common.exception.CustomException;
import com.example.groupbuying.common.result.ResultCodeEnum;
import com.example.groupbuying.common.constant.RedisConst;
import com.example.groupbuying.mq.constant.MqConst;
import com.example.groupbuying.mq.service.RabbitService;
import com.example.groupbuying.model.product.SkuAttrValue;
import com.example.groupbuying.model.product.SkuImage;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.model.product.SkuPoster;
import com.example.groupbuying.product.mapper.SkuInfoMapper;
import com.example.groupbuying.product.service.SkuAttrValueService;
import com.example.groupbuying.product.service.SkuImageService;
import com.example.groupbuying.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.product.service.SkuPosterService;
import com.example.groupbuying.vo.product.SkuInfoQueryVo;
import com.example.groupbuying.vo.product.SkuInfoVo;
import com.example.groupbuying.vo.product.SkuStockLockVo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Resource
    private SkuPosterService skuPosterService;

    @Resource
    private SkuImageService skuImagesService;

    @Resource
    private SkuAttrValueService skuAttrValueService;

    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;


    // 获取sku分页列表
    @Override
    public IPage<SkuInfo> selectPageSkuInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo) {
        // 获取条件值
        String keyword = skuInfoQueryVo.getKeyword();
        String skuType = skuInfoQueryVo.getSkuType();
        Long categoryId = skuInfoQueryVo.getCategoryId();
        // 封装条件
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like(SkuInfo::getSkuName, keyword);
        }
        if (!StringUtils.isEmpty(skuType)) {
            queryWrapper.like(SkuInfo::getSkuType, skuType);
        }
        if (!StringUtils.isEmpty(categoryId)) {
            queryWrapper.like(SkuInfo::getCategoryId, categoryId);
        }
        //调用方法查询
        IPage<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, queryWrapper);
        return skuInfoPage;
    }
    //添加商品
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        // 1.保存sku信息
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        baseMapper.insert(skuInfo);
        //this.save(skuInfo);

        // 2.保存sku海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(! CollectionUtils.isEmpty(skuPosterList)) {
            for(SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuInfo.getId());
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        // 3.保存sku图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImagesList)) {
            int sort = 1;
            for(SkuImage skuImages : skuImagesList) {
                skuImages.setSkuId(skuInfo.getId());
                skuImages.setSort(sort);
                sort++;
            }
            skuImagesService.saveBatch(skuImagesList);
        }

        // 4.保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)) {
            int sort = 1;
            for(SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValue.setSort(sort);
                sort++;
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }


    }

    //根据sku的id获取商品sku信息
    @Override
    public SkuInfoVo getSkuInfoVo(Long id) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        // 根据id产讯sku基本信息
        SkuInfo skuInfo = baseMapper.selectById(id);
        // 根据id查询商品图片列表
        List<SkuImage> skuImageList = skuImagesService.getImageListBySkuId(id);
        // 根据id查询商品海报列表
        List<SkuPoster> skuPosterList = skuPosterService.getPosterListBySkuId(id);
        // 根据id查询商品属性信息
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.getAttrValueListBySkuId(id);
        // 封装数据到SkuInfoVo对象中
        BeanUtils.copyProperties(skuInfo, skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    //修改商品sku信息
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {
        //修改sku基本信息
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        baseMapper.updateById(skuInfo);
        //this.updateById(skuInfoVo);

        Long skuId = skuInfoVo.getId();
        // 删除旧的海报信息
        skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId, skuId));
        //保存sku海报信息
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(!CollectionUtils.isEmpty(skuPosterList)) {
            for(SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuId);
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        //删除旧的sku商品图片
        skuImagesService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, skuId));
        //保存sku图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImagesList)) {
            int sort = 1;
            for(SkuImage skuImages : skuImagesList) {
                skuImages.setSkuId(skuId);
                skuImages.setSort(sort);
                sort++;
            }
            skuImagesService.saveBatch(skuImagesList);
        }

        //删除旧的sku平台属性
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, skuId));
        //保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)) {
            int sort = 1;
            for(SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuId);
                skuAttrValue.setSort(sort);
                sort++;
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    //商品审核
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void check(Long skuId, Integer status) {
        // 更改发布状态
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setCheckStatus(status);
        baseMapper.updateById(skuInfoUp);
    }

    @Autowired
    private RabbitService rabbitService;

    // 商品上架
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void publish(Long skuId, Integer status) {
        // 更改发布状态
        if(status == 1) { // 传入status状态值为1，说明本次执行的是要上架商品
            SkuInfo skuInfoUp = new SkuInfo();
            skuInfoUp.setId(skuId);
            skuInfoUp.setPublishStatus(status);
            baseMapper.updateById(skuInfoUp);
            // 商品上架 发送mq消息更新es数据
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_UPPER, skuId);

        } else { // 传入status状态值为0，说明本次执行的是要下架商品
            SkuInfo skuInfoDown = new SkuInfo();
            skuInfoDown.setId(skuId);
            skuInfoDown.setPublishStatus(status);
            baseMapper.updateById(skuInfoDown);
            // 商品下架 发送mq消息更新es数据
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_LOWER, skuId);
        }
    }

    //新人专享
    @Override
    public void isNewPerson(Long skuId, Integer status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsNewPerson(status);
        baseMapper.updateById(skuInfo);
    }

    //批量获取sku信息
    @Override
    public List<SkuInfo> findSkuInfoList(List<Long> skuIdList) {
        List<SkuInfo> skuInfoList = baseMapper.selectBatchIds(skuIdList);
        return skuInfoList;
    }

    /**
     * 根据关键字查询sku列表
     * @param keyword 关键字
     * @return sku列表
     */
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SkuInfo::getSkuName, keyword);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取新人专享商品列表
     * @return 新人专享商品列表
     */
    @Override
    public List<SkuInfo> findNewPersonSkuInfoList() {
        // 条件1：isNewPerson = 1 （新人专享商品）
        // 条件2：publishStatus = 1 （已发布）
        // 条件3：显示其中三个商品
        Page<SkuInfo> page = new Page<>(1, 3); // 分页参数：获取第1页，每页3条记录
        QueryWrapper<SkuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SkuInfo::getIsNewPerson, 1);
        queryWrapper.lambda().eq(SkuInfo::getPublishStatus, 1);
        queryWrapper.lambda().orderByDesc(SkuInfo::getStock); // 按照库存降序排序
        IPage<SkuInfo> skuInfoPage = baseMapper.selectPage(page, queryWrapper);
        return skuInfoPage.getRecords();
    }

    /**
     * 验证锁定库存
     *
     * @param skuStockLockVoList 要验证锁定的商品集合
     * @param orderNo            订单唯一标识
     * @return 是否成功
     */
    @Override
    public Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo) {
        // 1.判断skuStockLockVoList是否为空
        if (CollectionUtils.isEmpty(skuStockLockVoList)){
            throw new CustomException(ResultCodeEnum.DATA_ERROR);
        }
        // 2.遍历skuStockLockVoList，得到每一个商品，验证库存并锁定库存，具备原子性。
        skuStockLockVoList.forEach(skuStockLockVo -> {
            checkLock(skuStockLockVo);
        });
        // 3.只要有一个商品锁定失败，所有锁定成功的商品都解锁
        boolean flag = skuStockLockVoList.stream().anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock());
        if (flag) {
            // 获取所有锁定成功的商品，遍历解锁库存。所有锁定成功的商品都解锁
            skuStockLockVoList.stream().filter(SkuStockLockVo::getIsLock).forEach(skuStockLockVo -> {
                skuInfoMapper.unlockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            });
            // 返回失败的状态
            return false;
        }
        // 4.如果所有商品都锁定成功了，Redis缓存相关数据，为了方便后面解锁或减少库存

        // 以orderNo作为key，以lockVos锁定信息作为value
        this.redisTemplate.opsForValue().set(RedisConst.STOCK_INFO + orderNo, skuStockLockVoList);
//        this.rabbitTemplate.convertAndSend("ORDER_EXCHANGE", "stock.ttl", orderNo);
        return true;
    }

    private void checkLock(SkuStockLockVo skuStockLockVo){
        // 公平锁（FairLock）：就是保证客户端获取锁的顺序，跟他们请求获取锁的顺序，是一样的。
        // 公平锁需要排队，谁先申请获取这把锁，谁就可以先获取到这把锁，是按照请求的先后顺序来的。
        RLock rLock = this.redissonClient
                .getFairLock(RedisConst.SKU_KEY_PREFIX + skuStockLockVo.getSkuId());
        // 加锁
        rLock.lock();

        try {
            // 验库存：查询，返回的是满足要求的库存列表
            SkuInfo skuInfo = skuInfoMapper.checkStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            // 判断没有满足条件的商品，设置isLock值false，返回。
            if (null == skuInfo) {
                skuStockLockVo.setIsLock(false);
                return;
            }

            // 判断有满足条件的商品，锁定库存，更新
            Integer row = skuInfoMapper.lockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            if (row == 1) {
                skuStockLockVo.setIsLock(true);
            }
        } finally {
            // 解锁
            rLock.unlock();
        }
    }

    /**
     * 扣减库存
     *
     * @param orderNo 订单号
     */
    @Override
    public void minusStock(String orderNo) {
        // 从redis获取锁定库存的缓存信息
        List<SkuStockLockVo> skuStockLockVoList = (List<SkuStockLockVo>) this.redisTemplate.opsForValue().get(RedisConst.STOCK_INFO + orderNo);
        if (CollectionUtils.isEmpty(skuStockLockVoList)){
            return ;
        }

        // 遍历集合，得到每个对象减库存
        skuStockLockVoList.forEach(skuStockLockVo -> {
            skuInfoMapper.minusStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
        });

        // 解锁库存之后，删除锁定库存的缓存。以防止重复解锁库存
        this.redisTemplate.delete(RedisConst.STOCK_INFO + orderNo);
    }
}
