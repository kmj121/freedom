package com.roger.freedom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kmj123
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KnowledgeBaseRelateProjectDetail implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * knowledge_base_relate_project表中id
     */
    private Integer knowledgeBaseRelateProjectId;

    /**
     * 文件名（服务器上的绝对路径）
     */
    private String fileName;

    /**
     * 文件中注释内容开始
     */
    private String beginAnnotation;

    /**
     * 文件中注释内容结束
     */
    private String endAnnotation;

    /**
     * 开始注释和结束注释之间的内容
     */
    private String content;


}
