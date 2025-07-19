package com.yc.productionreport.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常:", e);
        return Result.error(500, "系统异常，请联系管理员");
    }

    // 处理参数类型不匹配异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型不匹配异常:", e);
        return Result.error(400, "参数格式错误: " + e.getName());
    }

    // 处理空指针异常
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常:", e);
        return Result.error(500, "数据不存在或已删除");
    }

    // 处理数据校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String message = String.join("; ", errorMessages);
        log.error("数据校验失败: {}", message);
        return Result.error(400, "参数校验失败: " + message);
    }

    // 处理通用异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("通用异常:", e);
        return Result.error(500, "操作失败: " + e.getMessage());
    }
}