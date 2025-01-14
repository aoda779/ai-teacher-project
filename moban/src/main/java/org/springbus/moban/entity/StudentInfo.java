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
 * 学生信息表
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("student_info")
@Schema(description = "学生信息表")
public class StudentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "学号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "班级ID")
    @TableField("clazz_id")
    private Long clazzId;

    @Schema(description = "姓名")
    @TableField("name")
    private String name;

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
