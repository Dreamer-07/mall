package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.SkuImagesEntity;
import pers.prover.mall.product.vo.SpuSaveVo;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Long skuId, List<SpuSaveVo.Skus.Images> images);

    /**
     * 根据 skuId 获取对应的 skuImage
     * @param skuId
     * @return
     */
    List<SkuImagesEntity> listBySkuId(Long skuId);
}

