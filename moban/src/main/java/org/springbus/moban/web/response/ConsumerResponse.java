
package org.springbus.moban.web.response;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema( description = "客户信息表")
public class ConsumerResponse implements Serializable {

    @Schema(description ="创建时间")
    private Date gmtCreate;

    @Schema(description ="修改时间")
    private Date gmtModified;

    @Schema(description ="唯一ID")
    private Long id;

    @Schema(description ="昵称")
    private String nickName;

    @Schema(description ="密码")
    private String password;

    @Schema(description ="角色名称")
    private String roleName;

    @Schema(description ="salt")
    private String salt;

    @Schema(description ="token")
    private String token;

    @Schema(description ="用户名称")
    private String username;

    @Schema(description ="手机号")
    @TableField("phone")
    private String phone;

    @Schema(description ="邮箱")
    @TableField("email")
    private String email;

    @Schema(description ="来源")
    private String source;

    @Schema(description ="0-有效 1-无效")
    private String status;

}
