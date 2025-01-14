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
 * 班级信息表
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Getter
@Setter
@Accessors(chain = true)
//使用 @TableName 注解可以显式地指定实体类与数据库表的映射关系，避免了可能的命名不一致问题，确保了映射的准确性。
@TableName("clazz_info")
@Schema(description = "班级信息表")
public class ClazzInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="唯一ID")
//    @Schema(description = "唯一ID"): 为 OpenAPI 文档提供描述。
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="年级ID(冗余字段)")
    @TableField("grade_id")
    private Long gradeId;

    @Schema(description ="年级")
    @TableField("grade")
    private String grade;

    @Schema(description ="学校名称")
    @TableField("school_name")
    private String schoolName;

    @Schema(description ="班级名")
    @TableField("clazz_name")
    private String clazzName;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description ="创建人")
    @TableField("created")
    private String created;

    @Schema(description ="创建时间")
    @TableField("created_date")
    private Date createdDate;

    @Schema(description ="修改人")
    @TableField("modified")
    private String modified;

    @Schema(description ="更新时间")
    @TableField("modified_date")
    private Date modifiedDate;
}
