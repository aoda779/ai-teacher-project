package org.springbus.moban.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.emus.ResponseCode;
import org.springbus.moban.entity.ClazzInfo;
import org.springbus.moban.entity.StudentInfo;
import org.springbus.moban.service.ClazzInfoService;
import org.springbus.moban.service.StudentInfoService;
import org.springbus.moban.web.LoginUserCache;
import org.springbus.moban.web.entity.StudentEntity;
import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.web.response.CommonResponse;
import org.springbus.moban.web.response.PageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生信息表 前端控制器
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Slf4j
@Tag(name = "考生信息管理")
@RestController
//定义了该控制器下所有方法的基础路径。
@RequestMapping("/student-info")
public class StudentInfoController {

    @Resource
    private StudentInfoService studentInfoService;

    @Resource
    private ClazzInfoService clazzInfoService;

    @GetMapping("/page")
    @Operation(summary = "分页查询考生信息")
    public PageResponse<StudentEntity> getPage(@Parameter(description = "页码", required = true) Long page,
                                               @Parameter(description = "条数", required = true) Long limit,
                                               @Parameter(description = "姓名/学号/班级") String param) {
        try {
            page = page == null ? 1 : page;
            limit = limit == null ? 10 : limit;
            List<ClazzInfo> clazzInfos = clazzInfoService.list();
            log.info("getPage clazzInfos={}", clazzInfos);
            Map<Long, ClazzInfo> clazzMap = Optional.ofNullable(clazzInfos).orElse(Lists.newArrayList()).stream()
                    .collect(Collectors.toMap(ClazzInfo::getId, e -> e, (k1, k2) -> k1));
            Long clazzId = Optional.ofNullable(clazzInfos).orElse(Lists.newArrayList()).stream()
                    .filter(e -> StringUtils.equals(e.getClazzName(), param))
                    .map(ClazzInfo::getId)
                    .findFirst().orElse(null);
            log.info("getPage clazzId={}", clazzId);

            Page<StudentInfo> studentInfoPage = studentInfoService.page(new Page<>(page, limit), new QueryWrapper<StudentInfo>()
                    .eq(StringUtils.isNotBlank(param), "name", param).or()
                    .eq(StringUtils.isNotBlank(param), "id", param).or()
                    .eq(Objects.nonNull(clazzId), "clazz_id", clazzId));
            List<StudentEntity> entityList = Optional.ofNullable(studentInfoPage).map(Page::getRecords).orElse(Lists.newArrayList()).stream()
                    .map(e -> {
                        StudentEntity studentEntity = new StudentEntity();
                        BeanUtils.copyProperties(e, studentEntity);
                        ClazzInfo clazzInfo = clazzMap.get(e.getClazzId());
                        if (Objects.nonNull(clazzInfo)) {
                            studentEntity.setSchoolName(clazzInfo.getSchoolName());
                            studentEntity.setGrade(clazzInfo.getGrade());
                            studentEntity.setClazzName(clazzInfo.getClazzName());
                        }
                        return studentEntity;
                    }).collect(Collectors.toList());
            long total = Optional.ofNullable(studentInfoPage).map(Page::getTotal).orElse(0L);
            return new PageResponse<>(entityList, total);
        } catch (Exception e) {
            log.error("分页查询班级信息异常", e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/detail")
    @Operation(summary = "查询考生详情信息")
    public CommonResponse<StudentEntity> getDetail(Long id) {
        try {
            StudentInfo studentInfo = studentInfoService.getById(id);
            StudentEntity entity = Optional.ofNullable(studentInfo).map(e -> {
                StudentEntity studentEntity = new StudentEntity();
                BeanUtils.copyProperties(e, studentEntity);
                return studentEntity;
            }).orElse(null);
            return new CommonResponse<>(entity);
        } catch (Exception e) {
            log.error("查询学生详情信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/save-or-up")
    @Operation(summary = "新增或修改学生信息")
    public CommonResponse<Boolean> updateOrAddUser(@Parameter(description = "学生信息", required = true) @RequestBody StudentEntity entity
            , HttpServletRequest request) {
        if (Objects.isNull(entity)) {
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        }

        UserEntity user = LoginUserCache.getGuavaCache(request.getHeader("Token"));
        StudentInfo studentInfo = new StudentInfo();
        String userName = Objects.isNull(user) ? "sys" : user.getUserName();
        studentInfo.setClazzId(entity.getClazzId()).setName(entity.getName())
                .setId(entity.getId());
        //判断是否是修改还是新增,ID为空则为新增
        if (Objects.nonNull(entity.getId())) {
            studentInfo.setModified(userName).setModifiedDate(new Date());
        } else {
            studentInfo.setCreated(userName).setCreatedDate(new Date());
        }
        try {
            boolean flag = studentInfoService.saveOrUpdate(studentInfo);
            return new CommonResponse<>(flag);
        } catch (Exception e) {
            log.error("新增或修改学生信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/remove/batch")
    @Operation(summary = "批量删除学生信息")
    public CommonResponse<Boolean> removeUser(@Parameter(description = "学生信息集合", required = true) @RequestBody List<StudentEntity> list) {
        try {
            List<Long> ids = Optional.ofNullable(list).orElse(Lists.newArrayList())
                    .stream().map(StudentEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
            boolean flag = studentInfoService.removeByIds(ids);
            return new CommonResponse<>(flag);
        } catch (Exception e) {
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }
}
