package org.springbus.moban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.entity.PaperManage;
import org.springbus.moban.entity.PaperManageEntity;
import org.springbus.moban.mapper.PaperManageMapper;
import org.springbus.moban.service.PaperManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 试卷信息表 服务实现类
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
//将该类注册为一个服务组件，使其可以被自动检测和注册为 Bean。这意味着你可以通过依赖注入的方式在其他地方使用它。
@Service
public class PaperManageServiceImpl extends ServiceImpl<PaperManageMapper, PaperManage> implements PaperManageService {

    @Resource
    private PaperManageMapper paperManageMapper;

    //定义了一个自定义方法 getPaperManagePage 用于分页查询试卷信息，支持根据班级名称、试卷名称和科目名称进行筛选。
    @Override
    public Page<PaperManageEntity> getPaperManagePage(long pageNum, long pageSize, PaperManageEntity entity) {
        Page<PaperManageEntity> paperManageEntityIPage = paperManageMapper.selectPaperManagePage(new Page<>(pageNum, pageSize), new QueryWrapper<PaperManageEntity>()
                .eq(StringUtils.isNotBlank(entity.getClazzName()), "clazz.clazz_name", entity.getClazzName()).or()
                .eq(StringUtils.isNotBlank(entity.getName()), "paper.name", entity.getName()).or()
                .eq(StringUtils.isNotBlank(entity.getSubjectName()), "paper.subject_name", entity.getSubjectName()));
        return paperManageEntityIPage;
    }
}
