package org.springbus.moban.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.entity.ClazzInfo;
import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.entity.UserInfo;
import org.springbus.moban.service.ClazzInfoService;
import org.springbus.moban.service.UserInfoService;
import org.springbus.moban.emus.ResponseCode;
import org.springbus.moban.exception.CommonException;
import org.springbus.moban.web.LoginUserCache;
import org.springbus.moban.web.request.BaseRequest;
import org.springbus.moban.web.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Slf4j
@Tag(name = "用户信息控制器")
@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ClazzInfoService clazzInfoService;

    @PostMapping("/login")
    @Operation(summary = "登陆")
    public CommonResponse<UserEntity> login(@Parameter(description = "用户信息", required = true) @RequestBody UserEntity info) {
        if (Objects.isNull(info) || StringUtils.isBlank(info.getPassword())
                || StringUtils.isBlank(info.getAccount())) {
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        }
        try {
            UserEntity entity = userInfoService.login(info.getAccount(), info.getPassword());
            String token = UUID.randomUUID().toString().replace("-", "");
            entity.setToken(token);
            entity.setActivityTime(new Date());
            //LoginUserCache.putGuavaCache(token, entity) 将生成的 token 和用户实体存储到了名为 LoginUserCache 的 Guava 缓存中。
            //后续请求可以通过 token 快速从缓存中获取用户信息，而不需要每次都查询数据库。token 用作会话标识符，帮助维持用户的登录状态。
            // 每次用户发起请求时，服务端可以根据 token 从缓存中取出对应的用户信息，验证其合法性并执行相应的操作。
            LoginUserCache.putGuavaCache(token, entity);
            return new CommonResponse<>(entity);
        } catch (CommonException e) {
            log.info("登陆异常", e);
            return new CommonResponse<>(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("登陆异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }

    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public CommonResponse<UserEntity> register(@Parameter(description = "用户信息", required = true) @RequestBody UserEntity info) {
        if (Objects.isNull(info) || StringUtils.isBlank(info.getPassword())
                || StringUtils.isBlank(info.getAccount())) {
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        }
        try {
            boolean flag = userInfoService.register(info.getAccount(), info.getPassword());
            if (!flag) {
                return new CommonResponse<>(ResponseCode.PARAM_ERROR.getCode(), "用户已存在");
            }
            return new CommonResponse<>(ResponseCode.SUCCESS);
        } catch (Exception e) {
            log.error("注册异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/login-out")
    @Operation(summary = "退出")
    public CommonResponse<Boolean> loginOut(@Parameter(description = "系统基础信息", required = true) @RequestBody BaseRequest request) {
        try {
            LoginUserCache.removeGuavaCache(request.getToken());
            return new CommonResponse<>(ResponseCode.SUCCESS);
        } catch (Exception e) {
            log.error("退出异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/detail")
    @Operation(summary = "获取个人信息详情")
    public CommonResponse<UserEntity> getDetail(HttpServletRequest request) {
        try {
            UserEntity user = LoginUserCache.getGuavaCache(request.getHeader("token"));
            if (Objects.isNull(user)) {
                return new CommonResponse<>(ResponseCode.NO_AUTH.getCode(), "登陆已失效，请重新登陆");
            }
            UserInfo userInfo = userInfoService.getById(user.getId());
            UserEntity userEntity = Optional.ofNullable(userInfo).map(e -> {
                ClazzInfo clazzInfo = clazzInfoService.getById(e.getClazzId());
                UserEntity entity = new UserEntity().setId(e.getId()).setUserName(e.getUserName()).setEmail(e.getEmail())
                        .setPhone(e.getPhone()).setMethod(e.getMethod()).setAccount(e.getAccount());
                if (Objects.nonNull(clazzInfo)) {
                    entity.setClazzName(clazzInfo.getClazzName()).setGrade(clazzInfo.getGrade()).setSchoolName(clazzInfo.getSchoolName());
                }
                return entity;
            }).orElse(new UserEntity());
            return new CommonResponse<>(userEntity);
        } catch (Exception e) {
            log.error("获取个人信息详情异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/modified/detail")
    @Operation(summary = "修改个人信息详情")
    public CommonResponse<Object> modifiedDetail(@Parameter(description = "用户信息", required = true) @RequestBody UserEntity entity) {
        if (Objects.isNull(entity) && Objects.isNull(entity.getId())) {
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        }
        try {
            UserInfo userInfo = new UserInfo().setUserName(entity.getUserName()).setEmail(entity.getEmail())
                    .setPhone(entity.getPhone()).setMethod(entity.getMethod())
                    .setClazzId(StringUtils.isBlank(entity.getClazzId())?null:Long.valueOf(entity.getClazzId()))
                    .setModified("sys").setModifiedDate(new Date());
            boolean flag = userInfoService.update(userInfo, new QueryWrapper<UserInfo>().eq("id", entity.getId()));
            return new CommonResponse<>(flag ? ResponseCode.SUCCESS : ResponseCode.DB_ERROR);
        } catch (Exception e) {
            log.error("修改个人信息详情异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/all/list")
    @Operation(summary = "获取所有用户列表")
    public CommonResponse<List<UserEntity>> getAllList() {
        try {
            List<UserInfo> result = userInfoService.list();
            List<UserEntity> collect = Optional.ofNullable(result).orElse(new ArrayList<>()).stream()
                    .map(e -> new UserEntity().setId(e.getId())
                            .setClazzId(null == e.getClazzId() ? "" : e.getClazzId().toString())
                            .setUserName(e.getUserName())
                            .setAccount(e.getAccount())
                            .setEmail(e.getEmail())
                            .setPhone(e.getPhone())
                            .setMethod(e.getMethod())).collect(Collectors.toList());
            return new CommonResponse<>(collect);
        }catch (Exception e){
            log.error("获取所有用户列表异常", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }
}
