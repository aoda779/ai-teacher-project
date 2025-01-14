package org.springbus.moban.mapper;

import org.springbus.moban.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Mapper
//提供 CRUD 功能：通过继承 BaseMapper<UserInfo>，它获得了一套完整的 CRUD 操作方法，可以直接用于对 UserInfo 实体进行数据库操作，而无需额外编写 SQL 语句。
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
