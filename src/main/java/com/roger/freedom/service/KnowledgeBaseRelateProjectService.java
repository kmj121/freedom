package com.roger.freedom.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.KnowledgeBaseRelateProject;
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
public interface KnowledgeBaseRelateProjectService extends IService<KnowledgeBaseRelateProject> {

    /**
     * 增加
     * @param project
     * @return
     */
    int insert(KnowledgeBaseRelateProject project);

    /**
     * 查询全部
     * @param queryWrapper
     * @return
     */
    List<KnowledgeBaseRelateProject> selectAll(Wrapper<KnowledgeBaseRelateProject> queryWrapper);

    /**
     * 据ID而查
     * @param id
     * @return
     */
    KnowledgeBaseRelateProject selectById(Integer id);

    /**
     * 据svnUrl而查
     * @param svnUrl
     * @return
     */
    KnowledgeBaseRelateProject selectBySvnUrl(String svnUrl);

    /**
     * 更改1行
     * @param project
     * @return
     */
    int updateOne(KnowledgeBaseRelateProject project);

    /**
     * 删除一行
     * @param project
     * @return
     */
    int deleteOne(KnowledgeBaseRelateProject project);
}
