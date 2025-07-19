package com.yc.productionreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.productionreport.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 普通用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);
}