package com.roger.freedom.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.KnowledgeBaseRelateProject;
import com.roger.freedom.mapper.KnowledgeBaseRelateProjectMapper;
import com.roger.freedom.service.KnowledgeBaseRelateProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kmj123
 * @since 2021-09-29
 */
@Service
public class KnowledgeBaseRelateProjectServiceImpl extends ServiceImpl<KnowledgeBaseRelateProjectMapper, KnowledgeBaseRelateProject> implements KnowledgeBaseRelateProjectService {

    @Autowired
    private KnowledgeBaseRelateProjectMapper projectMapper;

    @Override
    public int insert(KnowledgeBaseRelateProject project) {
        return baseMapper.insert(project);
    }

    @Override
    public List<KnowledgeBaseRelateProject> selectAll(Wrapper<KnowledgeBaseRelateProject> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public KnowledgeBaseRelateProject selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public KnowledgeBaseRelateProject selectBySvnUrl(String svnUrl) {
        return projectMapper.selectBySvnUrl(svnUrl);
    }

    @Override
    public int updateOne(KnowledgeBaseRelateProject project) {
        return baseMapper.updateById(project);
    }

    @Override
    public int deleteOne(KnowledgeBaseRelateProject project) {
        return baseMapper.deleteById(project);
    }
}
