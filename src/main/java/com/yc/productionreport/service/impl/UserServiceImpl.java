package com.yc.productionreport.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.productionreport.entity.User;
import com.yc.productionreport.mapper.UserMapper;
import com.yc.productionreport.service.UserService;
import com.yc.productionreport.util.PasswordUtils;
import com.yc.productionreport.vo.ResultVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 普通用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final long REDIS_EXPIRE_TIME = 86400L; // Redis缓存过期时间(秒)

    @Override
    @Transactional
    public ResultVO register(User user) {
        // 1. 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(user.getUsername());
        if (existingUser != null) {
            return ResultVO.error("用户名已存在");
        }

        // 2. 生成盐值并加密密码
        String salt = PasswordUtils.generateSalt();
        String encryptedPassword = PasswordUtils.hashPassword(user.getPassword(), salt);

        // 3. 设置用户信息
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setEnabled(true);

        // 4. 保存用户
        int rows = userMapper.insert(user);
        if (rows > 0) {
            return ResultVO.success("注册成功");
        } else {
            return ResultVO.error("注册失败");
        }
    }

    @Override
    public ResultVO login(String username, String password) {
        // 1. 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null || !user.getEnabled()) {
            return ResultVO.error("用户名不存在或已禁用");
        }

        // 2. 验证密码
        boolean passwordValid = PasswordUtils.verifyPassword(password, user.getPassword(), user.getSalt());
        if (!passwordValid) {
            return ResultVO.error("密码错误");
        }

        // 3. 缓存用户信息到Redis
        redisTemplate.opsForValue().set("user:" + username, user, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);

        // 4. 返回登录结果
        return ResultVO.success("登录成功", user);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}