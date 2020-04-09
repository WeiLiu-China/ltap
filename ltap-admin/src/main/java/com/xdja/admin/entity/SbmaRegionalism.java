package com.xdja.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行政区划表
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_SBMA_REGIONALISM")
public class SbmaRegionalism extends Model<SbmaRegionalism> {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区划代码CODE
     */
    @TableField("CODE")
    private String code;

    /**
     * 行政区划名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 父级政区划代码（顶级区划代码的父级区划代码是空）
     */
    @TableField("PARENT_CODE")
    private String parentCode;

    /**
     * 最后更新时间戳
     */
    @TableField("LAST_UPDATE_TIME")
    private Long lastUpdateTime;

    /**
     * 创建时间戳
     */
    @TableField("CREATE_TIME")
    private Long createTime;

    /**
     * 状态0删除1正常
     */
    @TableField("STATUS")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
