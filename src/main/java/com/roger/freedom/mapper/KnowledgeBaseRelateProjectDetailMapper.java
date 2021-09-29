package com.roger.freedom.mapper;

import com.roger.freedom.entity.KnowledgeBaseRelateProject;
import com.roger.freedom.entity.KnowledgeBaseRelateProjectDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kmj123
 * @since 2021-09-29
 */
@Component
public interface KnowledgeBaseRelateProjectDetailMapper extends BaseMapper<KnowledgeBaseRelateProjectDetail> {
    /**
     * 根据knowledge_base_relate_project_id查询
     * @param projectId
     * @return
     */
    List<KnowledgeBaseRelateProjectDetail> selectByProjectId(Integer projectId);
}
