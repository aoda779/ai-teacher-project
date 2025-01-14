package org.springbus.moban.service.impl;

import org.springbus.moban.entity.StudentInfo;
import org.springbus.moban.mapper.StudentInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springbus.moban.service.StudentInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生信息表 服务实现类
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements StudentInfoService {

}
