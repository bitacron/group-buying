package com.example.groupbuying.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.common.constant.RedisConst;
import com.example.groupbuying.common.exception.CustomException;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.common.result.ResultCodeEnum;
import com.example.groupbuying.common.utils.JwtHelper;
import com.example.groupbuying.enums.UserType;
import com.example.groupbuying.model.user.User;
import com.example.groupbuying.user.service.UserService;
import com.example.groupbuying.user.utils.ConstantPropertiesUtil;
import com.example.groupbuying.user.utils.HttpClientUtils;
import com.example.groupbuying.vo.user.LeaderAddressVo;
import com.example.groupbuying.vo.user.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user/weixin")
public class WeiXinApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户微信授权登录
     *
     * @param code 用户code
     * @return 用户信息
     */
    @ApiOperation(value = "微信登录获取openid(小程序)")
    @GetMapping("/wxLogin/{code}")
    public Result<Map<String, Object>> callback(@PathVariable String code) {
        // 1.获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。"+code);
        if (StringUtils.isEmpty(code)) {
            throw new CustomException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        // 2.使用code和app_id以及app_secret换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session" +
                "?appid=%s" +
                "&secret=%s" +
                "&js_code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result;
        // HttpClient发送get请求
        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILED);
        }

        System.out.println("使用code换取的access_token结果 = " + result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if(resultJson.getString("errcode") != null){
            throw new CustomException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILED);
        }

        String accessToken = resultJson.getString("session_key");
        String openId = resultJson.getString("openid");

        // 4. 添加微信用户信息到数据库中
        // 判断是否是第一次微信授权登录，通过openid进行数据库查询
        // 根据access_token获取微信用户的基本信息
        // 先根据openid进行数据库查询
        User user = userService.getByOpenid(openId);
        // 如果没有查到用户信息,那么调用微信个人信息获取的接口
        if(null == user){
            user = new User();
            user.setOpenId(openId);
            user.setNickName(openId);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }
        // 5. 根据userId查询提货点和团长信息
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressVoByUserId(user.getId());
        Map<String, Object> map = new HashMap<>();
        String name = user.getNickName();
        map.put("user", user);
        map.put("leaderAddressVo", leaderAddressVo);
        // 6. 通过JWT工具生成token返回
        String token = JwtHelper.createToken(user.getId(), name);
        map.put("token", token);

        // 7.获取当前登录用户信息，放到redis中并设置有效时间
        UserLoginVo userLoginVo = this.userService.getUserLoginVo(user.getId());
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + user.getId(), userLoginVo, RedisConst.USER_KEY_TIMEOUT, TimeUnit.DAYS);
        // 8. 将数据封装Map返回结果
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result<String> updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok("更新成功");
    }
}