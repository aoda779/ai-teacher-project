package org.springbus.moban.mapper;

import org.springbus.moban.entity.StudentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生信息表 Mapper 接口
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Mapper
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {

}
