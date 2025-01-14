package org.springbus.moban.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springbus.moban.web.request.BaseRequest;

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
public class UserEntity extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一ID")
    private Long id;

    @Schema(description = "班级ID")
    private String clazzId;

    @Schema(description = "名字")
    private String userName;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码（加密后的密码）")
    private String password;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "教学方法")
    private String method;

    @Schema(description = "年级")
    private String grade;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "班级名")
    private String clazzName;

    /* * 最后一次活动时间
     *用于缓存中过期 默认30分钟过期
     */
    private Date activityTime;
}
