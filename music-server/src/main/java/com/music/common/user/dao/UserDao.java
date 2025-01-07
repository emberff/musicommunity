package com.music.common.user.dao;

import com.music.common.user.domain.entity.User;
import com.music.common.user.mapper.UserMapper;
import com.music.common.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-01-07
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> implements IUserService {

}
