package org.springbus.moban.service;

import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
public interface UserInfoService extends IService<UserInfo> {

    UserEntity login(String userName, String password);

    boolean register(String userName,String password);


}
