package com.yc.productionreport.vo;

import lombok.Data;

/**
 * 登录返回VO
 */
@Data
public class LoginVO {
    private Long id;
    private String username;
    private String fullName;
    private String role; // 用户角色：USER/ADMIN
    private String token; // 登录令牌，预留字段
}