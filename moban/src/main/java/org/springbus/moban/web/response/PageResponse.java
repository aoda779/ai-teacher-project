package org.springbus.moban.web.response;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springbus.moban.emus.ResponseCode;
import java.util.List;

@Data
public class PageResponse<T> {

    private Long code;
    private String message;
    private Page<T> data;
    private Boolean success;

    private void isSuccess() {
        this.success = Long.valueOf(ResponseCode.SUCCESS.getCode()).equals(code);
    }

    //提供了多个构造函数以适应不同的使用场景
    // 1. 默认构造函数，用于创建一个成功响应对象，默认的响应码和消息为成功。
    public PageResponse(ResponseCode responseCode) {
        this.code = Long.valueOf(responseCode.getCode());
        this.message = responseCode.getDesc();
        this.data = new Page<>();
        isSuccess();
    }

//    带数据构造函数：
    //用于创建一个成功的分页响应，带有具体的分页数据和总记录数。
//如果传入的数据列表为空，则更新消息为“无数据”。
    public PageResponse(List<T> data, long total) {
        this.code = Long.valueOf(ResponseCode.SUCCESS.getCode());
        this.message = ResponseCode.SUCCESS.getDesc();
        Page<T> page = new Page<>();
        page.setItems(data);
        page.setTotal(total);
        if(CollectionUtils.isEmpty(data)){
            this.message =ResponseCode.NO_DATA.getDesc();
        }
        this.data = page;
        isSuccess();
    }

    public PageResponse(String responseCode, String responseDesc) {
        this.code = Long.valueOf(responseCode);
        this.message = responseDesc;
        this.data = new Page<>();
        isSuccess();
    }

    //提供最全面的构造方式，允许指定所有的响应属性，包括状态码、描述、分页数据和总记录数。
    public PageResponse(String responseCode, String responseDesc,List<T> data, long total) {
        this.code = Long.valueOf(responseCode);
        this.message = responseDesc;
        this.data = new Page<>();
        this.data.setItems(data);
        this.data.setTotal(total);
        if(CollectionUtils.isEmpty(data)){
            this.message =ResponseCode.NO_DATA.getDesc();
        }
        isSuccess();
    }
}
