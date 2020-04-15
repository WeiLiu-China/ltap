package com.xdja.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人员信息表
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_PERSON")
public class Person extends Model<Person> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("ID")
    private String id;

    /**
     * 终端登录上线时间
     */
    @TableField("LOGIN_TIME")
    private LocalDateTime loginTime;

    /**
     * 警号
     */
    @TableField("CODE")
    private String code;

    /**
     * 运营商编号，1移动，2联通 3电信(加固使用，暂时保留，一人一卡时录入页面提供)
     */
    @TableField("COMM_TYPE")
    private String commType;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 单位ID
     */
    @TableField("DEP_ID")
    private String depId;

    /**
     * 是否删除标志字段，0未删除；1删除。默认为0
     */
    @TableField("FLAG")
    private String flag;

    /**
     * 级别
     */
    @TableField("GRADE")
    private String grade;

    /**
     * 身份证号
     */
    @TableField("IDENTIFIER")
    private String identifier;

    /**
     * 最后一次密码修改时间
     */
    @TableField("LAST_CHANGE_PW_DATE")
    private LocalDateTime lastChangePwDate;

    /**
     * 最后一次失败登录时间（暂时不用）
     */
    @TableField("LAST_ERRLOGIN_DATE")
    private LocalDateTime lastErrloginDate;

    /**
     * 最近密码更新时间（暂时不用）
     */
    @TableField("LAST_UPDATEPASS_TIME")
    private LocalDateTime lastUpdatepassTime;

    /**
     * 领导级别
     */
    @TableField("LEADER_LEVEL")
    private String leaderLevel;

    /**
     * 终端锁定状态
     */
    @TableField("CLIENT_LOCK_STATE")
    private String clientLockState;

    /**
     * 后台登录失败次数
     */
    @TableField("ERROR_TIMES")
    private Long errorTimes;

    /**
     * 手机号(加固使用，暂时保留，一人一卡时存入)
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 备注
     */
    @TableField("NOTE")
    private String note;

    /**
     * 办公电话
     */
    @TableField("OFFICE_PHONE")
    private String officePhone;

    /**
     * 排序号
     */
    @TableField("ORDER_FIELD")
    private Long orderField;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 人员状态,state为0或null不显示，为1显示
     */
    @TableField("STATE")
    private String state;

    /**
     * 警种
     */
    @TableField("POLICE")
    private String police;

    /**
     * 职级
     */
    @TableField("POSITION")
    private String position;

    /**
     * 职务（手工录入，暂时不用）
     */
    @TableField("POSITION_INPUT")
    private String positionInput;

    /**
     * 手机小号（暂时不用）
     */
    @TableField("SMOBILE")
    private String smobile;

    /**
     * 性别
     */
    @TableField("SEX")
    private String sex;

    /**
     * 后台主题
     */
    @TableField("THEME")
    private String theme;

    /**
     * 更新时间戳
     */
    @TableField("N_LAST_UPDATE_TIME")
    private Long lastUpdateTime;

    /**
     * 单位编码
     */
    @TableField("DEP_CODE")
    private String depCode;

    /**
     * 姓名拼音简拼
     */
    @TableField("NAME_BRIEF_SPELL")
    private String nameBriefSpell;

    /**
     * 人员类型 详见码表code=PERSON_TYPE
     */
    @TableField("PERSON_TYPE")
    private String personType;

    /**
     * PC端唯一编号
     */
    @TableField("PC_ID")
    private String pcId;

    /**
     * 显示状态 ：  1显示 2不显示
     */
    @TableField("DISPLAY_STATE")
    private String displayState;

    /**
     * 快捷菜单
     */
    @TableField("SHORTCUT_MENU")
    private String shortcutMenu;

    /**
     * 私人电话
     */
    @TableField("MOBILE_PERSONAL")
    private String mobilePersonal;

    /**
     * 彩信电话
     */
    @TableField("MOBILE_MULTIMEDIA_MESSAGE")
    private String mobileMultimediaMessage;

    /**
     * 警信用户 0不是，1是
     */
    @TableField("JX_FLAG")
    private String jxFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
