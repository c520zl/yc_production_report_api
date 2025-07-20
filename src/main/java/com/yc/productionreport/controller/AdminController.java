package com.yc.productionreport.controller;

import com.yc.productionreport.entity.Admin;
import com.yc.productionreport.service.AdminService;
import com.yc.productionreport.vo.ResultVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import com.yc.productionreport.util.JwtUtils;
import java.util.Map;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 管理员注册
     */
    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody Admin admin) {
        return adminService.register(admin);
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        String captcha = loginData.get("captcha");
        
        // 验证验证码
        HttpSession session = request.getSession();
        String storedCaptcha = (String) session.getAttribute("captchaCode");
        
        if (captcha == null || storedCaptcha == null || !captcha.equalsIgnoreCase(storedCaptcha)) {
            return ResultVO.error("验证码错误或已过期");
        }
        
        // 验证成功后移除验证码
        session.removeAttribute("captchaCode");
        
        return adminService.login(username, password);
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public ResultVO changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResultVO.error("无效的令牌");
        }
        token = token.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);
        String newPassword = params.get("newPassword");
        return adminService.changePassword(username, newPassword);
    }
}