package com.example.groupbuying.home.controller;

import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.home.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "首页接口")
@RestController
@RequestMapping("api/home")
public class HomeApiController {

	@Resource
	private HomeService homeService;

	@ApiOperation(value = "获取首页数据")
	@GetMapping("index")
	public Result<Map<String, Object>> index(HttpServletRequest request) {
		// 获取用户Id
		Long userId = AuthContextHolder.getUserId();
		Map<String, Object> homeData = homeService.home(userId);
		return Result.ok(homeData);
	}
}