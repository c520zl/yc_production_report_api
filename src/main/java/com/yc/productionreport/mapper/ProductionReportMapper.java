package com.yc.productionreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.productionreport.entity.ProductionReport;
import org.apache.ibatis.annotations.Mapper;
/**
 * 生产报表 Mapper 接口
 *
 */
@Mapper
public interface ProductionReportMapper extends BaseMapper<ProductionReport> {
}