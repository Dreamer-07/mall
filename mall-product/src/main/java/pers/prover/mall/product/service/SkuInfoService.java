package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SpuInfoEntity;
import pers.prover.mall.product.vo.SpuSaveVo;
import pers.prover.mall.product.vo.api.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存 sku 信息
     * @param sku
     * @param spuInfoEntity
     * @param defaultImg
     * @return
     */
    Long save(SpuSaveVo.Skus sku, SpuInfoEntity spuInfoEntity, String defaultImg);

    /**
     * 获取 sku 名称
     * @param skuId
     * @return
     */
    String getSkuName(Long skuId);

    /**
     * 根据 spu 获取对应的 skuInfo
     * @param spuId
     * @return
     */
    List<SkuInfoEntity> listBySpuId(Long spuId);

    /**
     * 获取 sku 信息
     * @param skuId
     * @return
     */
    SkuItemVo getSkuItem(Long skuId);
}

