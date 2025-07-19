package com.yc.productionreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.excel.EasyExcel;
import com.yc.productionreport.dto.ProductionSummaryDTO;
import com.yc.productionreport.entity.ProductionReport;
import com.yc.productionreport.mapper.ProductionReportMapper;
import com.yc.productionreport.service.ProductionReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.io.IOException;

/**
 *  描述: 生产报表服务实现类
 */
@Service
public class ProductionReportServiceImpl extends ServiceImpl<ProductionReportMapper, ProductionReport> implements ProductionReportService {

    // 缓存管理器
    @Autowired
    private CacheManager cacheManager;

    // 分页查询
    @Override
    public IPage<ProductionReport> pageQuery(Map<String, Object> params) {
        long pageNum = params.get("pageNum") != null ? Long.parseLong(params.get("pageNum").toString()) : 1;
        long pageSize = params.get("pageSize") != null ? Long.parseLong(params.get("pageSize").toString()) : 10;
        Page<ProductionReport> page = new Page<>(pageNum, pageSize);

        QueryWrapper<ProductionReport> queryWrapper = createQueryWrapper(params);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public ProductionSummaryDTO getSummaryStatistics(Map<String, Object> params) {
        QueryWrapper<ProductionReport> queryWrapper = createQueryWrapper(params);
        List<ProductionReport> reports = baseMapper.selectList(queryWrapper);

        ProductionSummaryDTO summary = new ProductionSummaryDTO();
        if (reports.isEmpty()) {
            summary.setTotalProduction(0);
            summary.setTotalDefects(0);
            summary.setAverageDailyProduction(BigDecimal.ZERO);
            summary.setAverageDefectRate(BigDecimal.ZERO);
            return summary;
        }

        // 计算总生产量和总不良数
        int totalProduction = reports.stream()
                .mapToInt(report -> report.getCompletedQuantity() != null ? report.getCompletedQuantity() : 0)
                .sum();
        int totalDefects = reports.stream()
                .mapToInt(report -> report.getDefectCount() != null ? report.getDefectCount() : 0)
                .sum();

        // 计算天数（去重）
        long days = reports.stream()
                .map(report -> report.getReportDate())
                .distinct()
                .count();

        // 计算平均日产量
        BigDecimal avgDailyProduction = days > 0 ?
                BigDecimal.valueOf(totalProduction).divide(BigDecimal.valueOf(days), 1, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        // 计算平均不良率
        BigDecimal defectRate = totalProduction > 0 ?
                BigDecimal.valueOf(totalDefects).divide(BigDecimal.valueOf(totalProduction), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)) :
                BigDecimal.ZERO;

        summary.setTotalProduction(totalProduction);
        summary.setTotalDefects(totalDefects);
        summary.setAverageDailyProduction(avgDailyProduction);
        summary.setAverageDefectRate(defectRate);

        return summary;
    }

    @Override
    public void exportProductionReport(Map<String, Object> params, HttpServletResponse response) throws IOException {
        // 创建查询条件
        QueryWrapper<ProductionReport> queryWrapper = createQueryWrapper(params);
        List<ProductionReport> reports = baseMapper.selectList(queryWrapper);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("生产报表数据", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 写入Excel并响应
        EasyExcel.write(response.getOutputStream(), ProductionReport.class)
                .sheet("生产报表")
                .doWrite(reports);
    }

    private QueryWrapper<ProductionReport> createQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ProductionReport> queryWrapper = new QueryWrapper<>();
        if (params.get("reportDate") != null) {
            queryWrapper.eq("report_date", params.get("reportDate"));
        }
        if (params.get("machineId") != null) {
            queryWrapper.eq("machine_id", params.get("machineId"));
        }
        if (params.get("startDate") != null && params.get("endDate") != null) {
            queryWrapper.between("report_date", params.get("startDate"), params.get("endDate"));
        } else if (params.get("startDate") != null) {
            queryWrapper.ge("report_date", params.get("startDate"));
        } else if (params.get("endDate") != null) {
            queryWrapper.le("report_date", params.get("endDate"));
        }
        return queryWrapper;
    }

    // 根据id查询
    @Cacheable(value = "productionReport", key = "#id")
    @Override
    public ProductionReport getById(Long id) {
        return super.getById(id);
    }


    // 保存
    @Override
    public boolean save(ProductionReport entity) {
        boolean saved = baseMapper.insert(entity) > 0;
        if (saved) {
            Cache cache = cacheManager.getCache("productionReport");
            if (cache != null) {
                cache.put(entity.getId(), entity);
            }
        }
        return saved;
    }

    @Override
    public Map<String, Object> getProductionSummary(Map<String, Object> params) {
        ProductionSummaryDTO summaryDTO = getSummaryStatistics(params);
        return Map.of(
            "totalProduction", summaryDTO.getTotalProduction(),
            "avgDailyProduction", summaryDTO.getAverageDailyProduction(),
            "totalDefects", summaryDTO.getTotalDefects(),
            "avgDefectRate", summaryDTO.getAverageDefectRate()
        );
    }

    // 修改
    @Override
    @CachePut(value = "productionReport", key = "#entity.id")
    public boolean updateById(ProductionReport entity) {
        return super.updateById(entity);
    }

    // 删除
    @CacheEvict(value = "productionReport", key = "#id")
    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }
}