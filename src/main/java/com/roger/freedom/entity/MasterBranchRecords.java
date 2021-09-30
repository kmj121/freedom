package com.roger.freedom.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 记录每次提交到master上的并且对知识库维护内容有改动的内容
 * </p>
 *
 * @author kmj123
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MasterBranchRecords implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
      private Integer id;

    /**
     * 每次提交到gitlab master分支的id
     */
    private String commitId;

    /**
     * 提交者的用户名
     */
    private String authorUsername;

    /**
     * 提交者的邮箱
     */
    private String authorEmail;

    /**
     * 改动涉及到的具体内容的id
     */
    private Integer detailId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * commit的时间戳
     */
    private Integer timestamp;


}
