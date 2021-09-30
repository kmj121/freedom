package com.roger.freedom.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.roger.freedom.entity.MasterBranchRecords;
import com.roger.freedom.entity.ProdBranchRecords;
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
public interface ProdBranchRecordsService extends IService<ProdBranchRecords> {
    /**
     * 增加
     * @param prodBranchRecords
     * @return
     */
    int insert(ProdBranchRecords prodBranchRecords);

    /**
     * 查询全部
     * @param queryWrapper
     * @return
     */
    List<ProdBranchRecords> selectAll(Wrapper<ProdBranchRecords> queryWrapper);

    /**
     * 据ID而查
     * @param id
     * @return
     */
    ProdBranchRecords selectById(Integer id);


    /**
     * 更改1行
     * @param prodBranchRecords
     * @return
     */
    int updateOne(ProdBranchRecords prodBranchRecords);

    /**
     * 删除一行
     * @param prodBranchRecords
     * @return
     */
    int deleteOne(ProdBranchRecords prodBranchRecords);

}
