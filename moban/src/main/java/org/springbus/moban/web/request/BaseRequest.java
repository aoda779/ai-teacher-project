package org.springbus.moban.web.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRequest implements Serializable {

    /**
     * 操作人
     */
    private String operator;
    private String sysCode;
    private String token;
    private Integer limit;
    private Integer page;
}
