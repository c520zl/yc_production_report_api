package com.yc.productionreport.util;

import org.springframework.util.DigestUtils;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加密工具类，提供密码加盐哈希和验证功能
 */
public class PasswordUtils {
    private static final int SALT_LENGTH = 16; // 盐值长度
    private static final int HASH_ITERATIONS = 1024; // 哈希迭代次数

    /**
     * 生成随机盐值
     * @return 盐值
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 对密码进行加盐哈希
     * @param password 原始密码
     * @param salt 盐值
     * @return 哈希后的密码
     */
    public static String hashPassword(String password, String salt) {
        String hash = password + salt;
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            hash = DigestUtils.md5DigestAsHex(hash.getBytes());
        }
        return hash;
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @param salt 盐值
     * @return 是否验证通过
     */
    public static boolean verifyPassword(String rawPassword, String encodedPassword, String salt) {
        return encodedPassword.equals(hashPassword(rawPassword, salt));
    }
}