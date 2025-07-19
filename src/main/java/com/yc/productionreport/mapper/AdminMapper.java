package com.yc.productionreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.productionreport.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper接口
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 根据用户名查询管理员
     * @param username 管理员用户名
     * @return 管理员信息
     */
    Admin selectByUsername(String username);
}