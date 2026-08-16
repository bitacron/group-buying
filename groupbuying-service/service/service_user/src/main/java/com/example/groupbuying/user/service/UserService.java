package com.example.groupbuying.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.model.user.User;
import com.example.groupbuying.vo.user.LeaderAddressVo;
import com.example.groupbuying.vo.user.UserLoginVo;

public interface UserService extends IService<User> {

    /**
     * 获取团长地址信息
     *
     * @param userId 用户id
     * @return 团长地址信息
     */
    LeaderAddressVo getLeaderAddressVoByUserId(Long userId);

    /**
     * 根据微信openid获取用户信息
     *
     * @param openId 微信openid
     * @return 登录用户信息
     */
    User getByOpenid(String openId);

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户id
     * @return 用户登录信息
     */
    UserLoginVo getUserLoginVo(Long userId);
}