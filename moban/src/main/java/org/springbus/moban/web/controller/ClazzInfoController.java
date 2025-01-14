package org.springbus.moban.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springbus.moban.entity.UserInfo;
import org.springbus.moban.service.ClazzInfoService;
import org.springbus.moban.service.UserInfoService;
import org.springbus.moban.web.LoginUserCache;
import org.springbus.moban.web.entity.ClazzEntity;
import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.web.response.CommonResponse;
import org.springbus.moban.web.response.PageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 班级信息表 前端控制器
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
//构建用户信息映射表:
//将用户信息列表转换成一个 Map，以用户 ID 作为键，便于后续快速查找用户信息。
//分页查询班级信息:
//创建分页请求对象并设置分页参数。
//构建查询条件，根据提供的参数对班级名称、年级或者用户 ID 进行匹配查询。
//注意：这里的逻辑是当参数不为空时才进行相应的条件匹配。
//处理查询结果:
//将查询到的班级信息列表转换为目标实体类型 ClazzEntity。
//在转换过程中，通过关联的用户 ID 查找并设置用户的账户名和其他相关信息（如方法）。
//计算总记录数:
//从分页结果中提取总记录数，如果没有分页结果则使用当前列表的大小。
@Slf4j
@Tag(name = "班级信息管理")
@RestController
@RequestMapping("/clazz-info")
public class ClazzInfoController {

