package com.yc.productionreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.productionreport.entity.Admin;
import com.yc.productionreport.vo.ResultVO;

/**
 * 管理员服务接口
 */
public interface AdminService extends IService<Admin> {
    /**
     * 管理员注册
     * @param admin 管理员信息
     * @return 注册结果
     */
    ResultVO register(Admin admin);

    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含管理员信息和token
     */
    ResultVO login(String username, String password);

    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员信息
     */
    Admin findByUsername(String username);

    /**
     * 修改管理员密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    ResultVO changePassword(String username, String newPassword);
}