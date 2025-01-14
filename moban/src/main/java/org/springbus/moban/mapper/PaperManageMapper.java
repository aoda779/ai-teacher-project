package org.springbus.moban.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springbus.moban.entity.PaperManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springbus.moban.entity.PaperManageEntity;

/**
 * <p>
 * 试卷信息表 Mapper 接口
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Mapper
public interface PaperManageMapper extends BaseMapper<PaperManage> {

    Page<PaperManageEntity> selectPaperManagePage(Page<PaperManageEntity> page,  @Param("ew") Wrapper<PaperManageEntity> wrapper);
}
