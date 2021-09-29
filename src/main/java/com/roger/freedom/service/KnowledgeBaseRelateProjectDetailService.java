package com.roger.freedom.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.KnowledgeBaseRelateProject;
import com.roger.freedom.entity.KnowledgeBaseRelateProjectDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kmj123
 * @since 2021-09-29
 */
public interface KnowledgeBaseRelateProjectDetailService extends IService<KnowledgeBaseRelateProjectDetail> {
    /**
     * 增加
     * @param detail
     * @return
     */
    int insert(KnowledgeBaseRelateProjectDetail detail);

    /**
     * 查询全部
     * @param queryWrapper
     * @return
     */
    List<KnowledgeBaseRelateProjectDetail> selectAll(Wrapper<KnowledgeBaseRelateProjectDetail> queryWrapper);

    /**
     * 据ID而查
     * @param id
     * @return
     */
    KnowledgeBaseRelateProjectDetail selectById(Integer id);

    /**
     * 据项目ID而查
     * @param projectId
     * @return
     */
    List<KnowledgeBaseRelateProjectDetail> selectByProjectId(Integer projectId);

    /**
     * 更改1行
     * @param detail
     * @return
     */
    int updateOne(KnowledgeBaseRelateProjectDetail detail);

    /**
     * 删除一行
     * @param detail
     * @return
     */
    int deleteOne(KnowledgeBaseRelateProjectDetail detail);

}
