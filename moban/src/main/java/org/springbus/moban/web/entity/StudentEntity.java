package org.springbus.moban.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springbus.moban.web.request.BaseRequest;

import java.io.Serializable;

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
@Schema(description = "学生信息表")
public class StudentEntity extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "学号")
    private Long id;

    @Schema(description = "班级ID")
    private Long clazzId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "学校")
    private String schoolName;

    @Schema(description = "年级")
    private String grade;

    @Schema(description = "班级")
    private String clazzName;

}
