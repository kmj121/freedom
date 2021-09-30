package com.roger.freedom.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.KnowledgeBaseRelateProject;
import com.roger.freedom.entity.MasterBranchRecords;
import com.roger.freedom.mapper.MasterBranchRecordsMapper;
import com.roger.freedom.service.MasterBranchRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kmj123
 * @since 2021-09-30
 */
@Service
public class MasterBranchRecordsServiceImpl extends ServiceImpl<MasterBranchRecordsMapper, MasterBranchRecords> implements MasterBranchRecordsService {

    @Override
    public int insert(MasterBranchRecords masterBranchRecords) {
        return baseMapper.insert(masterBranchRecords);
    }

    @Override
    public List<MasterBranchRecords> selectAll(Wrapper<MasterBranchRecords> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public MasterBranchRecords selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int updateOne(MasterBranchRecords masterBranchRecords) {
        return baseMapper.updateById(masterBranchRecords);
    }

    @Override
    public int deleteOne(MasterBranchRecords masterBranchRecords) {
        return baseMapper.deleteById(masterBranchRecords);
    }
}
