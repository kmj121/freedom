package com.roger.freedom.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.KnowledgeBaseRelateProject;
import com.roger.freedom.entity.MasterBranchRecords;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kmj123
 * @since 2021-09-30
 */
public interface MasterBranchRecordsService extends IService<MasterBranchRecords> {
    /**
     * 增加
     * @param masterBranchRecords
     * @return
     */
    int insert(MasterBranchRecords masterBranchRecords);

    /**
     * 查询全部
     * @param queryWrapper
     * @return
     */
    List<MasterBranchRecords> selectAll(Wrapper<MasterBranchRecords> queryWrapper);

    /**
     * 据ID而查
     * @param id
     * @return
     */
    MasterBranchRecords selectById(Integer id);


    /**
     * 更改1行
     * @param masterBranchRecords
     * @return
     */
    int updateOne(MasterBranchRecords masterBranchRecords);

    /**
     * 删除一行
     * @param masterBranchRecords
     * @return
     */
    int deleteOne(MasterBranchRecords masterBranchRecords);
}
