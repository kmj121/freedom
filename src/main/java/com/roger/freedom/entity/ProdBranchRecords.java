package com.roger.freedom.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 记录每次提交到prod上的内容
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
     * merge之前的id
     */
    private String before;

    /**
     * merge之后的id
     */
    private String after;

    /**
     * merge中包含的commit id
     */
    private String commitId;

    /**
     * 此提交是否与之前推送的任何提交不同，0:false 1:true
     */
    private Integer distinct;

    /**
     * 创建时间
     */
    private Date createTime;


}
