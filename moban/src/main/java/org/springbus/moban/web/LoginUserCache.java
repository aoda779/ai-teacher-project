package org.springbus.moban.web;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.emus.ResponseCode;
import org.springbus.moban.exception.CommonException;
import org.springbus.moban.web.entity.UserEntity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class LoginUserCache {

    public static final ConcurrentMap<String, UserEntity> GUAVA_CACHE = new ConcurrentHashMap<>();


    public static void putGuavaCache(String token, UserEntity userEntity) {
        if (StringUtils.isBlank(token)) {
            throw new CommonException(ResponseCode.PARAM_ERROR, "token is null");
        }
        GUAVA_CACHE.put(token, userEntity);
    }

    public static UserEntity getGuavaCache(String token) {
        if (StringUtils.isBlank(token)) {
//            throw new CommonException(ResponseCode.PARAM_ERROR,"token is null");
            return null;
        }
        return GUAVA_CACHE.get(token);
    }

    public static void removeGuavaCache(String token) {
        if (StringUtils.isBlank(token)) {
            throw new CommonException(ResponseCode.PARAM_ERROR, "token is null");
        }
        GUAVA_CACHE.remove(token);
    }

    public static void main(String[] args) {
//        LoginUserCache.putGuavaCache("55038d2ae95c4104bdfef07a35dd35d1", new UserEntity().setUserName("admin"));
        UserEntity guavaCache = LoginUserCache.getGuavaCache("d7152cb81b1a42a98de7c89257993ad0");
        System.out.println(JSON.toJSONString(guavaCache));
    }
}
