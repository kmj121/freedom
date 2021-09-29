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
public class KnowledgeBaseRelateProject implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目的gitlab url地址
     */
    private String svnUrl;

    /**
     * 项目所在服务器上的绝对路径，包括项目文件夹名
     */
    private String projectPath;


}
