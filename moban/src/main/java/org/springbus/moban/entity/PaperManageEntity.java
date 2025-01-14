package org.springbus.moban.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springbus.moban.web.request.BaseRequest;

import java.io.Serializable;

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
public class PaperManageEntity extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="唯一ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="班级ID")
    @TableField("clazz_id")
    private Long clazzId;

    @Schema(description ="科目ID(冗余字段)")
    @TableField("subject_id")
    private Long subjectId;

    @Schema(description ="科目名称")
    @TableField("subject_name")
    private String subjectName;

    @Schema(description ="试卷名称")
    @TableField("name")
    private String name;

    @Schema(description ="年级")
    @TableField("grade")
    private String grade;

    @Schema(description ="时间")
    @TableField("paper_time")
    private String paperTime;

    @Schema(description ="学校名称")
    @TableField("school_name")
    private String schoolName;

    @Schema(description ="班级名")
    @TableField("clazz_name")
    private String clazzName;

}
