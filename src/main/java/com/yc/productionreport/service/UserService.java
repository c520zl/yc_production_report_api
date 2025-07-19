package com.yc.productionreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.productionreport.entity.User;
import com.yc.productionreport.vo.LoginVO;
import com.yc.productionreport.vo.ResultVO;

/**
 * 普通用户服务接口
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    ResultVO register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含用户信息和token
     */
    ResultVO login(String username, String password);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);
}