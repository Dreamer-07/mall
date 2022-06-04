package pers.prover.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.ware.entity.PurchaseDetailEntity;
import pers.prover.mall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:30:45
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void done(List<PurchaseDetailEntity> purchaseDetailEntities);

    /**
     * 获取 skuids 中分别对应的库存信息
     * @param skuIds
     * @return
     */
    Map<Long, Boolean> listStockInfo(List<Long> skuIds);
}

