package com.yc.productionreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.productionreport.dto.ProductionSummaryDTO;
import com.yc.productionreport.entity.ProductionReport;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;
import java.io.IOException;

/**
 *  生产报表服务
 */
public interface ProductionReportService extends IService<ProductionReport> {
    // 分页查询生产报表
    IPage<ProductionReport> pageQuery(Map<String, Object> params);

    // 获取生产汇总统计数据
    ProductionSummaryDTO getSummaryStatistics(Map<String, Object> params);

    // 导出生产报表
    void exportProductionReport(Map<String, Object> params, HttpServletResponse response) throws IOException;

    // 根据id查询生产报表
    @Cacheable(value = "productionReport", key = "#id")
    ProductionReport getById(Long id);

    // 修改生产报表
    @CacheEvict(value = "productionReport", key = "#id")
    boolean removeById(Long id);

    // 保存生产报表
    @CachePut(value = "productionReport", key = "#result.id")
    boolean save(ProductionReport entity);

    Map<String, Object> getProductionSummary(Map<String, Object> params);
}