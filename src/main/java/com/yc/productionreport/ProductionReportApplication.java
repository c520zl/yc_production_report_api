package com.yc.productionreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 *  启动类
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.yc.productionreport.mapper")
public class ProductionReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductionReportApplication.class, args);
    }
}