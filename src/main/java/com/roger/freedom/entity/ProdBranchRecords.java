package com.roger.freedom.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kmj123
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProdBranchRecords implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
      private Integer id;

    /**
     * merge id
     */
    private String mergeId;

    /**
     * 所有commitId，用逗号隔开
     */
    @TableField("commitIds")
    private String commitIds;

    /**
     * 点击merge的时间
     */
    private String mergeTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
