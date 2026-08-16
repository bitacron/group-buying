package com.example.groupbuying.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.enums.UserType;
import com.example.groupbuying.model.user.Leader;
import com.example.groupbuying.model.user.User;
import com.example.groupbuying.model.user.UserDelivery;
import com.example.groupbuying.user.mapper.LeaderMapper;
import com.example.groupbuying.user.mapper.UserDeliveryMapper;
import com.example.groupbuying.user.mapper.UserMapper;
import com.example.groupbuying.user.service.UserService;
import com.example.groupbuying.vo.user.LeaderAddressVo;
import com.example.groupbuying.vo.user.UserLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Resource
	private UserMapper userMapper;

	@Resource
	private UserDeliveryMapper userDeliveryMapper;

	@Resource
	private LeaderMapper leaderMapper;

	//@Resource
	//private RegionFeignClient regionFeignClient;

	/**
	 * 获取团长地址信息
	 *
	 * @param userId 用户id
	 * @return 团长地址信息
	 */
	@Override
	public LeaderAddressVo getLeaderAddressVoByUserId(Long userId) {
		// 根据用户userId获取默认团长leaderId
		LambdaQueryWrapper<UserDelivery> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(UserDelivery::getUserId, userId);
		queryWrapper.eq(UserDelivery::getIsDefault, 1);
		UserDelivery userDelivery = userDeliveryMapper.selectOne(queryWrapper);
		if(null == userDelivery) {
			return null;
		}

		// 根据团长leaderId获取团长信息
		Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());

		LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
		BeanUtils.copyProperties(leader, leaderAddressVo);
		leaderAddressVo.setUserId(userId);
		leaderAddressVo.setLeaderId(leader.getId());
		leaderAddressVo.setLeaderName(leader.getName());
		leaderAddressVo.setLeaderPhone(leader.getPhone());
		leaderAddressVo.setWareId(userDelivery.getWareId());
		leaderAddressVo.setStorePath(leader.getStorePath());
		return leaderAddressVo;
	}

	@Override
	public User getByOpenid(String openId) {
		return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
	}

	@Override
	public UserLoginVo getUserLoginVo(Long userId) {
		UserLoginVo userLoginVo = new UserLoginVo();
		User user = this.getById(userId);
		userLoginVo.setNickName(user.getNickName());
		userLoginVo.setUserId(userId);
		userLoginVo.setPhotoUrl(user.getPhotoUrl());
		userLoginVo.setOpenId(user.getOpenId());
		userLoginVo.setIsNew(user.getIsNew());

		// 如果是团长获取当前前团长id与对应的仓库id
		if (user.getUserType() == UserType.LEADER) {
			//LambdaQueryWrapper<Leader> queryWrapper = new LambdaQueryWrapper<>();
			//queryWrapper.eq(Leader::getUserId, userId);
			//queryWrapper.eq(Leader::getCheckStatus, 1);
			//Leader leader = leaderMapper.selectOne(queryWrapper);
			//if(Objects.nonNull(leader)) {
			//	userLoginVo.setLeaderId(leader.getId());
			//	Long wareId = regionFeignClient.getWareId(leader.getRegionId());
			//	userLoginVo.setWareId(wareId);
			//}
		} else {
			//如果是会员获取当前会员对应的仓库id
			LambdaQueryWrapper<UserDelivery> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(UserDelivery::getUserId, userId);
			queryWrapper.eq(UserDelivery::getIsDefault, 1);
			UserDelivery userDelivery = userDeliveryMapper.selectOne(queryWrapper);
			if (Objects.nonNull(userDelivery)) {
				userLoginVo.setLeaderId(userDelivery.getLeaderId());
				userLoginVo.setWareId(userDelivery.getWareId());
			} else {
				userLoginVo.setLeaderId(1L);
				userLoginVo.setWareId(1L);
			}
		}
		return userLoginVo;
	}
}