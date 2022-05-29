package pers.prover.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 16:44:02
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

