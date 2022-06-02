package pers.prover.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.ware.entity.PurchaseDetailEntity;
import pers.prover.mall.ware.vo.PurchaseReqVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:30:45
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(Long id, Integer status);

    /**
     * 修改采购需求详情状态
     * @param getPurchaseDetailReqVos
     */
    void done(List<PurchaseReqVo.PurchaseDetailReqVo> getPurchaseDetailReqVos);
}

