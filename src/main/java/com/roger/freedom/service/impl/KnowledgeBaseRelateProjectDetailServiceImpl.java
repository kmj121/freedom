package com.roger.freedom.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.KnowledgeBaseRelateProjectDetail;
import com.roger.freedom.mapper.KnowledgeBaseRelateProjectDetailMapper;
import com.roger.freedom.service.KnowledgeBaseRelateProjectDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class KnowledgeBaseRelateProjectDetailServiceImpl extends ServiceImpl<KnowledgeBaseRelateProjectDetailMapper, KnowledgeBaseRelateProjectDetail> implements KnowledgeBaseRelateProjectDetailService {

    @Autowired
    private KnowledgeBaseRelateProjectDetailMapper detailMapper;

    @Override
    public int insert(KnowledgeBaseRelateProjectDetail detail) {
        return baseMapper.insert(detail);
    }

    @Override
    public List<KnowledgeBaseRelateProjectDetail> selectAll(Wrapper<KnowledgeBaseRelateProjectDetail> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public KnowledgeBaseRelateProjectDetail selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<KnowledgeBaseRelateProjectDetail> selectByProjectId(Integer projectId) {
        return detailMapper.selectByProjectId(projectId);
    }

    @Override
    public int updateOne(KnowledgeBaseRelateProjectDetail detail) {
        return baseMapper.updateById(detail);
    }

    @Override
    public int deleteOne(KnowledgeBaseRelateProjectDetail detail) {
        return baseMapper.deleteById(detail);
    }
}
