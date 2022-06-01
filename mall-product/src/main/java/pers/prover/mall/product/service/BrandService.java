package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 级联更新
     * @param brand
     */
    void updateCascade(BrandEntity brand);
}

