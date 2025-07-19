package com.yc.productionreport.security;

import com.yc.productionreport.entity.Admin;
import com.yc.productionreport.entity.User;
import com.yc.productionreport.service.AdminService;
import com.yc.productionreport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 自定义用户详情服务，用于加载用户认证信息
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 先尝试从管理员表查询
        Admin admin = adminService.findByUsername(username);
        if (admin != null) {
            return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // 再从用户表查询
        User user = userService.findByUsername(username);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        // 用户名不存在
        throw new UsernameNotFoundException("用户名或密码不正确");
    }
}