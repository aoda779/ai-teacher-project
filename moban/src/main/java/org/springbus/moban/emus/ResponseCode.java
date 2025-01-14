package org.springbus.moban.emus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("200", "SUCCESS"),
    PARAM_ERROR("101", "参数错误"),
    NO_AUTH("102", "无权限"),
    RPC_ERROR("103", "调用外部接口异常"),
    SYSTEM_ERROR("20001", "系统异常"),
    NO_DATA("104", "无数据"),
    BUSINESS_ERROR("105", "业务错误"),
    DB_ERROR("106", "数据库错误")
    ;

    private final String code;
    private final String desc;

//    义了一组标准的 API 响应状态码及描述，并提供了方便的方法来根据状态码查找对应的枚举实例。这有助于在整个应用程序中统一管理响应状态，确保一致性，并且使代码更易于维护和扩展。
    public static ResponseCode getByCode(String code) {
        if (code != null && !code.trim().isEmpty()) {
            ResponseCode[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ResponseCode item = var1[var3];
                if (item.getCode().equals(code)) {
                    return item;
                }
            }

            return null;
        } else {
            return null;
        }
    }
}
