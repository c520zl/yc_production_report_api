package com.yc.productionreport.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.productionreport.entity.Admin;
import com.yc.productionreport.mapper.AdminMapper;
import com.yc.productionreport.service.AdminService;
import com.yc.productionreport.util.JwtUtils;
import java.util.Date;
import com.yc.productionreport.util.PasswordUtils;
import com.yc.productionreport.vo.ResultVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 管理员服务实现类
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    private static final long REDIS_EXPIRE_TIME = 86400L; // Redis缓存过期时间(秒)

    @Override
    @Transactional
    public ResultVO register(Admin admin) {
        // 1. 检查用户名是否已存在
        Admin existingAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (existingAdmin != null) {
            return ResultVO.error("管理员用户名已存在");
        }

        // 2. 生成盐值并加密密码
        String salt = PasswordUtils.generateSalt();
        String encryptedPassword = PasswordUtils.hashPassword(admin.getPassword(), salt);

        // 3. 设置管理员信息
        admin.setPassword(encryptedPassword);
        admin.setSalt(salt);
        admin.setCreateTime(new Date());
        admin.setUpdateTime(new Date());
        admin.setEnabled(true);

        // 4. 保存管理员
        int rows = adminMapper.insert(admin);
        if (rows > 0) {
            return ResultVO.success("管理员注册成功");
        } else {
            return ResultVO.error("管理员注册失败");
        }
    }

    @Override
    public ResultVO login(String username, String password) {
        // 1. 查询管理员
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null || !admin.getEnabled()) {
            return ResultVO.error("管理员用户名不存在或已禁用");
        }

        // 2. 验证密码
        boolean passwordValid = PasswordUtils.verifyPassword(password, admin.getPassword(), admin.getSalt());
        if (!passwordValid) {
            return ResultVO.error("密码错误");
        }

        // 3. 生成JWT token
        String token = jwtUtils.generateToken(username, "ADMIN");

        // 4. 缓存管理员信息和token到Redis
        redisTemplate.opsForValue().set("admin:" + username, admin, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("token:" + username, token, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);

        // 5. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", admin);

        return ResultVO.success("管理员登录成功", result);
    }

    @Override
    public Admin findByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public ResultVO changePassword(String username, String newPassword) {
        // 1. 查询管理员
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            return ResultVO.error("管理员不存在");
        }

        // 2. 生成新盐和加密新密码
        String newSalt = PasswordUtils.generateSalt();
        String encryptedNewPassword = PasswordUtils.hashPassword(newPassword, newSalt);

        // 3. 更新密码和盐
        admin.setPassword(encryptedNewPassword);
        admin.setSalt(newSalt);
        admin.setUpdateTime(new Date());
        adminMapper.updateById(admin);

        return ResultVO.success("密码修改成功");
    }
}