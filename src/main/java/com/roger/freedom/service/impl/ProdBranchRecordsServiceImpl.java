package com.roger.freedom.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.ProdBranchRecords;
import com.roger.freedom.mapper.ProdBranchRecordsMapper;
import com.roger.freedom.service.ProdBranchRecordsService;
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
public class ProdBranchRecordsServiceImpl extends ServiceImpl<ProdBranchRecordsMapper, ProdBranchRecords> implements ProdBranchRecordsService {

    @Override
    public int insert(ProdBranchRecords prodBranchRecords) {
        return baseMapper.insert(prodBranchRecords);
    }

    @Override
    public List<ProdBranchRecords> selectAll(Wrapper<ProdBranchRecords> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ProdBranchRecords selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int updateOne(ProdBranchRecords prodBranchRecords) {
        return baseMapper.updateById(prodBranchRecords);
    }

    @Override
    public int deleteOne(ProdBranchRecords prodBranchRecords) {
        return baseMapper.deleteById(prodBranchRecords);
    }
}
