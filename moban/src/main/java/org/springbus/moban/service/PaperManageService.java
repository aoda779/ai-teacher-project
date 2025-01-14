package org.springbus.moban.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springbus.moban.entity.PaperManage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springbus.moban.entity.PaperManageEntity;

/**
 * <p>
 * 试卷信息表 服务类
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
public interface PaperManageService extends IService<PaperManage> {

    Page<PaperManageEntity> getPaperManagePage(long pageNum, long pageSize, PaperManageEntity paperManageEntity);
}
