package org.springbus.moban.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 班级信息表
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "班级信息表")
public class ClazzEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="唯一ID")
    private Long id;

    @Schema(description ="年级ID")
    private Long gradeId;

    @Schema(description ="年级")
    private String grade;

    @Schema(description ="学校名称")
    private String schoolName;

    @Schema(description ="班级名")
    private String clazzName;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "教学方法")
    private String method;
}
