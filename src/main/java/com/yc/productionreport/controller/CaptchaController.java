package com.yc.productionreport.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/api")
public class CaptchaController {

    private final Producer kaptchaProducer;

    @Autowired
    public CaptchaController(Producer kaptchaProducer) {
        this.kaptchaProducer = kaptchaProducer;
    }

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应头，禁止缓存
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");

        // 生成验证码文本
        String capText = kaptchaProducer.createText();
        System.out.println("验证码文本：" + capText);

        // 将验证码文本存入session
        HttpSession session = request.getSession();
        session.setAttribute("captchaCode", capText);

        // 生成验证码图片
        BufferedImage bi = kaptchaProducer.createImage(capText);
        OutputStream out = response.getOutputStream();
        ImageIO.write(bi, "png", out);

        // 确保所有内容都被写出
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}