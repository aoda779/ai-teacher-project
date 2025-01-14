package org.springbus.moban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.entity.UserInfo;
import org.springbus.moban.mapper.UserInfoMapper;
import org.springbus.moban.service.UserInfoService;
import org.springbus.moban.emus.ResponseCode;
import org.springbus.moban.exception.CommonException;
import org.springbus.moban.utils.AESUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public UserEntity login(String account, String password) {
        //检查 password 和 account 是否为空或空白字符串。如果是，则抛出 CommonException 异常，响应码为 PARAM_ERROR。
        if (StringUtils.isBlank(password) || StringUtils.isBlank(account)) {
            throw new CommonException(ResponseCode.PARAM_ERROR);
        }
        //使用 AESUtil.encrypt(password) 对输入的密码进行加密，以确保存储在数据库中的密码是安全的。
        String newPassword = AESUtil.encrypt(password);
        //使用 QueryWrapper<UserInfo> 构建查询条件，匹配 password 和 account 字段
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("password", newPassword);
        userInfoQueryWrapper.eq("account", account);
        //这行代码的作用是根据指定的查询条件从数据库中检索 UserInfo 实体对象列表。
        List<UserInfo> list = this.list(userInfoQueryWrapper);
        if (CollectionUtils.isEmpty(list)){
            throw new CommonException(ResponseCode.BUSINESS_ERROR,"用户不存在");
        }
        if (list.size() > 1) {
            log.error("用户重复");
            throw new CommonException(ResponseCode.BUSINESS_ERROR,"用户重复");
        }
        //将查询到的 UserInfo 对象列表转换为 UserEntity 对象，并返回第一个匹配的结果。如果没有任何匹配结果，则返回 null。
        return list.stream().map(e -> {
            UserEntity entity = new UserEntity();
            entity.setId(e.getId());
            entity.setClazzId(Optional.ofNullable(e.getClazzId()).map(String::valueOf).orElse(""));
            entity.setUserName(e.getUserName());
            entity.setPassword(e.getPassword());
            entity.setEmail(e.getEmail());
            entity.setPhone(e.getPhone());
            entity.setAccount(e.getAccount());
            return entity;
        }).findFirst().orElse(null);
    }

    @Override
    public boolean register(String account,String password) {
          // 参数验证
        if (StringUtils.isBlank(password) || StringUtils.isBlank(account)) {
            throw new CommonException(ResponseCode.PARAM_ERROR);
        }
        //密码加密
        String newPassword = AESUtil.encrypt(password);
        // 创建 UserInfo 对象并设置属性
        UserInfo userInfo=new UserInfo();
        userInfo.setPassword(newPassword).setAccount(account);
        // 构建查询条件
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("account", account);
        List<UserInfo> list = this.list(userInfoQueryWrapper);
        if (CollectionUtils.isNotEmpty(list)){
            log.info("用户已存在");
            return false;
        }
        return this.save(userInfo);
    }
}
