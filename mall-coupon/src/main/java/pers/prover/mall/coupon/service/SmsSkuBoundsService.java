package pers.prover.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.coupon.entity.SmsSkuBoundsEntity;

import java.util.Map;

/**
 * 商品sku积分设置
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:26:15
 */
public interface SmsSkuBoundsService extends IService<SmsSkuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

