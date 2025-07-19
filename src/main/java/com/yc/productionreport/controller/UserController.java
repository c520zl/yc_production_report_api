package com.yc.productionreport.controller;

import com.yc.productionreport.entity.User;
import com.yc.productionreport.service.UserService;
import com.yc.productionreport.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;

/**
 * 普通用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
public ResultVO login(@RequestBody User user, HttpServletRequest request) {
    String captcha = user.getCaptcha();
    HttpSession session = request.getSession();
    String sessionCaptcha = (String) session.getAttribute("captchaCode");
    
    if (captcha == null || sessionCaptcha == null || !captcha.equalsIgnoreCase(sessionCaptcha)) {
        return ResultVO.error("验证码错误或已过期");
    }
    session.removeAttribute("captchaCode");
    return userService.login(user.getUsername(), user.getPassword());
}
}