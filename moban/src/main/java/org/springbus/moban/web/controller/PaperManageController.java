package org.springbus.moban.web.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springbus.moban.emus.ResponseCode;
import org.springbus.moban.entity.PaperManage;
import org.springbus.moban.entity.PaperManageEntity;
import org.springbus.moban.service.PaperManageService;
import org.springbus.moban.web.LoginUserCache;
import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.web.response.CommonResponse;
import org.springbus.moban.web.response.PageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 试卷信息表 前端控制器
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Slf4j
@Tag(name = "试卷信息管理")
@RestController
@RequestMapping("/paper-manage")
public class PaperManageController {

    @Resource
    private PaperManageService paperManageService;

    @GetMapping("/page")
    @Operation(summary = "分页查询试卷信息")
    public PageResponse<PaperManageEntity> getPaperManagePage(@Parameter(description = "页码", required = true) Long page,
                                                              @Parameter(description = "条数", required = true) Long limit,
                                                              @Parameter(description = "入参") String param) {

        PaperManageEntity entity = new PaperManageEntity();
        entity.setSubjectName(param).setClazzName(param).setName(param);
        page = page == null ? 1 : page;
        limit = limit == null ? 10 : limit;
        try {
            Page<PaperManageEntity> result = paperManageService.getPaperManagePage(page, limit, entity);
            List<PaperManageEntity> list = Optional.ofNullable(result).map(IPage::getRecords).orElse(Lists.newArrayList());
            long total = Optional.ofNullable(result).map(IPage::getTotal).orElse((long) list.size());
            return new PageResponse<>(list, total);
        } catch (Exception e) {
            log.error("分页查询试卷信息异常", e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/detail")
    @Operation(summary = "查询试卷详情信息")
    public CommonResponse<PaperManageEntity> getPaperDetail(Long id) {
        try {
            PaperManage paper = paperManageService.getById(id);
            PaperManageEntity paperManageEntity = Optional.ofNullable(paper).map(e -> {
                PaperManageEntity entity = new PaperManageEntity();
                BeanUtils.copyProperties(e, entity);
                if (null != paper.getPaperTime()) {
                    entity.setPaperTime(DateFormatUtils.format(paper.getPaperTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                return entity;
            }).orElse(new PaperManageEntity());
            return new CommonResponse<>(paperManageEntity);
        } catch (Exception e) {
            log.error("查询试卷详情信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/save-or-up")
    @Operation(summary = "新增或修改试卷信息")
    public CommonResponse<Boolean> updateOrAddPaperManage(@Parameter(description = "试卷信息", required = true) @RequestBody PaperManageEntity entity
            , HttpServletRequest request) {
        if (Objects.isNull(entity)) {
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        }

        UserEntity user = LoginUserCache.getGuavaCache(request.getHeader("Token"));
        PaperManage paper = new PaperManage();
        DateTime dateTime = DateUtil.parse(entity.getPaperTime(), "yyyy-MM-dd HH:mm:ss");
        String userName = Objects.isNull(user) ? "sys" : user.getUserName();
        paper.setClazzId(entity.getClazzId()).setSubjectName(entity.getSubjectName())
                .setName(entity.getName()).setPaperTime(dateTime).setId(entity.getId());
        //判断是否是修改还是新增,ID为空则为新增
        if (Objects.nonNull(entity.getId())) {
            paper.setModified(userName).setModifiedDate(new Date());
        } else {
            paper.setCreated(userName).setCreatedDate(new Date());
        }
        try {
            boolean flag = paperManageService.saveOrUpdate(paper);
            return new CommonResponse<>(flag);
        } catch (Exception e) {
            log.error("新增或修改试卷信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/remove/batch")
    @Operation(summary = "批量删除试卷信息")
    public CommonResponse<Boolean> removePaperManage(@Parameter(description = "试卷信息集合", required = true) @RequestBody List<PaperManageEntity> list) {
        try {
            List<Long> ids = Optional.ofNullable(list).orElse(Lists.newArrayList())
                    .stream().map(PaperManageEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
            boolean flag = paperManageService.removeByIds(ids);
            return new CommonResponse<>(flag);
        } catch (Exception e) {
            log.error("批量删除试卷信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }
}
