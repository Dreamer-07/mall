package pers.prover.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.ware.entity.PurchaseEntity;
import pers.prover.mall.ware.vo.MergeVo;
import pers.prover.mall.ware.vo.PurchaseReqVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:30:45
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未被领取的采购单
     * @return
     */
    PageUtils queryUnreceiveList(Map<String, Object> params);

    /**
     * 保存合并采购需求
     * @param mergeVo
     */
    void saveMegre(MergeVo mergeVo);

    /**
     * 领取采购单
     * @param purchaseIds
     */
    void received(List<Long> purchaseIds);

    /**
     * 记录已经完成的采购但
     * @param purchaseReqVo
     */
    void done(PurchaseReqVo purchaseReqVo);

    /**
     * 修改采购单详情
     * @param purchase
     */
    void updateDetail(PurchaseEntity purchase);
}

