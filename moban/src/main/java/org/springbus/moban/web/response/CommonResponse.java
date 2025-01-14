package org.springbus.moban.web.response;

import lombok.Data;
import org.springbus.moban.emus.ResponseCode;

import java.io.Serializable;

//一个通用的响应类，用于封装 API 响应数据。
//涵盖了基本的成功/失败状态报告，还能携带任意类型的业务数据，同时提供了清晰的消息反馈机制。
@Data
//泛型参数 <T>：表示响应中可能携带的数据类型是可变的，可以是任何 Java 类型。
public class CommonResponse<T> implements Serializable {

    private Long code;

    private T data;

    private String message;

    private Boolean success;

    private void isSuccess() {
        this.success = Long.valueOf(ResponseCode.SUCCESS.getCode()).equals(code);
    }

    public CommonResponse(ResponseCode responseCode) {
        this.code = Long.valueOf(responseCode.getCode());
        this.message = responseCode.getDesc();
        isSuccess();
    }

    public CommonResponse(String code, String desc) {
        this.code = Long.valueOf(code);
        this.message = desc;
        isSuccess();
    }

    public CommonResponse(String code, String desc, T data) {
        this.code = Long.valueOf(code);
        this.message = desc;
        this.data = data;
        isSuccess();
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        this.code = Long.valueOf(responseCode.getCode());
        this.message = responseCode.getDesc();
        this.data = data;
        isSuccess();
    }

    public CommonResponse<T> desc(String desc) {
        if (desc != null && !desc.trim().isEmpty()) {
            this.message = desc;
        }
        isSuccess();
        return this;
    }

    public CommonResponse(T data) {
        this.code = Long.valueOf(ResponseCode.SUCCESS.getCode());
        this.message = ResponseCode.SUCCESS.getDesc();
        this.data = data;
        isSuccess();
    }
}
