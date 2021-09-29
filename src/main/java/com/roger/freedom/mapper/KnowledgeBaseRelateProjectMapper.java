package com.roger.freedom.mapper;

import com.roger.freedom.entity.KnowledgeBaseRelateProject;
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
public interface KnowledgeBaseRelateProjectMapper extends BaseMapper<KnowledgeBaseRelateProject> {

    /**
     * 根据svnUrl查询
     * @param svnUrl
     * @return
     */
    KnowledgeBaseRelateProject selectBySvnUrl(String svnUrl);
}
