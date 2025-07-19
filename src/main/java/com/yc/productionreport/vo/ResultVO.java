package com.yc.productionreport.vo;

import lombok.Data;

/**
 * 统一接口返回结果VO
 */
@Data
public class ResultVO<T> {
    private Integer code; // 状态码：0-成功，1-失败
    private String message; // 提示信息
    private T data; // 业务数据

    /**
     * 成功返回结果
     */
    public static <T> ResultVO<T> success(String message, T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(0);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果（无数据）
     */
    public static <T> ResultVO<T> success(String message) {
        return success(message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> ResultVO<T> error(String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(1);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}