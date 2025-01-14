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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 试卷信息表
 * </p>
 *
 * @author Mr.Han
 * @since 2024-12-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("paper_manage")
@Schema(description = "试卷信息表")
public class PaperManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="唯一ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="试卷名称")
    @TableField("name")
    private String name;

    @Schema(description ="班级ID")
    @TableField("clazz_id")
    private Long clazzId;

    @Schema(description ="科目ID(冗余字段)")
    @TableField("subject_id")
    private Long subjectId;

    @Schema(description ="科目名称")
    @TableField("subject_name")
    private String subjectName;


    @Schema(description ="阅卷老师ID")
    @TableField("teacher_id")
    private Long teacherId;

    @Schema(description ="阅卷老师名称")
    @TableField("teacher")
    private String teacher;

    @Schema(description ="成绩")
    @TableField("grades")
    private BigDecimal grades;

    @Schema(description ="扩展字段")
    @TableField("extended_field")
    private String extendedField;

    @Schema(description ="时间")
    @TableField("paper_time")
    private Date paperTime;

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