    @Resource
    private ClazzInfoService clazzInfoService;

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/page")
    @Operation(summary = "分页查询班级信息")
    public PageResponse<ClazzEntity> getClazzInfoPage(@Parameter(description = "页码", required = true) Long page,
                                                      @Parameter(description = "条数", required = true) Long limit,
                                                      @Parameter(description = "入参") String param) {

        try {
            // 如果传入的页码为空，则默认设置为第一页
            page = page == null ? 1 : page;
            // 如果传入的每页显示条数为空，则默认设置为每页10条
            limit = limit == null ? 10 : limit;
            List<UserInfo> infoList = userInfoService.list();
            UserInfo info = Optional.ofNullable(infoList).orElse(Lists.newArrayList()).stream()
                    .filter(e -> StringUtils.equals(e.getAccount(), param)).findFirst().orElse(new UserInfo());
            Map<Long, UserInfo> userInfoMap = Optional.ofNullable(infoList).orElse(Lists.newArrayList()).stream()
                    .collect(Collectors.toMap(UserInfo::getId, e -> e, (k1, k2) -> k1));
            // 分页查询班级信息，并根据参数进行条件筛选
            Page<ClazzInfo> result = clazzInfoService.page(new Page<>(page, limit), new QueryWrapper<ClazzInfo>()
                    // 当参数不为空时，按班级名称或年级或用户ID进行匹配查询
                    .eq(StringUtils.isNotBlank(param), "clazz_name", param).or()
                    .eq(StringUtils.isNotBlank(param), "grade", param).or()
                    .eq(StringUtils.isNotBlank(param) && Objects.nonNull(info.getId()), "user_id", info.getId()));
            // 将查询结果转换为 ClazzEntity 列表，并关联用户信息
            List<ClazzEntity> list = Optional.ofNullable(result).map(IPage::getRecords).orElse(Lists.newArrayList()).stream().map(e -> {
                ClazzEntity entity = new ClazzEntity();
                BeanUtils.copyProperties(e, entity);
                UserInfo userInfo = userInfoMap.get(e.getUserId());
                if (null != userInfo) {
                    entity.setAccount(userInfo.getAccount()).setMethod(userInfo.getMethod());
                }
                return entity;
            }).collect(Collectors.toList());
            long total = Optional.ofNullable(result).map(IPage::getTotal).orElse((long) list.size());
            return new PageResponse<>(list, total);
        } catch (Exception e) {
            log.error("分页查询班级信息异常", e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/list")
    @Operation(summary = "查询所有班级信息")
    public CommonResponse<List<ClazzEntity>> getClazzInfoList() {
        try {
            List<UserInfo> infoList = userInfoService.list();
            Map<Long, UserInfo> userInfoMap = Optional.ofNullable(infoList).orElse(Lists.newArrayList()).stream()
                    .collect(Collectors.toMap(UserInfo::getId, e -> e, (k1, k2) -> k1));
            List<ClazzInfo> result = clazzInfoService.list();
            List<ClazzEntity> list = Optional.ofNullable(result).orElse(Lists.newArrayList()).stream().
                    map(e -> {
                        ClazzEntity entity = new ClazzEntity();
                        BeanUtils.copyProperties(e, entity);
                        UserInfo userInfo = userInfoMap.get(e.getUserId());
                        if (null != userInfo) {
                            entity.setAccount(userInfo.getAccount()).setMethod(userInfo.getMethod());
                        }
                        return entity;
                    }).collect(Collectors.toList());
            return new CommonResponse<>(list);
        } catch (Exception e) {
            log.error("查询所有班级信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/detail")
    @Operation(summary = "查询班级详情")
    public CommonResponse<ClazzEntity> getClazzDetail(Long id) {
        try {
            List<UserInfo> infoList = userInfoService.list();
            Map<Long, UserInfo> userInfoMap = Optional.ofNullable(infoList).orElse(Lists.newArrayList()).stream()
                    .collect(Collectors.toMap(UserInfo::getId, e -> e, (k1, k2) -> k1));
            ClazzInfo result = clazzInfoService.getById(id);
            ClazzEntity entity = Optional.ofNullable(result).
                    map(e -> {
                        ClazzEntity clazzEntity = new ClazzEntity();
                        BeanUtils.copyProperties(e, clazzEntity);
                        UserInfo userInfo = userInfoMap.get(e.getUserId());
                        if (null != userInfo) {
                            clazzEntity.setAccount(userInfo.getAccount()).setMethod(userInfo.getMethod());
                        }
                        return clazzEntity;
                    }).orElse(null);
            return new CommonResponse<>(entity);
        } catch (Exception e) {
            log.error("查询所有班级信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/save-or-up")
    @Operation(summary = "新增或修改班级信息")
    public CommonResponse<Boolean> updateOrAddClazz(HttpServletRequest request, @Parameter(description = "班级信息", required = true) @RequestBody ClazzEntity entity) {
        if (Objects.isNull(entity)) {
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        }

        UserEntity user = LoginUserCache.getGuavaCache(request.getHeader("Token"));
        ClazzInfo clazzInfo = new ClazzInfo();
        clazzInfo.setId(entity.getId()).setGrade(entity.getGrade()).setSchoolName(entity.getSchoolName())
                .setClazzName(entity.getClazzName()).setUserId(entity.getUserId());
        //判断是否是修改还是新增,ID为空则为新增
        String userName = Objects.isNull(user)?"sys":user.getUserName();
        if (Objects.nonNull(entity.getId())) {
            clazzInfo.setModified(userName).setModifiedDate(new Date());
        } else {
            clazzInfo.setCreated(userName).setCreatedDate(new Date());
        }
        try {
            boolean flag = clazzInfoService.saveOrUpdate(clazzInfo);
            return new CommonResponse<>(flag);
        } catch (Exception e) {
            log.error("新增或修改班级信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/remove/batch")
    @Operation(summary = "批量删除班级信息")
    public CommonResponse<Boolean> removeClazz(@Parameter(description = "班级信息集合", required = true) @RequestBody List<ClazzEntity> list) {
        try {
            List<Long> ids = Optional.ofNullable(list).orElse(Lists.newArrayList())
                    .stream().map(ClazzEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
            boolean flag = clazzInfoService.removeByIds(ids);
            return new CommonResponse<>(flag);
        } catch (Exception e) {
            log.error("批量删除班级信息异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }
}
