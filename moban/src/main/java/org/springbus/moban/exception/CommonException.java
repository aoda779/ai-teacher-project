package org.springbus.moban.exception;


import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.emus.ResponseCode;
//CommonException 类主要用于处理应用程序中可能发生的各种异常情况，并提供了一种标准化的方式来传递错误信息、状态码和附加数据。
public class CommonException extends RuntimeException {

    private final ResponseCode code;

    private String message;

    private Object data;

    public CommonException(ResponseCode code) {
        this.code = code;
        this.message = code.getDesc();
    }

    public CommonException(ResponseCode code, String message) {
        this.code = code;
        this.message = message;
        if (StringUtils.isBlank(message) && code != null) {
//            如果提供的消息为空或仅包含空白字符，则使用 ResponseCode 中定义的描述信息作为默认值。
            this.message = code.getDesc();
        }
    }

    public CommonException(Throwable e, ResponseCode code) {
        super(e);
        this.code = code;
        this.message = code.getDesc();
    }

    public ResponseCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
