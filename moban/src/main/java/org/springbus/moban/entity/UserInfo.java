package org.springbus.moban.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_info")
@Schema(description = "用户信息表")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "班级ID")
    @TableField("clazz_id")
    private Long clazzId;

    @Schema(description = "名字")
    @TableField("user_name")
    private String userName;

    @Schema(description = "账号")
    @TableField("account")
    private String account;

    @Schema(description = "密码（加密后的密码）")
    @TableField("password")
    private String password;

    @Schema(description = "电子邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "教学方法")
    @TableField("method")
    private String method;

    @Schema(description = "创建人")
    @TableField("created")
    private String created;

    @Schema(description = "创建时间")
    @TableField("created_date")
    private Date createdDate;

    @Schema(description = "修改人")
    @TableField("modified")
    private String modified;

    @Schema(description = "更新时间")
    @TableField("modified_date")
    private Date modifiedDate;
}
