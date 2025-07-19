package com.yc.productionreport.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.productionreport.common.Result;
import com.yc.productionreport.entity.ProductionReport;
import com.yc.productionreport.service.ProductionReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 *  生产报表控制层
 */
@RestController
@RequestMapping("/api/production-report")
public class ProductionReportController {

    @Autowired
    private ProductionReportService productionReportService;

    // 分页查询
    @GetMapping("/page")
    public Result<IPage<ProductionReport>> pageQuery(@RequestParam Map<String, Object> params) {
        IPage<ProductionReport> page = productionReportService.pageQuery(params);
        return Result.success(page);
    }

    // 生产汇总统计接口
    @GetMapping("/summary")
    public Result<Map<String, Object>> getProductionSummary(@RequestParam Map<String, Object> params) {
        Map<String, Object> summaryData = productionReportService.getProductionSummary(params);
        return Result.success(summaryData);
    }

    // 根据ID查询
    @GetMapping("/{id}")
    public Result<ProductionReport> getById(@PathVariable Long id) {
        ProductionReport report = productionReportService.getById(id);
        return Result.success(report);
    }

    // 新增
    @PostMapping
    public Result<Boolean> save(@Valid @RequestBody ProductionReport report) {
        return Result.success(productionReportService.save(report));
    }

    // 修改
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody ProductionReport report) {
        return Result.success(productionReportService.updateById(report));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(productionReportService.removeById(id));
    }
}